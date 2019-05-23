package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class GuiControllerPowerUp implements GuiController {

    @FXML
    private ImageView powerupThird;
    @FXML
    private ImageView powerupSecond;
    @FXML
    private ImageView powerupFirst;

    private Dispatcher dispatcher = new Dispatcher();

    @Override
    public void update(Event message) {
        message.handle(dispatcher);
    }

    private class Dispatcher implements GuiDispatcher{
        //TODO
    }
}
