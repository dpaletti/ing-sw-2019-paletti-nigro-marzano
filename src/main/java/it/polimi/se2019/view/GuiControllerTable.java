package it.polimi.se2019.view;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.gui_events.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GuiControllerTable extends GuiController {

    @FXML
    private GridPane hp;

    @FXML
    private GridPane mark;

    @FXML
    private GridPane skull;

    @FXML
    private Button endTurn;

    @FXML
    private Button leave;

    @FXML
    private ImageView moveAndGrab;

    @FXML
    private Label redAmmo;

    @FXML
    private Label yellowAmmo;

    @FXML
    private Label blueAmmo;

    @FXML
    private ImageView currentPlayer;

    @FXML
    private ImageView run;

    @FXML
    private ImageView shoot;

    @FXML
    private ImageView reload;

    @FXML
    private ImageView moveMoveGrab;

    @FXML
    private ImageView moveShoot;

    @FXML
    private GridPane root;

    @FXML
    private TableView<TableModel> leaderboard;

    //----- match_setup attributes ---//
    private GridPane choiceGrid;
    private CheckBox frenzyBox;
    private Label title;
    private SimpleStringProperty titleText = new SimpleStringProperty();
    private int missingPlayers = 3;
    private Label timer;
    private SimpleStringProperty timerText = new SimpleStringProperty();
    //-------------------------------//

    private List<TableModel> rowTrack = new ArrayList<>();
    private List<String> mapsAdded = new ArrayList<>();

    private Scene scene;

    private String chosenMap;
    private int skulls;
    private boolean frenzy = false;
    private static final String MAP_DIR = "files/assets/board/map/";
    private static final String HIGHLIGHTED = "_highlighted";

    public static class TableModel {
        StringProperty username;
        StringProperty score;

        public TableModel(String username, String score) {
            this.username = new SimpleStringProperty(username);
            this.score = new SimpleStringProperty(score);
        }

        public String getUsername() {
            return username.get();
        }

        public String getScore() {
            return score.get();
        }

        public StringProperty usernameProperty() {
            return username;
        }

        public StringProperty scoreProperty() {
            return score;
        }

        public void setUsername(String greetings) {
            this.username.set(greetings);
        }

        public void setScore(String score) {
            this.score.set(score);
        }
    }


    @Override
    public void dispatch(UiMapConfigEvent message) {
        try {
            leave.setOnMouseClicked((MouseEvent event) ->{
                System.exit(0);
            });
            leave.setOnMouseEntered((MouseEvent event) -> {
                clickable();
            });
            leave.setOnMouseExited((MouseEvent event) -> {
                notClickable();
            });
            disableSetupInteraction();
            List<Integer> mapLayout = new ArrayList<>();
            mapLayout.add(1);
            mapLayout.add(0);
            mapLayout.add(2);

            int column;
            int columnCounter = 0;
            int row = 0;
            for (String conf : message.getConfigs()) {
                column = mapLayout.get(columnCounter);
                createMapImage(conf, (ImageView) scene.lookup("#map" + row + column));
                columnCounter++;
                if (columnCounter >= mapLayout.size()) {
                    columnCounter = 0;
                    row++;
                }
            }
        } catch (MalformedURLException e) {
            Log.severe("Could not create map image");
        }

    }

    public void disableSetupInteraction() {
        scene = root.getScene();
        choiceGrid = (GridPane) scene.lookup("#choiceGrid");
        frenzyBox = (CheckBox) scene.lookup("#frenzy");
        title = (Label) scene.lookup("#title");
        timer = (Label) scene.lookup("#timer");
        endTurn.setDisable(true);
        endTurn.setText("Send");
        frenzyBox.setDisable(true);
        ToggleGroup t = new ToggleGroup();
        RadioButton r;
        for (int i = 5; i <= 8; i++) {
            r = ((RadioButton) scene.lookup("#skulls" + i));
            if(i == 5)
                r.setSelected(true);
            r.setToggleGroup(t);
            r.setDisable(true);
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++)
                scene.lookup("#map" + i + j).setDisable(true);
        }

        timerText.set("");
        timer.textProperty().bind(timerText);

        titleText = new SimpleStringProperty("Waiting for " + missingPlayers + " more to join match");
        title.textProperty().bindBidirectional(titleText);

        TableColumn username = new TableColumn<>("Username");
        TableColumn score = new TableColumn<>("Score");

        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));

        leaderboard.getColumns().addAll(username, score);
    }


    private void createMapImage(String conf, ImageView holder) throws MalformedURLException {
        String path = MAP_DIR + "map_" + conf.toLowerCase() + ".png";
        Image image = new Image(Paths.get(path).toUri().toURL().toString());
        holder.setImage(image);
        holder.setId(conf.toLowerCase());
        mapsAdded.add(holder.getId());

    }

    public void highlight(MouseEvent event, String conf) {
        try {
            String path = MAP_DIR + "map_" + conf.toLowerCase() + HIGHLIGHTED + ".png";
            ((ImageView) event.getSource()).setImage(new Image(Paths.get(path).toUri().toURL().toString()));
        } catch (MalformedURLException e) {
            Log.severe("Could not find image");
        }

    }

    public void highlight(ImageView source, String conf) {
        try {
            String path = MAP_DIR + "map_" + conf.toLowerCase() + HIGHLIGHTED + ".png";
            source.setImage(new Image(Paths.get(path).toUri().toURL().toString()));
        } catch (MalformedURLException e) {
            Log.severe("Could not find image");
        }

    }

    public void darken(MouseEvent event, String conf) {
        String path = MAP_DIR + "map_" + conf.toLowerCase() + ".png";
        try {
            ((ImageView) event.getSource()).setImage(new Image(Paths.get(path).toUri().toURL().toString()));
        } catch (MalformedURLException e) {
            Log.severe("Could not find image");
        }
    }


    public void darken(ImageView source, String conf) {
        String path = MAP_DIR + "map_" + conf.toLowerCase() + ".png";
        try {
            source.setImage(new Image(Paths.get(path).toUri().toURL().toString()));
        } catch (MalformedURLException e) {
            Log.severe("Could not find image");
        }
    }

    @Override
    public void dispatch(UiAddPlayer message) {
        TableModel t = new TableModel(message.getPlayer(), "0");
        rowTrack.add(t);
        leaderboard.getItems().add(t);
        int oldMissing = missingPlayers;
        missingPlayers--;
        titleText.set(titleText.get().replace(((Integer) oldMissing).toString(), ((Integer) missingPlayers).toString()));
    }

    @Override
    public void dispatch(UiRemovePlayer message) {
        for (TableModel t : rowTrack) {
            if (t.getUsername().equals(message.getPlayer())) {
                rowTrack.remove(t);
                leaderboard.getItems().remove(t);
                return;
            }
        }
    }

    @FXML
    private void clickable() {
        currentPlayer.getScene().setCursor(Cursor.HAND);
    }

    @FXML
    private void notClickable() {
        currentPlayer.getScene().setCursor(Cursor.DEFAULT);
    }


    @Override
    public void dispatch(UiTimerStart message) {
        titleText.set("Waiting for countdown");
        int timeToSet = message.getDuration() / 1000;
        timerText.set(((Integer) timeToSet).toString());
    }

    @Override
    public void dispatch(UiTimerTick message) {
        int timeToSet = message.getTimeToGo() / 1000;
        timerText.set((((Integer) timeToSet).toString()));
    }

    @Override
    public void dispatch(UiTimerStop message) {
        timerText.set("");
    }


    @Override
    public synchronized void dispatch(UiCloseMatchMaking message) {

        titleText.set("Choose preferred options and press send");
        initializeSendButton();
        initializeMapSelection();
        initializeCheckBox();
        initializeSkullSelection();
    }


    public void initializeSendButton(){
        endTurn.setDisable(false);
        endTurn.setOnMouseClicked((MouseEvent event) ->
        {
            ViewGUI.getInstance().gameSetup(skulls, frenzy, chosenMap);
        });
    }

    public void clickOnMap(ImageView map){
        if(chosenMap != null) {
            ImageView oldMap = (ImageView) scene.lookup("#" + chosenMap);
            darken(oldMap, oldMap.getId());
            oldMap.setDisable(false);
        }
        map.setDisable(true);
        highlight(map, map.getId());
        chosenMap = map.getId();
    }

    public void initializeMapSelection() {

        for(String map: mapsAdded){
            ImageView holder = (ImageView) scene.lookup("#" + map);
            holder.setDisable(false);
            holder.setOnMouseEntered((MouseEvent event) -> {
                highlight(event, holder.getId());
                clickable();
            });
            holder.setOnMouseExited((MouseEvent event) -> {
                if(!holder.isDisabled())
                    darken(event, holder.getId());
                notClickable();
            });
            holder.setOnMouseClicked((MouseEvent event) -> {
                clickOnMap((ImageView) event.getSource());
            });
        }
        ImageView defaultMap = (ImageView) scene.lookup("#"+mapsAdded.get(0));
        clickOnMap(defaultMap);
    }

        public void initializeCheckBox () {
            frenzyBox.setDisable(false);
            frenzyBox.setOnAction((ActionEvent event) -> {
                if (((CheckBox) event.getSource()).isSelected()) {
                    frenzy = true;
                    return;
                }
                frenzy = false;
            });
        }

        public void initializeSkullSelection () {
            RadioButton r;
            for (int i = 5; i <= 8; i++) {
                r = ((RadioButton) scene.lookup("#skulls" + i));
                r.setDisable(false);
                r.setOnAction(
                        (ActionEvent event) -> {
                            if (((RadioButton) event.getSource()).getId().equals("skulls5"))
                                skulls = 5;
                            if (((RadioButton) event.getSource()).getId().equals("skulls6"))
                                skulls = 6;
                            if (((RadioButton) event.getSource()).getId().equals("skulls7"))
                                skulls = 7;
                            if (((RadioButton) event.getSource()).getId().equals("skulls7"))
                                skulls = 8;
                        }
                );
            }

        }
    }







