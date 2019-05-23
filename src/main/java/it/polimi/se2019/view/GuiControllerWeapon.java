package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class GuiControllerWeapon implements GuiController {

    @FXML
    private ImageView weaponThird;
    @FXML
    private ImageView weaponSecond;
    @FXML
    private ImageView weaponFirst;

    private Dispatcher dispatcher = new Dispatcher();

    @Override
    public void update(Event message) {
        message.handle(dispatcher);
    }

    private class Dispatcher implements GuiDispatcher{
        //TODO
    }
}
