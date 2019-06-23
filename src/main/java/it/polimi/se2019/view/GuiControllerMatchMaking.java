package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.gui_events.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class GuiControllerMatchMaking extends GuiController implements Initializable{
    @FXML
    private Label timer;
    @FXML
    private TableView<TableModel> playerTable;
    @FXML
    private TableColumn<TableModel, String> usernameColumn;
    @FXML
    private ObservableList<TableModel> usernames = FXCollections.observableArrayList();

    private Stage currentStage;

    private int missingPlayers = 3;

    private SimpleStringProperty timerValue = new SimpleStringProperty();



    public static class TableModel{
        StringProperty username;

        public TableModel(String username) {
            this.username = new SimpleStringProperty(username);
        }

        public String getUsername() {
            return username.get();
        }

        public StringProperty usernameProperty() {
            return username;
        }

        public void setUsername(String greetings) {
            this.username.set(greetings);
        }

    }

    @Override
    public void update(Event message) {
        ensureJavaFXThread(() -> {
            try {
                message.handle(this);
            }catch (UnsupportedOperationException e){
                Log.fine("MatchMaking Controller: ignored " +
                        message);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        playerTable.setItems(usernames);
    }

    @Override
        public void dispatch(UiAddPlayer message) {
            if(usernames.size() == 0)
                currentStage = (Stage) playerTable.getScene().getWindow();

            missingPlayers--;
            usernames.add(new TableModel(message.getPlayer()));
            timer.setText("waiting for " +  missingPlayers + " more players...");
        }

        @Override
        public void dispatch(UiTimerStart message) {
            currentStage.setTitle("Match starting");
            timerValue = new SimpleStringProperty(((Integer) message.getDuration()).toString());
            timer.textProperty().bind(timerValue);
        }

        @Override
        public void dispatch(UiTimerStop message){
            currentStage.setTitle("Waiting for " + missingPlayers + " more players");
        }

        @Override
        public void dispatch(UiTimerTick message) {
            int timeToSet = message.getTimeToGo() /1000;
            timerValue.set((((Integer)timeToSet).toString()));
        }

        @Override
        public void dispatch(UiRemovePlayer message) {
            playerTable.getItems().remove(new TableModel(message.getPlayer()));
            missingPlayers++;
        }

        @Override
        public void dispatch(UiCloseMatchMaking message) {
            try {
                new FXMLLoader(Paths.get("files/fxml/table.fxml").toUri().toURL()).load();
            }catch (IOException e){
                Log.severe("Could not open table.fxmk");
            }
        }
}
