package it.polimi.se2019.view;

import it.polimi.se2019.network.Settings;
import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.gui_events.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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

        public void dispatch(GuiAddPlayer message) {
            missingPlayers--;
            currentStage = (Stage) anchorPane.getScene().getWindow();
            currentStage.setTitle("Waiting for " + missingPlayers + " more players");
            Label label = new Label(message.getPlayer());
            fromUsernameToLabel.put(message.getPlayer(), label);
            label.setFont(new Font(24));
            label.setTextFill(players.getTextFill());
            label.setEffect(players.getEffect());
            usernames.getChildren().add(label);
        }

        @Override
        public void dispatch(GuiTimerStart message) {
            currentStage.setTitle("Match starting");
            timer.setText(timerValue.get());
            timer.setFont(new Font(18));
            timer.textProperty().bind(timerValue);
        }

        @Override
        public void dispatch(GuiTimerStop message){
            timer.setText("");
            currentStage.setTitle("Waiting for " + missingPlayers + " more players");
        }

        @Override
        public void dispatch(GuiTimerTick message) {
            int timeToSet = message.getTimeToGo() /1000;
            timerValue.set((((Integer)timeToSet).toString()));
        }

        @Override
        public void dispatch(GuiRemovePlayer message) {
            usernames.getChildren().remove(fromUsernameToLabel.get(message.getPlayer()));
            missingPlayers++;
        }

        @Override
        public void dispatch(GuiCloseMatchMaking message) {
            currentStage.close();
            FXMLLoader tableLoader = new FXMLLoader(MatchMakingGui.class.getClassLoader().getResource("fxml/table.fxml"));
            FXMLLoader boardLoader = new FXMLLoader(MatchMakingGui.class.getClassLoader().getResource("fxml/board.fxml"));
            FXMLLoader weaponLoader = new FXMLLoader(MatchMakingGui.class.getClassLoader().getResource("fxml/weapon.fxml"));
            FXMLLoader powerupLoader = new FXMLLoader(MatchMakingGui.class.getClassLoader().getResource("fxml/powerup.fxml"));
            try {
                GridPane tableGrid = tableLoader.load();
                GridPane boardGrid = boardLoader.load();
                GridPane weaponGrid = weaponLoader.load();
                GridPane powerupGrid = powerupLoader.load();
                tableGrid.getChildren().addAll(boardGrid, weaponGrid, powerupGrid);
                Scene scene = new Scene(tableGrid);

                currentStage.setTitle("Adrenaline");
                currentStage.setScene(scene);
                currentStage.setResizable(false);
                currentStage.setMaximized(true);

                currentStage.show();
            }catch (IOException e){
               Log.severe("Could not correctly load FXML files " + e.getMessage());
            }
        }

    @Override
    public void update(Event message) {
        try {

            ensureJavaFXThread(() -> message.handle(this));
        }catch (UnsupportedOperationException e){
            Log.fine("ignored " + message);
        }
    }
}
