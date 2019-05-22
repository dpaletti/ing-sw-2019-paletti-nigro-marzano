package it.polimi.se2019.view;

import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.Log;
import javafx.application.Platform;
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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MatchMakingControllerGui implements Initializable {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewGUI.setMatchMakingControllerGui(this);
    }


    private void ensureJavaFXThread(Runnable action){
        if(Platform.isFxApplicationThread())
            action.run();
        else
            Platform.runLater(action);

    }


    public void endMatchMaking(){
        ensureJavaFXThread(() -> {
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(MatchMakingControllerGui.class.getClassLoader().getResource("fxml/match.fxml"));
           try {
               AnchorPane anchor = loader.load();
               Scene scene = new Scene(anchor);
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
