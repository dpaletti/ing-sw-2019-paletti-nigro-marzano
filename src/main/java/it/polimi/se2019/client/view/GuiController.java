package it.polimi.se2019.client.view;

import it.polimi.se2019.client.view.ui_events.UiTimerTick;
import it.polimi.se2019.commons.utility.Event;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.utility.Observer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for javaFX fxml files contained in files/fxml, every implementation manages one or more fxml all linked
 * to a specific section of the GUI
 */
public abstract class GuiController implements Observer<Event>, Initializable, UiDispatcher {

    /**
     * Automatic registration to ViewGUI for javaFX controllers
     * @param location  ignored
     * @param resources ignored
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewGUI.getInstance().registerController(this);
    }

    /**
     * update methods that guarantees to always dispatch events in a javaFX thread
     * @param message dispatched message
     */
    @Override
    public void update(Event message) {
        ensureJavaFXThread(() -> {
            try {
                message.handle(this);
            }catch (UnsupportedOperationException e){
                if (!(message instanceof UiTimerTick)){
                    Log.fine("ignored " +
                            message);
                }

            }
        });
    }


    protected void ensureJavaFXThread(Runnable action){
        if(Platform.isFxApplicationThread())
            action.run();
        else
            Platform.runLater(action);
    }

    protected EventHandler<MouseEvent> clickable(Scene scene) {
        return (MouseEvent event) -> {
            if(scene != null)
                scene.setCursor(Cursor.HAND);
        };
    }


    protected EventHandler<MouseEvent> notClickable(Scene scene) {
        return (MouseEvent event) -> {
            if(scene != null)
                scene.setCursor(Cursor.DEFAULT);
        };
    }

    protected void clickableNoHandler(Scene scene){
        scene.setCursor(Cursor.HAND);
    }

    protected void notClickableNoHandler(Scene scene){
        scene.setCursor(Cursor.DEFAULT);
    }

    public void removeHandlers(ImageView image){
        image.setOnMouseClicked(null);
        image.setOnMouseExited(null);
        image.setOnMouseExited(null);
    }


}
