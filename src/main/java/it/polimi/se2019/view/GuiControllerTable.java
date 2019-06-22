package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.gui_events.UiMatchSetup;
import it.polimi.se2019.view.gui_events.UiTimerStart;
import it.polimi.se2019.view.gui_events.UiTimerStop;
import it.polimi.se2019.view.gui_events.UiTimerTick;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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
import java.util.concurrent.Semaphore;

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

    private String chosenMap;
    private int skulls;
    private boolean frenzy = false;
    private Semaphore choiceSem = new Semaphore(2, true);
    private GridPane choiceGrid;
    private SimpleStringProperty timerValue;

    @FXML
    private void clickable(){
        currentPlayer.getScene().setCursor(Cursor.HAND);
    }

    @FXML
    private void notClickable(){
        currentPlayer.getScene().setCursor(Cursor.DEFAULT);
    }



    @Override
    public void update(Event message) {
        try {
            ensureJavaFXThread(() -> message.handle(this));
        } catch (UnsupportedOperationException e) {
            Log.fine("ignored " + message);
        }
    }

    @Override
    public void dispatch(UiMatchSetup message) {
        try {
            choiceSem.acquireUninterruptibly(2);
            FXMLLoader choices = new FXMLLoader(Paths.get("files/fxml/match_setup.fxml").toUri().toURL());
            Pane choichePane = choices.load();

            root.add(choichePane,3, 2);
            GridPane mapGrid = new GridPane();
            choiceGrid = (GridPane) choichePane.getChildren().get(0);

            Pane tempPane;
            ImageView tempImage;
            List<Integer> mapLayout = new ArrayList<>();
            mapLayout.add(1);
            mapLayout.add(0);
            mapLayout.add(2);
            for(String conf: message.getConfiguration()) {
                for (int i = 0; i <= (Math.ceil(message.getConfiguration().size() / 3.0)); i++) {
                    for(Integer j: mapLayout) {
                        tempPane = new Pane();
                        tempImage = createMapImage(conf, mapGrid);
                        mapGrid.add(tempPane, i, j);
                        tempImage.setFitWidth(tempPane.getWidth());
                        tempImage.setFitHeight(tempPane.getHeight());
                        tempPane.getChildren().add(tempImage);
                    }
                }
            }

            initializeCheckBox();
            initializeSkullSelection();
            choiceSem.acquireUninterruptibly(2);
            viewGUI.gameSetup(skulls, frenzy, chosenMap);
        }catch (MalformedURLException e){
            Log.severe("Url for table");
        }catch (IOException e){
            Log.severe("Could not load " + e.getCause());
        }
    }

    @Override
    public void dispatch(UiTimerStart message) {
        getTimer().setText("Time left: " + message.getDuration());
        timerValue = new SimpleStringProperty(((Integer) message.getDuration()).toString());
        getTimer().setText("Time left: " + timerValue.get());
        getTimer().textProperty().bind(timerValue);
    }

    @Override
    public void dispatch(UiTimerTick message) {
        int timeToSet = message.getTimeToGo() /1000;
        timerValue.set((((Integer)timeToSet).toString()));
    }

    @Override
    public void dispatch(UiTimerStop message) {
        getTimer().setText("Time is up");
        choiceGrid.setDisable(true);
    }

    public Label getTimer(){
        for(Node n: choiceGrid.getParent().getChildrenUnmodifiable()){
            if(n.getId().equals("choiceTimer"))
                return (Label) n;
        }
        throw new NullPointerException("Could not find timer label");
    }

    public void initializeSkullSelection(){
        GridPane gridPane = null;
        for(Node n: choiceGrid.getChildren()){
            if(n.getId().equals("skullGrid")){
                gridPane = (GridPane) n;
            }
        }
        if(gridPane == null)
            throw new NullPointerException("Could not find skull grid");
        for(Node n: gridPane.getChildren()){
            ((RadioButton) n).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(((RadioButton) event.getSource()).getId().equals("skulls5"))
                        skulls=5;
                    if(((RadioButton) event.getSource()).getId().equals("skulls6"))
                        skulls=6;
                    if(((RadioButton) event.getSource()).getId().equals("skulls7"))
                        skulls=7;
                    if(((RadioButton) event.getSource()).getId().equals("skulls7"))
                        skulls=8;
                    choiceSem.release();
                }
            });
        }
    }

    public void initializeCheckBox(){
        CheckBox frenzyBox = null;
        for(Node n: choiceGrid.getChildren()){
            if(n.getId().equals("frenzy"))
                frenzyBox = (CheckBox) n;
        }

        if(frenzyBox == null)
            throw new NullPointerException("Could not find frenzy check box");

        frenzyBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(((CheckBox) event.getSource()).isSelected()) {
                    frenzy = true;
                    return;
                }
                frenzy = false;
            }
        });


    }


    public ImageView createMapImage(String conf, GridPane mapGrid) throws MalformedURLException{
        ImageView tempImage = new ImageView(Paths.get("files/assets/board/map/" + conf).toUri().toURL().toString());
        tempImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                choiceSem.release();
                highlight(event, conf);
                chosenMap = conf;
                for (Node i : mapGrid.getChildren()) {
                    i.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            notClickable();
                        }
                    });
                }
            }
        });
        tempImage.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                highlight(event, conf);
                clickable();
            }
        });
        tempImage.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                darken(event, conf);
                notClickable();
            }
        });
        return tempImage;
    }

    public void highlight(MouseEvent event, String conf){
        try {
            ((ImageView) event.getSource()).setImage(new Image(Paths.get("files/assets/board/map/" + conf + "_highlighted").toUri().toURL().toString()));
        }catch (MalformedURLException e){
            Log.severe("Could not find: " + conf + "_highlighted");
        }

    }

    public void darken(MouseEvent event, String conf){
        try{
            ((ImageView) event.getSource()).setImage(new Image(Paths.get("files/assets/board/map/" + conf).toUri().toURL().toString()));
        }catch (MalformedURLException e){
            Log.severe("Could not find: " + conf + "_highlighted");
        }
    }



}
