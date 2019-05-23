package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GuiControllerBoard implements GuiController {

    @FXML
    private GridPane board;

    @FXML
    private ImageView boardLeft;

    @FXML
    private ImageView boardRight;

    @FXML
    private Pane leftRoot;

    @FXML
    private Pane rightRoot;

    private Dispatcher dispatcher = new Dispatcher();

    @Override
    public void update(Event message) {
        message.handle(dispatcher);
    }

    private class Dispatcher implements GuiDispatcher{
        //TODO
    }
}
