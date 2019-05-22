package it.polimi.se2019.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MatchControllerGui implements Initializable {
    @FXML
    ImageView table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewGUI.setMatchControllerGui(this);
    }

    public void test(){}
}
