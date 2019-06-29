package it.polimi.se2019.view;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.ui_events.*;
import it.polimi.se2019.view.vc_events.PowerUpUsageEvent;
import it.polimi.se2019.view.vc_events.SpawnEvent;
import it.polimi.se2019.view.vc_events.VCSellPowerUpEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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

    private String left;
    private String middle;
    private String right;

    private int blueSold = 0;
    private int yellowSold = 0;
    private int redSold = 0;

    private List<String> soldPowerups = new ArrayList<>();

    @Override
    public void dispatch(UiPutPowerUp message) {
        try {
            if (powerupLeft.getImage() == null) {
                powerupLeft.setImage(new Image(Paths.get(path + message.getPowerup()).toUri().toURL().toString() + ".png"));
                left = message.getPowerup();
            }
            else if (powerupMiddle.getImage() == null) {
                powerupMiddle.setImage(new Image(Paths.get(path + message.getPowerup()).toUri().toURL().toString() + ".png"));
                middle = message.getPowerup();
            }
            else if (powerupRight.getImage() == null) {
                powerupRight.setImage(new Image(Paths.get(path + message.getPowerup()).toUri().toURL().toString() + ".png"));
                right = message.getPowerup();
            }
            else
                ViewGUI.getInstance().send(new UiShowFourth(message.getPowerup(), false));

        }catch (MalformedURLException e){
            Log.severe("Wrong URL for: " + message.getPowerup());
        }
    }

    @Override
    public void dispatch(UiSpawn message) {
        scene = powerupLeft.getScene();

        powerupLeft.setOnMouseClicked(spawnPowerUp(powerupLeft, left));
        powerupLeft.setOnMouseEntered(clickable(scene));
        powerupLeft.setOnMouseExited(notClickable(scene));

        powerupMiddle.setOnMouseClicked(spawnPowerUp(powerupMiddle, left));
        powerupMiddle.setOnMouseEntered(clickable(scene));
        powerupMiddle.setOnMouseExited(notClickable(scene));

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
            }else
                throw new IllegalArgumentException("Could not find powerUp: " + powerupName);

            ViewGUI.getInstance().send(new SpawnEvent(ViewGUI.getInstance().getUsername(), powerupName, powerUpToKeep));
            powerup.setImage(null);
            removeHandlers(powerup);
        };
    }


    public void activatePowerup(String position, String powerUp){
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
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve asset for highlighting powerup effect");
        }
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
}
