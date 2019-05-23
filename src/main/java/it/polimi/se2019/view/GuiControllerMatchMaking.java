package it.polimi.se2019.view;

import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.Log;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GuiControllerMatchMaking implements Initializable, GuiController {
    @FXML
    private VBox usernames;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label timer;
    @FXML
    private Label players;

    private Stage currentStage;

    private int missingPlayers = 3;

    private Map<String, Label> fromUsernameToLabel = new HashMap<>();

    private SimpleStringProperty timerValue = new SimpleStringProperty(((Integer) Settings.MATCH_MAKING_TIMER).toString());

    private Dispatcher dispatcher = new Dispatcher();

    private class Dispatcher implements GuiDispatcher{
        //TODO
    }

    @Override
    public void update(Event message) {
        message.handle(dispatcher);
    }

    public void endMatchMaking(){
        ensureJavaFXThread(() -> {
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(GuiControllerMatchMaking.class.getClassLoader().getResource("fxml/table.fxml"));
           try {
               AnchorPane border = loader.load();
               Scene scene = new Scene(border);
               Screen screen = Screen.getPrimary();
               Stage stage = new Stage();
               Rectangle2D bounds = screen.getVisualBounds();

               stage.setX(bounds.getMinX());
               stage.setY(bounds.getMinY());
               stage.setWidth(bounds.getWidth());
               stage.setHeight(bounds.getHeight());;
               stage.setScene(scene);
               stage.setResizable(false);
               stage.show();
           }catch (IOException e){
               Log.severe(e.getMessage());
           }

        });
    }


    public void startTimer(){
        ensureJavaFXThread(() -> {
            currentStage.setTitle("Match starting");
            timer.setText(timerValue.get());
            timer.setFont(new Font(18));
            timer.textProperty().bind(timerValue);
        });
    }

    public void stopTimer(){
        ensureJavaFXThread(() ->{
            timer.setText("");
            currentStage.setTitle("Waiting for " + missingPlayers + " more players");
        });
    }

    public void timerTick(Integer timeToSet){
        ensureJavaFXThread(() -> timerValue.set(timeToSet.toString()));
    }

    public void addPlayer(String username){
        ensureJavaFXThread(() -> {
            missingPlayers--;
            currentStage = (Stage) anchorPane.getScene().getWindow();
            currentStage.setTitle("Waiting for " + missingPlayers + " more players");
            Label label = new Label(username);
            fromUsernameToLabel.put(username, label);
            label.setFont(new Font(24));
            label.setTextFill(players.getTextFill());
            label.setEffect(players.getEffect());
            usernames.getChildren().add(label);
        });
    }

    public void removePlayer(String username){
        ensureJavaFXThread(() -> {
            usernames.getChildren().remove(fromUsernameToLabel.get(username));
            missingPlayers++;
        });
    }


}
