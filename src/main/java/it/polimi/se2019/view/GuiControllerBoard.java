package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class GuiControllerBoard implements GuiController {

    @FXML
    private GridPane board;
    @FXML
    private ImageView boardLeft;
    @FXML
    private ImageView boardRight;
    @FXML
    private ImageView topRedSpawn;
    @FXML
    private ImageView middleRedSpawn;
    @FXML
    private ImageView bottomRedSpawn;
    @FXML
    private ImageView leftBlueSpawn;
    @FXML
    private ImageView middleBlueSpawn;
    @FXML
    private ImageView rightBlueSpawn;
    @FXML
    private ImageView topYellowSpawn;
    @FXML
    private ImageView middleYellowSpawn;
    @FXML
    private ImageView bottomYellowSpawn;

    private Dispatcher dispatcher = new Dispatcher();

    @Override
    public void update(Event message) {
        message.handle(dispatcher);
    }

    private class Dispatcher implements GuiDispatcher{
        //TODO
    }
}
