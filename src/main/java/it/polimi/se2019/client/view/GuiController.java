package it.polimi.se2019.client.view;

import it.polimi.se2019.client.view.ui_events.UiTimerTick;
import it.polimi.se2019.commons.utility.Event;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.utility.Observer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public abstract class GuiController implements Observer<Event>, Initializable, UiDispatcher {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewGUI.getInstance().registerController(this);
    }

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

    public Node getGridNode(GridPane gridPane, int x, int y){
        for(Node n: gridPane.getChildren()){
            if(GridPane.getColumnIndex(n) == x && GridPane.getRowIndex(n) == y)
                return n;
        }
        throw new NullPointerException("There is no node at specific coordinate");
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


    protected String getUrl(Path path){
        try {
            return path.toUri().toURL().toString();
        }catch (MalformedURLException e){
            Log.severe("Could not find requested image: " + path.toString());
        }
        throw new IllegalStateException("Could not get Image from path");
    }
    

}
