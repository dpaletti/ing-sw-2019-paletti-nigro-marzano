package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class GuiControllerTable implements GuiController {

    @FXML
    private Pane rootPlayer;

    @FXML
    private Text redAmmo;

    @FXML
    private Text yellowAmmo;

    @FXML
    private Text blueAmmo;

    @FXML
    private GridPane mainPane;

    @FXML
    private ImageView currentPlayer;

    private Dispatcher dispatcher = new Dispatcher();

    @Override
    public void update(Event message) {
        message.handle(dispatcher);
    }

    private class Dispatcher implements GuiDispatcher{
        //TODO
    }
}
