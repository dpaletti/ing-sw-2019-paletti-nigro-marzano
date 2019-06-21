package it.polimi.se2019.view;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.gui_events.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class GuiControllerMatchMaking extends GuiController implements Initializable{
    @FXML
    private Label timer;
    @FXML
    private TableView<String> playerTable;

    private Stage currentStage;

    private int missingPlayers = 3;

    private SimpleStringProperty timerValue;

        @Override
        public void dispatch(UiAddPlayer message) {
            missingPlayers--;
            timer.setText("waiting for " +  missingPlayers + "more players...");
            playerTable.getItems().add(message.getPlayer());
        }

        @Override
        public void dispatch(UiTimerStart message) {
            currentStage.setTitle("Match starting");
            timerValue = new SimpleStringProperty(((Integer) message.getDuration()).toString());
            timer.setText(timerValue.get());
            timer.setFont(new Font(18));
            timer.textProperty().bind(timerValue);
        }

        @Override
        public void dispatch(UiTimerStop message){
            timer.setText("");
            currentStage.setTitle("Waiting for " + missingPlayers + " more players");
        }

        @Override
        public void dispatch(UiTimerTick message) {
            int timeToSet = message.getTimeToGo() /1000;
            timerValue.set((((Integer)timeToSet).toString()));
        }

        @Override
        public void dispatch(UiRemovePlayer message) {
            playerTable.getItems().remove(message.getPlayer());
            missingPlayers++;
        }

        @Override
        public void dispatch(UiCloseMatchMaking message) {
            viewGUI.closeMatchMaking();
            try {
                currentStage.close();
                FXMLLoader tableLoader = new FXMLLoader(Paths.get("files/fxml/table.fxml").toUri().toURL());
                GridPane tableGrid = tableLoader.load();
                tableGrid.getChildren().addAll();
                Scene scene = new Scene(tableGrid);

                currentStage.setTitle("Adrenaline");
                currentStage.setScene(scene);
                currentStage.setResizable(false);
                currentStage.setMaximized(true);
                new Thread(currentStage::show).start();
            }catch (IOException e){
                Log.severe("Load error " + e.getMessage());
            }
        }
}
