package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.view.gui_events.GuiInitialize;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class GuiControllerTable implements GuiController {
    //TODO disable frenzy regions and then activate them when needed

    @FXML
    private GridPane root;

    @FXML
    private Text redAmmo;

    @FXML
    private Text yellowAmmo;

    @FXML
    private Text blueAmmo;

    @FXML
    private ImageView currentPlayer;

    @FXML
    private Region run;

    @FXML
    private Region moveGrab;

    @FXML
    private Region shoot;

    @FXML
    private Region reload;

    @FXML
    private Region moveMoveGrab;

    @FXML
    private Region moveShoot;

    @FXML
    private Region moveReloadShoot;

    @FXML
    private Region runMove;

    @FXML
    private Region frenzyMoveMoveGrab;

    @FXML
    private Region moveMoveReloadShoot;

    @FXML
    private Region runGrab;

    @Override
    public void update(Event message) {
        message.handle(this);
    }

        @Override
        public void dispatch(GuiInitialize message) {
            root.getChildren().removeAll(frenzyMoveMoveGrab, moveMoveReloadShoot, runGrab, runMove, moveReloadShoot);
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
