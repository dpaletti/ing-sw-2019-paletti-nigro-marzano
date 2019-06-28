package it.polimi.se2019.view;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.ui_events.*;
import it.polimi.se2019.view.vc_events.PowerUpUsageEvent;
import it.polimi.se2019.view.vc_events.SpawnEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.MalformedURLException;
import java.nio.file.Paths;

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
        String position;
        if(message.getPowerUp().equalsIgnoreCase(left))
            position = "Left";
        else if(message.getPowerUp().equalsIgnoreCase(right))
            position = "Right";
        else if(message.getPowerUp().equalsIgnoreCase(middle))
            position = "Middle";
        else
            throw new IllegalArgumentException("Could not find powerup");
        activatePowerup(position, message.getPowerUp());
    }

    @Override
    public void dispatch(UiTurnEnd message){
        removeHandlers(powerupLeft);
        removeHandlers(powerupMiddle);
        removeHandlers(powerupRight);

    }
}
