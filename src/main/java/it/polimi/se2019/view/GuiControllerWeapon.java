package it.polimi.se2019.view;

import it.polimi.se2019.view.ui_events.UiTurnEnd;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GuiControllerWeapon extends GuiController {

    @FXML
    private ImageView weaponLeft;

    @FXML
    private ImageView effectBaseLeft;

    @FXML
    private ImageView effectAlternateLeft;

    @FXML
    private ImageView effectOptional1Left; //optional 1 is on the left

    @FXML
    private ImageView effectOptional2Left;


    @FXML
    private ImageView weaponMiddle;

    @FXML
    private ImageView effectBaseMiddle;

    @FXML
    private ImageView effectAlternateMiddle;

    @FXML
    private ImageView effectOptional1Middle;

    @FXML
    private ImageView effectOptional2Middle;


    @FXML
    private ImageView weaponRight;

    @FXML
    private ImageView effectBaseRight;

    @FXML
    private ImageView effectAlternateRight;

    @FXML
    private ImageView effectOptional1Right;

    @FXML
    private ImageView effectOptional2Right;

    private List<ImageView> active;

    @Override
    public void dispatch(UiTurnEnd message) {
        for(ImageView i: active)
            i.setDisable(false);
        active = new ArrayList<>();
    }

}
