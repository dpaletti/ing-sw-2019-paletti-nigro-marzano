package it.polimi.se2019.view;

import it.polimi.se2019.network.Settings;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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
    private VBox timer;

    private Map<String, Label> fromUsernametoLabel = new HashMap<>();

    private SimpleStringProperty timerValue = new SimpleStringProperty(((Integer) Settings.MATCH_MAKING_TIMER).toString());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewGUI.setMatchMakingControllerGui(this);
        startMatchMaking();
    }

    private void startMatchMaking(){
        Label label = new Label("Countdown will start upon reaching 3 players");
        label.setFont(new Font(20));
        label.setWrapText(true);
        timer.getChildren().add(label);
    }

    public void startTimer(){
        Platform.runLater(() -> {
            timer.getChildren().clear();
            Label label = new Label("Time to game:");
            label.setFont(new Font(20));
            timer.getChildren().add(label);
            Label timeToGo = new Label();
            timeToGo.setFont(new Font(20));
            timeToGo.textProperty().bind(timerValue);
            timer.getChildren().add(timeToGo);
        });
    }

    public void stopTimer(){
        Platform.runLater(() ->{
            timer.getChildren().clear();
            startMatchMaking();
        });
    }

    public void timerTick(Integer timeToSet){
        Platform.runLater(() -> timerValue.set(timeToSet.toString()));
    }

    public void addPlayer(String username){
        Platform.runLater(() -> {
                Label label = new Label(username);
                fromUsernametoLabel.put(username, label);
                label.setFont(new Font(16));
                usernames.getChildren().add(label);
        });
    }

    public void removePlayer(String username){
        Platform.runLater(() -> usernames.getChildren().remove(fromUsernametoLabel.get(username)));
    }

}
