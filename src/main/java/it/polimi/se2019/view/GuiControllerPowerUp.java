package it.polimi.se2019.view;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.ui_events.UiAvailablePowerup;
import it.polimi.se2019.view.ui_events.UiPutPowerUp;
import it.polimi.se2019.view.ui_events.UiSpawn;
import it.polimi.se2019.view.ui_events.UiTurnEnd;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GuiControllerPowerUp extends GuiController {

    @FXML
    private ImageView powerupLeft;

    @FXML
    private ImageView effectLeft;

    @FXML
    private ImageView ammoLeft;



    @FXML
    private ImageView powerupMiddle;

    @FXML
    private ImageView effectMiddle;

    @FXML
    private ImageView ammoMiddle;


    @FXML
    private ImageView powerupRight;

    @FXML
    private ImageView effectRight;

    @FXML
    private ImageView ammoRight;

    private String path = "files/assets/cards/";
    private String left;
    private String middle;
    private String right;

    private List<ImageView> active = new ArrayList<>();


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
                ViewGUI.getInstance().fourthPowerUp(message.getPowerup());

        }catch (MalformedURLException e){
            Log.severe("Wrong URL for: " + message.getPowerup());
        }
    }

    @FXML
    private void clickable() {
        powerupLeft.getScene().setCursor(Cursor.HAND);
    }

    @FXML
    private void notClickable() {
        powerupLeft.getScene().setCursor(Cursor.DEFAULT);
    }
    @Override
    public void dispatch(UiSpawn message) {
        enable(powerupLeft);
        powerupLeft.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ViewGUI.getInstance().chooseSpawn(left, middle);
                disable(powerupLeft);
                disable(powerupMiddle);
                powerupLeft.setImage(null);
                left = null;
                notClickable();
            }
        });
        powerupLeft.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clickable();
            }
        });
        powerupLeft.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                notClickable();
            }
        });

        enable(powerupMiddle);
        powerupMiddle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ViewGUI.getInstance().chooseSpawn(left, middle);
                disable(powerupMiddle);
                disable(powerupLeft);
                powerupMiddle.setImage(null);
                middle = null;
                notClickable();
            }
        });
        powerupMiddle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clickable();
            }
        });
        powerupMiddle.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                notClickable();
            }
        });
    }

    private void enable(ImageView imageView){
        imageView.setDisable(false);
        active.add(imageView);
    }

    private void disable(ImageView imageView){
        imageView.setDisable(true);
        active.remove(imageView);
    }

    @Override
    public void dispatch(UiAvailablePowerup message) {
        if(message.getPowerUp().equalsIgnoreCase(left)){
            enable(powerupLeft);
            powerupLeft.setOnMouseClicked((MouseEvent event) -> {
                ViewGUI.getInstance().usePowerUp(message.getPowerUp());
                disable(powerupLeft);
                powerupLeft.setImage(null);
                left = null;
            });

            powerupLeft.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    clickable();
                }
            });
            powerupLeft.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    notClickable();
                }
            });
        }
        if(message.getPowerUp().equalsIgnoreCase(right)){
            enable(powerupRight);
            powerupRight.setOnMouseClicked((MouseEvent event) -> {
                ViewGUI.getInstance().usePowerUp(message.getPowerUp());
                disable(powerupRight);
                powerupRight.setImage(null);
                right = null;
            });

            powerupRight.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    clickable();
                }
            });
            powerupRight.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    notClickable();
                }
            });
        }
        if(message.getPowerUp().equalsIgnoreCase(middle)){
            enable(powerupMiddle);
            powerupMiddle.setOnMouseClicked((MouseEvent event) -> {
                ViewGUI.getInstance().usePowerUp(message.getPowerUp());
                disable(powerupMiddle);
                powerupMiddle.setImage(null);
                middle = null;
            });

            powerupMiddle.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    clickable();
                }
            });
            powerupRight.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    notClickable();
                }
            });

        }
    }

    @Override
    public void dispatch(UiTurnEnd message){
        for (ImageView i: active)
            disable(i);

    }
}
