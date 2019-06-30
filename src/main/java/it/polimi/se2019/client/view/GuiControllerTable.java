package it.polimi.se2019.client.view;

import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.client.view.ui_events.*;
import it.polimi.se2019.commons.vc_events.*;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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

    @FXML
    private Label redAmmo;

    @FXML
    private Label blueAmmo;

    @FXML
    private Label yellowAmmo;


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
    private boolean isLockedWeapon = false;

    private String oldDirections = null;

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
        int oldMissing;
        for (TableModel t : rowTrack) {
            if (t.getUsername().equals(message.getPlayer())) {
                rowTrack.remove(t);
                leaderboard.getItems().remove(t);
                oldMissing = missingPlayers;
                missingPlayers++;
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
        endTurn.setDisable(false);
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

    @Override
    public void dispatch(UiRespawnEvent message) {
        dispatch(new UiSpawn());
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
            oldDirections = directionsText.get();
            directionsText.set("Looking at player " + message.getNewContext().toLowerCase());

            endTurn.setText("Back");
            endTurn.setDisable(false);
            endTurn.setOnMouseClicked((MouseEvent event) -> {
                ViewGUI.getInstance().setCurrentlyShownFigure(headPlayer);
                ViewGUI.getInstance().send(new UiContextSwitchEnd());
            });

            resetHp();

            for(String drop: ViewGUI.getInstance().getHp())
                dispatch(new UiAddDamage(drop, ViewGUI.getInstance().getHp().indexOf(drop), false));

            for(String drop: ViewGUI.getInstance().getMarks())
                dispatch(new UiAddDamage(drop, ViewGUI.getInstance().getMarks().indexOf(drop), true));

            dispatch(new UiAmmoUpdate(ViewGUI.getInstance().getAmmos()));

        }catch (MalformedURLException e){
            Log.severe("Wrong URL for context switching");
        }
    }

    @Override
    public void dispatch(UiContextSwitchEnd message) {
            try {
                if(frenzy)
                    currentPlayer.setImage(new Image(Paths.get("files/assets/player/player_" + headPlayer + "_back.png").toUri().toURL().toString()));
                else
                    currentPlayer.setImage(new Image(Paths.get("files/assets/player/player_" + headPlayer + ".png").toUri().toURL().toString()));

                enableAllCombos();
                buttonSetup();
                directionsText.set(oldDirections);
                for(String drop: ViewGUI.getInstance().getHp())
                    dispatch(new UiAddDamage(drop, ViewGUI.getInstance().getHp().indexOf(drop), false));

                for(String drop: ViewGUI.getInstance().getMarks())
                    dispatch(new UiAddDamage(drop, ViewGUI.getInstance().getMarks().indexOf(drop), true));

                dispatch(new UiAmmoUpdate(ViewGUI.getInstance().getAmmos()));
            } catch (MalformedURLException e) {
                Log.severe("Could not retrieve old Image for player board");
            }
    }

    private void resetHp(){
        Pane pane;
        for (int i = 0; i < 12; i++){
            pane = (Pane) scene.lookup("#" + i);
            if(pane != null)
                ((Pane)pane.getParent()).getChildren().remove(pane);
        }
    }

    private void resetMark(){
        List<Integer> position = new ArrayList<>();
        for (int j = 0; j<2; j++){
            for (int i=0; i < 10; i++){
                position.add(i + 9*10);
            }
        }
        dispatch(new UiRemoveMarks(position));
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
            if(message.isWeapon())
                isLockedWeapon = true;
            lockedCard = message.getCard();
            showedCard.setImage(new Image(Paths.get("files/assets/cards/" + message.getCard() + ".png").toUri().toURL().toString()));
            directionsText.set("Please choose card to discard");
            showedCard.setOnMouseClicked((MouseEvent event) ->{
                if(message.isWeapon())
                    ViewGUI.getInstance().send(new DiscardedWeaponEvent(
                            ViewGUI.getInstance().getUsername(),lockedCard));
                else {
                    if(!ViewGUI.getInstance().isRespawning())
                        ViewGUI.getInstance().send(new DiscardedPowerUpEvent(
                                ViewGUI.getInstance().getUsername(), lockedCard));
                    else {
                        ViewGUI.getInstance().setRespawning(false);
                        ViewGUI.getInstance().send(new SpawnEvent(ViewGUI.getInstance().getUsername(), lockedCard));
                    }
                }
                showedCard.setOnMouseClicked(null);
                showedCard.setImage(null);
                lockedCard = null;
        });
        }catch (MalformedURLException e){
            Log.severe(e.getMessage());
        }

    }


    @Override
    public void dispatch(UiShowFourthEnd message) {
        if(isLockedWeapon)
            ViewGUI.getInstance().send(new UiPutWeapon(lockedCard));
        else
            ViewGUI.getInstance().send(new UiPutPowerUp(lockedCard));
        lockedCard = null;
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
    public void dispatch(UiSkip message) {
        directionsText.set("Please choose a target or skip");
        endTurn.setText("Skip");
        endTurn.setOnAction((ActionEvent event) -> {
            ViewGUI.getInstance().send(new UiPartialSkippedEvent());
            ViewGUI.getInstance().send(new VCPartialEffectEvent(ViewGUI.getInstance().getUsername()));
        });

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
        ImageView holder;
        try {
            int i = 0;
            for (String f : message.getFiguresToShow()) {
                i++;
                if(!message.getHighlightedFigures().contains(f)) {
                    holder = ((ImageView) scene.lookup("#figure" + i));
                    holder.setImage(new Image
                            (Paths.get("files/assets/player/figure_" + f.toLowerCase() + ".png").toUri().toURL().toString()));
                }
                else{
                    holder = ((ImageView) scene.lookup("#figure" + i));
                    holder.setImage(new Image
                            (Paths.get("files/assets/player/figure_" + f.toLowerCase() + "_targeted.png").toUri().toURL().toString()));
                }
                holder.setOnMouseEntered(clickable(scene));
                holder.setOnMouseExited(clickable(scene));
                holder.setOnMouseClicked((MouseEvent event) -> dispatch(new UiContextSwitch(f.toLowerCase())));

            }
        }catch (MalformedURLException e){
            Log.severe("Wrong URL in reshowing locked selection");
        }
    }

    @Override
    public void dispatch(UiAddDamage message) {
        FXMLLoader loader;
        int row;
        int column;
        try {
            if (!message.isMark())
                 loader = new FXMLLoader(Paths.get("files/fxml/hp.fxml").toUri().toURL());
            else
                loader = new FXMLLoader(Paths.get("files/fxml/mark.fxml").toUri().toURL());
            Pane pane = loader.load();
            Circle hit = (Circle) (pane).getChildren().get(0);
            setCircleFill(hit, message.getColour());
            if(!message.isMark()) {
                hp.add(pane, message.getPosition(), 0);
                hp.setId("hp" + message.getPosition());
            }
            else{
                if(message.getPosition() > 9) {
                    row = 1;
                    column = message.getPosition() - 10;
                }
                else {
                    row = 0;
                    column = message.getPosition();
                }
                mark.add(hit, column, row);
                hit.setId("mark" + column + row);
            }
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve hp asset");
        }catch (IOException e){
            Log.severe("Could not load hit asset");
        }
    }

    private void setCircleFill(Circle circle, String colour){
        if(colour.equalsIgnoreCase("blue"))
            circle.setFill(Color.BLUE);
        else if (colour.equalsIgnoreCase("red"))
            circle.setFill(Color.RED);
        else if (colour.equalsIgnoreCase("yellow"))
            circle.setFill(Color.YELLOW);
        else if (colour.equalsIgnoreCase("gray"))
            circle.setFill(Color.GRAY);
        else if (colour.equalsIgnoreCase("green"))
            circle.setFill(Color.GREEN);
        else
            throw new IllegalArgumentException("Could not find such colour: " + colour);

    }
    @Override
    public void dispatch(UiRemoveMarks message) {
        int column;
        int row;
        for(Integer i: message.getPositon()){
            row = 0;
            column = i;
            if(i > 9) {
                row = 1;
                column = i - 10;
            }
            mark.getChildren().remove(scene.lookup("#mark" + column + row));
            column++;
            if(column == 10){
                row = 1;
                column = 0;
            }
            while(scene.lookup("mark" + column + row) != null){
                dispatch(new UiAddDamage(ViewGUI.getInstance().getMarks().get(column + (9 * row)), (column - 1) + (9*row), true));
                column++;
                if(column == 10){
                    row = 1;
                    column = 0;
                }
            }
        }
    }

    @Override
    public void dispatch(UiAmmoUpdate message) {
        redAmmo.setText("Red: " + Collections.frequency(message.getAmmos(), "RED"));
        blueAmmo.setText("Blue: " + Collections.frequency(message.getAmmos(), "BLUE"));
        yellowAmmo.setText("Yellow: " + Collections.frequency(message.getAmmos(), "YELLOW"));
    }

    @Override
    public void dispatch(UiChooseAmmoToPay message) {
        Label label;
        for(String colour: message.getAvailable()){
            label = (Label) scene.lookup("#" + colour.toLowerCase() + "Ammo");
            label.setOnMouseEntered(clickable(scene));
            label.setOnMouseExited(notClickable(scene));
            label.setOnMouseClicked((MouseEvent event) -> {
                for (String cc: message.getAvailable()){
                    scene.lookup("#" + colour.toLowerCase() + "Ammo").setOnMouseEntered(null);
                    scene.lookup("#" + colour.toLowerCase() + "Ammo").setOnMouseExited(null);
                    scene.lookup("#" + colour.toLowerCase() + "Ammo").setOnMouseClicked(null);
                }
                ViewGUI.getInstance().send(new VCChooseAmmoToPayEvent(ViewGUI.getInstance().getUsername(), colour));
            });
        }
    }

    @Override
    public void dispatch(UiFinalFrenzy message) {
        try {
            frenzy = true;
            currentPlayer.setImage(new Image(Paths.get("files/assets/player/player_" + headPlayer + ".png").toUri().toURL().toString()));
            FXMLLoader loader = new FXMLLoader(Paths.get("files/fxml/frenzy.fxml").toUri().toURL());
            ((Pane)scene.lookup("#playerCombo")).getChildren().add(loader.load());
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve final frenzy board for: " + headPlayer);
        }catch (IOException e){
            Log.severe("Could not load final frenzy board for: " + headPlayer);
        }
    }

    @Override
    public void dispatch(UiPausePlayer message) {
        int position = 0;
        for(TableModel t: rowTrack){
            if(t.getUsername().equalsIgnoreCase(message.getPlayerToPause()))
                break;
            position++;
        }
        leaderboard.requestFocus();
        leaderboard.getSelectionModel().select(position);
        leaderboard.getFocusModel().focus(position);
    }

    @Override
    public void dispatch(UiPointsEvent message) {
        for(TableModel t: rowTrack){
            if(t.getUsername().equalsIgnoreCase(message.getUsername())) {
                t.setScore(((Integer) message.getPoints()).toString());
                break;
            }
        }
    }

    @Override
    public void dispatch(UiReloading message){
        directionsText.set("Please choose weapon to reload");
        endTurn.setText("Stop");
        endTurn.setOnAction((ActionEvent event) -> ViewGUI.getInstance().send(new UiStopReloading()));

    }

    @Override
    public void dispatch(UiGrabWeapon message) {
        directionsText.set("Please choose weapon to grab");
    }

    @Override
    public void dispatch(UiGrabLoot message) {
        directionsText.set("Please click on loot to grab it");
    }

    @Override
    public void dispatch(UiActivateWeapons message) {
        directionsText.set("Please choose weapon");
    }

    @Override
    public void dispatch(UiStopReloading message){
        directionsText.set("Reloading is over");
        buttonSetup();
    }

    @Override
    public void dispatch(UiTurnEnd message) {
        directionsText.set("Please wait for your turn");
        endTurn.setDisable(true);
    }

    @Override
    public void dispatch(UiHighlightTileEvent message) {
        directionsText.set("Please choose a tile");
    }

    @Override
    public void dispatch(UiPutWeapon message) {
        showedCard.setImage(null);
    }

    //----------------------------------------------------------------------------------------------------------------//












}







