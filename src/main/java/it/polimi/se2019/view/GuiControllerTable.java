package it.polimi.se2019.view;

import it.polimi.se2019.view.gui_events.GuiInitialize;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class GuiControllerTable extends GuiController {


    @FXML
    private ImageView hp1;

    @FXML
    private ImageView hp2;

    @FXML
    private ImageView hp3;

    @FXML
    private ImageView hp4;

    @FXML
    private ImageView hp5;

    @FXML
    private ImageView hp6;

    @FXML
    private ImageView hp7;

    @FXML
    private ImageView hp8;

    @FXML
    private ImageView hp9;

    @FXML
    private ImageView hp10;

    @FXML
    private ImageView hp11;

    @FXML
    private ImageView hp12;

    @FXML
    private ImageView mark1;

    @FXML
    private ImageView mark2;

    @FXML
    private ImageView mark3;

    @FXML
    private ImageView mark4;

    @FXML
    private ImageView mark5;

    @FXML
    private ImageView mark6;

    @FXML
    private ImageView mark7;

    @FXML
    private ImageView mark8;

    @FXML
    private ImageView mark9;

    @FXML
    private ImageView mark10;

    @FXML
    private ImageView mark11;

    @FXML
    private ImageView mark12;

    @FXML
    private ImageView mark13;

    @FXML
    private ImageView mark14;

    @FXML
    private ImageView mark15;

    @FXML
    private ImageView skull8;

    @FXML
    private ImageView skull6;

    @FXML
    private ImageView skull2;

    @FXML
    private ImageView skull11;

    @FXML
    private ImageView skull12;

    @FXML
    private Button endTurn;

    @FXML
    private Button leave;

    @FXML
    private ImageView moveAndGrab;

    @FXML
    private Text redAmmo;

    @FXML
    private Text yellowAmmo;

    @FXML
    private Text blueAmmo;

    @FXML
    private ImageView currentPlayer;

    @FXML
    private ImageView run;

    @FXML
    private ImageView shoot;

    @FXML
    private ImageView reload;

        @Override
        public void dispatch(GuiInitialize message) {
        //TODO
        }

    @FXML
    private void clickable(){
        currentPlayer.getScene().setCursor(Cursor.HAND);
    }

    @FXML
    private void notClickable(){
        currentPlayer.getScene().setCursor(Cursor.DEFAULT);
    }


}
