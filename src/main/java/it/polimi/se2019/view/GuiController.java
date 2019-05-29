package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.Observer;
import javafx.application.Platform;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public interface GuiController extends Observer<Event>, Initializable, GuiDispatcher {
    
    @Override
    default void initialize(URL location, ResourceBundle resources) {
        ViewGUI.staticRegister(this);
    }

    default void ensureJavaFXThread(Runnable action){
        if(Platform.isFxApplicationThread())
            action.run();
        else
            Platform.runLater(action);
    }
}
