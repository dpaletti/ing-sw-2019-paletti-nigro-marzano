package it.polimi.se2019.client.view;

import it.polimi.se2019.client.view.ui_events.*;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.vc_events.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuiControllerPowerUp extends GuiController {

    @FXML
    private ImageView powerupLeft;

    @FXML
    private ImageView powerupMiddle;

    @FXML
    private ImageView powerupRight;


    private Scene scene;

    private String path = "files/assets/cards/";

    private String left = null;
    private String middle = null;
    private String right = null;

    private int blueSold = 0;
    private int yellowSold = 0;
    private int redSold = 0;

    private List<String> soldPowerups = new ArrayList<>();

    private ImageView current;

    @Override
    public void dispatch(UiPutPowerUp message) {
        try {
            if (left == null) {
                removeHandlers(powerupLeft);
                powerupLeft.setImage(new Image(Paths.get(path + message.getPowerup()).toUri().toURL().toString() + ".png"));
                left = message.getPowerup();
            }
            else if (middle == null) {
                removeHandlers(powerupMiddle);
                powerupMiddle.setImage(new Image(Paths.get(path + message.getPowerup()).toUri().toURL().toString() + ".png"));
                middle = message.getPowerup();
            }
            else if (right == null) {
                removeHandlers(powerupRight);
                powerupRight.setImage(new Image(Paths.get(path + message.getPowerup()).toUri().toURL().toString() + ".png"));
                right = message.getPowerup();
            }
            else {
                ViewGUI.getInstance().send(new UiShowFourth(message.getPowerup(), false));
                powerupRight.setOnMouseEntered(clickable(scene));
                powerupRight.setOnMouseExited(notClickable(scene));
                powerupRight.setOnMouseClicked(handleDiscard(right));

                powerupLeft.setOnMouseEntered(clickable(scene));
                powerupLeft.setOnMouseExited(notClickable(scene));
                powerupLeft.setOnMouseClicked(handleDiscard(left));

                powerupMiddle.setOnMouseEntered(clickable(scene));
                powerupMiddle.setOnMouseExited(notClickable(scene));
                powerupMiddle.setOnMouseClicked(handleDiscard(middle));
            }

        }catch (MalformedURLException e){
            Log.severe("Wrong URL for: " + message.getPowerup());
        }
    }

    private EventHandler<MouseEvent> handleDiscard(String powerUpName){
        return (MouseEvent event) -> {
            ViewGUI.getInstance().send(new DiscardedPowerUpEvent(ViewGUI.getInstance().getUsername(), powerUpName));
            removeHandlers((ImageView) event.getSource());
            notClickableNoHandler(scene);
            if(powerUpName.equals(left))
                left = null;
            else if(powerUpName.equals(middle))
                middle = null;
            else if(powerUpName.equals(right))
                right = null;
        };
    }

    @Override
    public void dispatch(UiSpawn message) {
        scene = powerupLeft.getScene();

        powerupLeft.setOnMouseClicked(spawnPowerUp(powerupLeft, left));
        powerupLeft.setOnMouseEntered(clickable(scene));
        powerupLeft.setOnMouseExited(notClickable(scene));

        powerupMiddle.setOnMouseClicked(spawnPowerUp(powerupMiddle, middle));
        powerupMiddle.setOnMouseEntered(clickable(scene));
        powerupMiddle.setOnMouseExited(notClickable(scene));

        powerupRight.setOnMouseClicked(spawnPowerUp(powerupRight, right));
        powerupRight.setOnMouseEntered(clickable(scene));
        powerupRight.setOnMouseExited(notClickable(scene));
    }

    @Override
    public void dispatch(UiRespawnEvent message) {
        dispatch(new UiSpawn());
    }

    private EventHandler<MouseEvent> spawnPowerUp(ImageView powerup, String powerupName){
        return (MouseEvent event) -> {
            String powerUpToKeep;
            if(powerupName.equals(left)) {
                left = null;
                powerUpToKeep = middle;
            }else if (powerupName.equals(middle)){
                middle = null;
                powerUpToKeep = left;
            }else if(powerupName.equals(right) && ViewGUI.getInstance().isRespawning()){
                right = null;
                ViewGUI.getInstance().send(new SpawnEvent(ViewGUI.getInstance().getUsername(), powerupName));
                powerup.setImage(null);
                removeHandlers(powerup);
                ViewGUI.getInstance().setRespawning(false);
                return;
            }else
                throw new IllegalArgumentException("Could not find powerUp: " + powerupName);

            String colour;
            if(powerupName.contains("Red"))
                colour = "RED";
            else if(powerupName.contains("Blue"))
                colour = "BLUE";
            else if(powerupName.contains("Yellow"))
                colour = "YELLOW";
            else
                throw new IllegalArgumentException(powerupName + " could not be converted to colour");
            ViewGUI.getInstance().send(new SpawnEvent(ViewGUI.getInstance().getUsername(), colour, powerUpToKeep));
            powerup.setImage(null);
            removeHandlers(powerup);
        };
    }



    public ImageView activatePowerup(String position, String powerUp){
        try {
            ImageView effect = ((ImageView) scene.lookup("#effect" + position));
            effect.setImage(new Image(Paths.get("files/assets/rectangle_black.png").toUri().toURL().toString()));
            effect.setOnMouseEntered(clickable(scene));
            effect.setOnMouseExited(notClickable(scene));
            effect.setOnMouseClicked((MouseEvent event) -> {
                ViewGUI.getInstance().send(new PowerUpUsageEvent(ViewGUI.getInstance().getUsername(), powerUp));
                effect.setImage(null);
                removeHandlers(effect);
            });
            return effect;
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve asset for highlighting powerup effect");
        }
        return null;
    }


    @Override
    public void dispatch(UiAvailablePowerup message) {
        activatePowerup(getPositionOnPowerUp(message.getPowerUp()), message.getPowerUp());
    }

    private String getPositionOnPowerUp(String powerUp){
        String position;
        if(powerUp.equalsIgnoreCase(left))
            position = "Left";
        else if(powerUp.equalsIgnoreCase(right))
            position = "Right";
        else if(powerUp.equalsIgnoreCase(middle))
            position = "Middle";
        else
            throw new IllegalArgumentException("Could not find powerup");
        return position;
    }

    @Override
    public void dispatch(UiDisablePowerUpEvent message) {
        disablePowerUp(getPositionOnPowerUp(message.getToDisable()));
    }

    public void dispatch(UiContextSwitch message){
        powerupLeft.setImage(null);
        powerupMiddle.setImage(null);
        powerupRight.setImage(null);
        for(Node n: powerupLeft.getParent().getChildrenUnmodifiable()){
            n.setDisable(true);
        }

        for(Node n: powerupRight.getParent().getChildrenUnmodifiable()){
            n.setDisable(true);
        }

        for(Node n: powerupMiddle.getParent().getChildrenUnmodifiable()){
            n.setDisable(true);
        }
    }

    @Override
    public void dispatch(UiContextSwitchEnd message) {
        try {
            powerupLeft.setImage(new Image(Paths.get(path + left).toUri().toURL().toString() + ".png"));
            powerupMiddle.setImage(new Image(Paths.get(path + middle).toUri().toURL().toString() + ".png"));
            powerupRight.setImage(new Image(Paths.get(path + right).toUri().toURL().toString() + ".png"));

            for(Node n: powerupLeft.getParent().getChildrenUnmodifiable()){
                n.setDisable(false);
            }

            for(Node n: powerupRight.getParent().getChildrenUnmodifiable()){
                n.setDisable(false);
            }

            for(Node n: powerupMiddle.getParent().getChildrenUnmodifiable()){
                n.setDisable(false);
            }

        }catch (MalformedURLException e){
            Log.severe("Could not retrieve powerup when ending Context Switch");
        }
    }

    @Override
    public void dispatch(UiSellPowerups message) {
        for(String powerup: message.getPowerUpsToSell()) {
            scene.lookup("#ammo" + getPositionOnPowerUp(powerup)).setOnMouseEntered(clickable(scene));
            scene.lookup("#ammo" + getPositionOnPowerUp(powerup)).setOnMouseExited(notClickable(scene));
            scene.lookup("#ammo" + getPositionOnPowerUp(powerup)).setOnMouseClicked((MouseEvent event) -> {
                if(powerup.contains("Blue"))
                    blueSold++;
                if(powerup.contains("Red"))
                    redSold++;
                if(powerup.contains("Yellow"))
                    yellowSold++;
                soldPowerups.add(powerup);
                if(blueSold == getQuantityToSell(message.getColoursToMissing(), "blue") &&
                    yellowSold == getQuantityToSell(message.getColoursToMissing(), "yellow") &&
                    redSold == getQuantityToSell(message.getColoursToMissing(), "red")){
                    blueSold = 0;
                    redSold = 0;
                    yellowSold = 0;
                    soldPowerups = new ArrayList<>();
                    ViewGUI.getInstance().send(new VCSellPowerUpEvent(ViewGUI.getInstance().getUsername(), soldPowerups));
                }

            });

        }
    }

    private int getQuantityToSell(Map<String, Integer> map, String colour){
        for(String s: map.keySet()){
            if(s.equalsIgnoreCase(colour))
                return map.get(s);
        }
        throw new IllegalArgumentException("Could not find colour: " + colour  + "in colour map for selling");
    }

    private void disablePowerUp(String position){
        ImageView effect = ((ImageView) scene.lookup("#effect" + position));
        effect.setImage(null);
        effect.setOnMouseExited(null);
        effect.setOnMouseEntered(null);
        effect.setOnMouseClicked(null);

    }

    @Override
    public void dispatch(UiTurnEnd message){
        removeHandlers(powerupLeft);
        removeHandlers(powerupMiddle);
        removeHandlers(powerupRight);

    }

    @Override
    public void dispatch(UiRemovePowerUp message) {
        ImageView toRemove;
        if(left.equals(message.getPowerUp())) {
            toRemove = powerupLeft;
            left = null;
        }
        else if(middle.equals(message.getPowerUp())) {
            toRemove = powerupMiddle;
            middle = null;
        }
        else if(right.equals(message.getPowerUp())) {
            toRemove = powerupRight;
            right = null;
        }
        else
            throw new IllegalArgumentException("Could not remove any powerUp");

        toRemove.setImage(null);
        removeHandlers(toRemove);

    }

    @Override
    public void dispatch(UiDarken message) {
        List<ImageView> powerups = new ArrayList<>();
        powerups.add(powerupLeft);
        powerups.add(powerupMiddle);
        powerups.add(powerupRight);
        for(ImageView i: powerups){
            for(Node n: ((Pane)i.getParent()).getChildren()){
                if(n != powerupLeft && n != powerupMiddle && n!= powerupRight)
                    ((ImageView) n).setImage(null);
            }
        }
    }

    @Override
    public void dispatch(UiActivatePowerup message) {
        String position = getPositionOnPowerUp(message.getToActivate());
        activatePowerup(position, message.getToActivate()).setOnMouseClicked((MouseEvent event) -> {
            ViewGUI.getInstance().send(new ChosenEffectPowerUpEvent(ViewGUI.getInstance().getUsername(), "effect", message.getToActivate()));
            current = ((ImageView) event.getSource());
            ((ImageView) event.getSource()).setImage(null);
            removeHandlers((ImageView) event.getSource());
            ((ImageView)scene.lookup("#powerup" + position)).setImage(null);
        });
    }

    @Override
    public void dispatch(UiPowerUpEnd message) {
        current.setImage(null);
        removeHandlers(current);
    }
}
