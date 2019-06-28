package it.polimi.se2019.view;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.ui_events.*;
import it.polimi.se2019.view.vc_events.ChosenComboEvent;
import it.polimi.se2019.view.vc_events.DiscardedPowerUpEvent;
import it.polimi.se2019.view.vc_events.DiscardedWeaponEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
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
    private ImageView currentPlayer;

    @FXML
    private GridPane root;

    @FXML
    private TableView<TableModel> leaderboard;

    @FXML
    private ImageView showedCard;

    @FXML
    private Label turnTimer;

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
    private int skulls = 5;
    private boolean frenzy = false;
    private static final String MAP_DIR = "files/assets/board/map/";
    private static final String HIGHLIGHTED = "_highlighted";

    private SimpleStringProperty directionsText = new SimpleStringProperty();
    private List<String> availableMoves = new ArrayList<>();

    private List<String> lockedPlayers = null;
    private List<String> highlightedLockedPlayers = null;

    private String headPlayer;

    private String lockedCard = null;

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


    //----------------------------------------------------------Timer-------------------------------------------------//
    @Override
    public void dispatch(UiTimerStart message) {
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
    //-----------------------------------------------------------------------------------------------------------------//


    //----------------------------------------------Match Making--------------------------------------------------------//

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


    @Override
    public void dispatch(UiMapConfigEvent message) {
        try {
            leave.setOnMouseClicked((MouseEvent event) -> System.exit(0));
            leave.setOnMouseEntered(clickable(scene));
            leave.setOnMouseExited(notClickable(scene));

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


    //-----------------------------------------------------------------------------------------------------------------//


    //---------------------------------------------------------Setup---------------------------------------------------//

    @Override
    public synchronized void dispatch(UiCloseMatchMaking message) {

        initializeSendButton();
        initializeMapSelection();
        initializeCheckBox();
        initializeSkullSelection();
        titleText.set("Choose preferred options");
    }

    private void initializeSendButton(){
        endTurn.setDisable(false);
        endTurn.setOnMouseClicked((MouseEvent event) ->
        {
            ViewGUI.getInstance().gameSetup(skulls, frenzy, chosenMap);
            endTurn.setDisable(true);
            endTurn.setOnMouseClicked(null);
        });

        endTurn.setOnMouseEntered(clickable(scene));
        endTurn.setOnMouseExited(notClickable(scene));
    }

    private void initializeMapSelection() {

        for(String map: mapsAdded){
            ImageView holder = (ImageView) scene.lookup("#" + map);
            holder.setDisable(false);
            holder.setOnMouseEntered((MouseEvent event) -> {
                highlight(event, holder.getId());
                clickableNoHandler(scene);
            });
            holder.setOnMouseExited((MouseEvent event) -> {
                if(!holder.isDisabled())
                    darken(event, holder.getId());
                notClickableNoHandler(scene);
            });
            holder.setOnMouseClicked((MouseEvent event) -> {
                clickOnMap((ImageView) event.getSource());
            });
        }
        ImageView defaultMap = (ImageView) scene.lookup("#"+mapsAdded.get(0));
        clickOnMap(defaultMap);
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


    private void initializeCheckBox () {
        frenzyBox.setDisable(false);
        frenzyBox.setOnAction((ActionEvent event) -> {
            if (((CheckBox) event.getSource()).isSelected()) {
                frenzy = true;
                return;
            }
            frenzy = false;
        });
    }

    private void initializeSkullSelection () {
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


    @Override
    public void dispatch(UiCloseSetup message) {
        try {
            root.getChildren().remove(choiceGrid.getParent());

            FXMLLoader loader = new FXMLLoader(Paths.get("files/fxml/board.fxml").toUri().toURL());
            GridPane board = loader.load();
            loader = new FXMLLoader(Paths.get("files/fxml/weapon.fxml").toUri().toURL());
            root.add(board, 3, 2);
            root.getChildren().add(loader.load());
            loader = new FXMLLoader(Paths.get("files/fxml/powerup.fxml").toUri().toURL());
            root.getChildren().add(loader.load());

            buttonSetup();
            directionSetup();
        }catch (MalformedURLException e){
            Log.severe("Could not get board.fxml");
        }catch (IOException e){
            Log.severe("Could not load AnchorPane in board");
        }
    }

    @Override
    public void dispatch(UiSetPlayerBoard message) {
        try {
            currentPlayer.setImage(new Image(Paths.get("files/assets/player/player_" + message.getColour().toLowerCase() + ".png").toUri().toURL().toString()));
            headPlayer = message.getColour().toLowerCase();
        }catch (MalformedURLException e){
            Log.severe("Could not get " + message.getColour() + "player board");
        }
    }

    private void buttonSetup(){
        endTurn.setText("End turn");
        endTurn.setOnAction((ActionEvent event) -> ViewGUI.getInstance().endTurn());
    }

    private void directionSetup(){
        ((Label)scene.lookup("#directions")).textProperty().bind(directionsText);
        directionsText.set("Please wait for your turn");
    }

    //----------------------------------------------------------------------------------------------------------------//

    //--------------------------------------------------------Turn----------------------------------------------------//

    @Override
    public void dispatch(UiSpawn message) {
        turnTimer.textProperty().bind(timerText);
        directionsText.set("Please choose a powerup to discard");
    }

    public void dispatch(UiStartTurn message){
        endTurn.setDisable(false);
    }

    @Override
    public void dispatch(UiAvailableMove message) {
        try {
            directionsText.set("Please choose one of the highlighted combos");
            ImageView available;
            available =((ImageView) scene.lookup("#" + message.getCombo()));
            availableMoves.add(message.getCombo());
            available.setDisable(false);
            available.setImage(
                    new Image(Paths.get("files/assets/rectangle_" + ViewGUI.getInstance().getColour().toLowerCase()).toUri().toURL().toString() + ".png"));
            available.setOnMouseEntered(clickable(scene));
            available.setOnMouseExited(notClickable(scene));
            available.setOnMouseClicked((MouseEvent event) -> {
                ViewGUI.getInstance().send(new ChosenComboEvent(ViewGUI.getInstance().getUsername(), message.getCombo()));
                for(String s: availableMoves)
                    scene.lookup("#" + s).setDisable(true);
            });
        }catch (MalformedURLException e){
            Log.severe("Cannot retrieve rectangle for overlay");
        }
    }

    @Override
    public void dispatch(UiContextSwitch message) {
        try {
            currentPlayer.setImage(new Image(Paths.get("files/assets/player/player_" + message.getNewContext().toLowerCase() + ".png").toUri().toURL().toString()));
            disableAllCombos();
            String oldDirections = directionsText.get();
            directionsText.set("Looking at player " + message.getNewContext().toLowerCase());

            endTurn.setText("Back");
            endTurn.setOnMouseClicked((MouseEvent event) ->{
                try {
                    currentPlayer.setImage(new Image(Paths.get("files/assets/player/player_" + headPlayer + ".png").toUri().toURL().toString()));
                    enableAllCombos();
                    buttonSetup();
                    directionsText.set(oldDirections);

                }catch (MalformedURLException e){
                    Log.severe("Could not retrieve old Image for player board");
                }

            });
        }catch (MalformedURLException e){
            Log.severe("Wrong URL for context switching");
        }
    }

    private void disableAllCombos(){
        List<Node> nodes = ((Pane)scene.lookup("#playerCombo")).getChildren();
        nodes.addAll(((Pane) scene.lookup("#playerComboDamaged")).getChildren());
        for(Node n: nodes)
            n.setDisable(true);
    }

    private void enableAllCombos(){
        List<Node> nodes = ((Pane)scene.lookup("#playerCombo")).getChildren();
        nodes.addAll(((Pane) scene.lookup("#playerComboDamaged")).getChildren());
        for(Node n: nodes)
            n.setDisable(false);

    }

    @Override
    public void dispatch(UiShowFourth message) {
        try {
            if (lockedCard != null)
                throw new IllegalStateException("Trying to show fourth card when still showing previous one");
            lockedCard = message.getCard();
            showedCard.setImage(new Image(Paths.get("files/assets/cards/" + message.getCard() + ".png").toUri().toURL().toString()));
            directionsText.set("Please choose card to discard");
            showedCard.setOnMouseClicked((MouseEvent event) ->{
                if(message.isWeapon())
                    ViewGUI.getInstance().send(new DiscardedWeaponEvent(
                            ViewGUI.getInstance().getUsername(),lockedCard));
                else
                    ViewGUI.getInstance().send(new DiscardedPowerUpEvent(
                            ViewGUI.getInstance().getUsername(), lockedCard));
                showedCard.setOnMouseClicked(null);
                showedCard.setImage(null);
                lockedCard = null;
        });
        }catch (MalformedURLException e){
            Log.severe(e.getMessage());
        }

    }

    @Override
    public void dispatch(UiShowWeapon message) {
        try {
            showedCard.setImage(new Image(Paths.get("files/assets/cards/" + message.getWeapon() + ".png").toUri().toURL().toString()));
        }catch (MalformedURLException e){
            Log.severe("Could not show card: " + e.getMessage());
        }
    }

    @Override
    public void dispatch(UiHideWeapon message) {
        try {
            if (lockedCard != null) {
                showedCard.setImage(new Image(Paths.get("files/assets/cards/" + lockedCard + ".png").toUri().toURL().toString()));
            }
            else {
                showedCard.setImage(null);
            }
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve locked card for showing");
        }
    }

    @Override
    public void dispatch(UiHidePlayers message) {
        for (int i = 1; i < 6; i++) {
            ((ImageView) scene.lookup("#figure" + i)).setImage(null);
        }
        if (lockedPlayers != null)
            dispatch(new UiShowPlayers(lockedPlayers, highlightedLockedPlayers));
    }

    @Override
    public void dispatch(UiLockPlayers message) {
        lockedPlayers = new ArrayList<>();
        highlightedLockedPlayers = new ArrayList<>();
        lockedPlayers.addAll(message.getFiguresToLock());
        highlightedLockedPlayers.addAll(message.getHighlighted());
    }

    @Override
    public void dispatch(UiUnlockPlayers message) {
        lockedPlayers = null;
        highlightedLockedPlayers = null;
        dispatch(new UiHidePlayers(null));
    }

    @Override
    public void dispatch(UiShowPlayers message) {
        try {
            int i = 0;
            for (String f : message.getFiguresToShow()) {
                i++;
                if(!message.getHighlightedFigures().contains(f))
                    ((ImageView) scene.lookup("#figure" + i)).setImage(new Image
                            (Paths.get("files/assets/player/figure_" + f.toLowerCase() + ".png").toUri().toURL().toString()));
                else
                    ((ImageView) scene.lookup("#figure" + i)).setImage(new Image
                            (Paths.get("files/assets/player/figure_" + f.toLowerCase() + "_targeted.png").toUri().toURL().toString()));

            }
        }catch (MalformedURLException e){
            Log.severe("Wrong URL in reshowing locked selection");
        }
    }

    @Override
    public void dispatch(UiTurnEnd message) {
        directionsText.set("Please wait for your turn");

    }

    //----------------------------------------------------------------------------------------------------------------//












}







