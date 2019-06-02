package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.gui_events.GuiRegistrationEvent;
import javafx.application.Platform;
import javafx.fxml.Initializable;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public abstract class GuiController implements Observer<Event>, Initializable, GuiDispatcher {
    protected ViewGUI viewGUI;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewGUI.staticRegister(this);
    }

    @Override
    public void dispatch(GuiRegistrationEvent message) {
        Log.fine("registering viewGui");
        this.viewGUI = message.getReference();
    }

    @Override
    public void update(Event message) {
        try {
            ensureJavaFXThread(() -> message.handle(this));
        } catch (UnsupportedOperationException e) {
            Log.fine("ignored " + message);
        }
    }

    protected void ensureJavaFXThread(Runnable action){
        if(Platform.isFxApplicationThread())
            action.run();
        else
            Platform.runLater(action);
    }

    protected String getUrl(Path path){
        try {
            return path.toUri().toURL().toString();
        }catch (MalformedURLException e){
            Log.severe("Could not find requested image: " + path.toString());
        }
        throw new IllegalStateException("Could not get Image from path");
    }

}
