package it.polimi.se2019.view;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.ui_events.UiBoardInitialization;
import it.polimi.se2019.view.ui_events.UiHighlightTileEvent;
import it.polimi.se2019.view.ui_events.UiMoveFigure;
import it.polimi.se2019.view.ui_events.UiResetMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiControllerBoard extends GuiController {

    @FXML
    private GridPane board;
    @FXML
    private ImageView boardLeft;
    @FXML
    private ImageView boardRight;
    @FXML
    private GridPane boardSkull;

    private String leftConfig;
    private String rightConfig;
    private String path = "files/assets/board/board_" ;

    private Scene scene;

    private List<String> redEmpty = new ArrayList<>();
    private List<String> yellowEmpty = new ArrayList<>();
    private List<String> blueEmpty = new ArrayList<>();
    private Map<Point, ArrayList<String>> figuresOnTile = new HashMap<>();
    private List<ImageView> highlightedTiles = new ArrayList<>();

    @Override
    public void dispatch(UiBoardInitialization message) {
        scene = board.getScene();
        initializeSpots(message.getWeaponSpots());
        setupMap(message.getLeftConfig(), message.getRightConfig());
        initializeSkulls(message.getSkulls());
        initializeLoot(message.getLootCards());
    }

    private void place(String figure, Point position){
            Point oldPosition = ViewGUI.getInstance().getPosition(figure);
            leaveTile(oldPosition, figure);
            enterTile(position, figure);
            for(ImageView i: highlightedTiles){
                i.setImage(null);
                i.setDisable(false);
            }
            ViewGUI.getInstance().setPosition(figure, position);
    }

    public void enterTile(Point toUpdate, String figureEntering){
        try {
            if(toUpdate.getX() == -1 && toUpdate.getY() == -1)
                return;

            ImageView centerToUpdate = (ImageView) scene.lookup("#center" + toUpdate.getX() + toUpdate.getY());


            Point actualEntry = null;
            for(Point p: figuresOnTile.keySet()){
                if(p.equals(toUpdate)) {
                    actualEntry = p;
                    figuresOnTile.get(p).add(figureEntering);
                }
            }

            if(actualEntry == null) {
                actualEntry = toUpdate;
                figuresOnTile.put(actualEntry, new ArrayList<>());
                figuresOnTile.get(actualEntry).add(figureEntering);
            }


            if (figuresOnTile.get(actualEntry).size() == 1) {
                centerToUpdate.setImage(new Image(Paths.get("files/assets/player/figure_" + figureEntering.toLowerCase() + ".png").toUri().toURL().toString()));
                toFigure(centerToUpdate, figureEntering.toLowerCase());
            } else if (figuresOnTile.get(actualEntry).size() == 2) {
                centerToUpdate.setImage(new Image(Paths.get("files/assets/black_hole.png").toUri().toURL().toString()));
                toBlackHole(centerToUpdate, actualEntry);
            }
        }catch (MalformedURLException e){
            Log.severe("Could not position " + figureEntering + "on tile: wrong URL");
        }
    }

    public void leaveTile(Point toUpdate, String figureLeaving){
        try {
            if(toUpdate.getX() == -1 && toUpdate.getY() == -1)
                return;
            if (figuresOnTile.get(toUpdate) == null || figuresOnTile.get(toUpdate).isEmpty())
                throw new IllegalStateException(figureLeaving + " leaving" + toUpdate + "but not registered there");

            ImageView centerToUpdate = (ImageView) scene.lookup("#center" + toUpdate.getX() + toUpdate.getY());

            Point actualEntry = null;
            for(Point p: figuresOnTile.keySet()){
                if(p.equals(toUpdate)) {
                    actualEntry = p;
                    figuresOnTile.get(p).remove(figureLeaving);
                }
            }
            if(actualEntry == null)
                throw new IllegalArgumentException("Could not find " + figureLeaving + "in" + toUpdate);


            if (figuresOnTile.get(actualEntry).isEmpty()) {
                centerToUpdate.setImage(null);
                centerToUpdate.setDisable(true);
            }
            if (figuresOnTile.get(actualEntry).size() == 1) {
                centerToUpdate.setImage(new Image(Paths.get("files/assets/player/figure_" + figuresOnTile.get(actualEntry).get(0).toLowerCase() + ".png").toUri().toURL().toString()));
                toFigure(centerToUpdate, figuresOnTile.get(actualEntry).get(0).toLowerCase());
            }else
                figuresOnTile.get(actualEntry).remove(figureLeaving);

        }catch (MalformedURLException e){
            Log.severe("Could not");
        }

    }

    private void toBlackHole(ImageView toUpdate, Point p){
        toUpdate.setOnMouseClicked(handleBlackHoleOnFirstClick(p));
        toUpdate.setOnMouseEntered(handleBlackHoleOnEntrance(p));
        toUpdate.setOnMouseExited(handleBlackHoleOnExit(p));

    }

    private void toFigure(ImageView toUpdate, String figure){
        toUpdate.setDisable(false);
        toUpdate.setOnMouseClicked(handleFigureOnClick(figure));
        toUpdate.setOnMouseEntered(handleFigureOnEntrance());
        toUpdate.setOnMouseExited(handleFigureOnExit());
    }


    private EventHandler<MouseEvent> handleFigureOnEntrance(){
        return (MouseEvent event)-> clickable();
    }

    private EventHandler<MouseEvent> handleFigureOnExit(){
        return (MouseEvent event)-> notClickable();
    }

    private EventHandler<MouseEvent> handleFigureOnClick(String figure) {
        return (MouseEvent event)-> ViewGUI.getInstance().contextSwitch(figure);
    }

    private EventHandler<MouseEvent> handleBlackHoleOnFirstClick(Point p){
        return (MouseEvent event)-> {
            ViewGUI.getInstance().lockPlayers(figuresOnTile.get(p));
            ((ImageView) event.getSource()).setOnMouseClicked(handleBlackHoleOnSecondClick(p));
        };
    }

    private EventHandler<MouseEvent> handleBlackHoleOnSecondClick(Point p){
        return (MouseEvent event)-> {
            ViewGUI.getInstance().unlockPlayers();
            ((ImageView) event.getSource()).setOnMouseClicked(handleBlackHoleOnFirstClick(p));
        };
    }

    private EventHandler<MouseEvent> handleBlackHoleOnEntrance(Point p){
        return (MouseEvent event)-> {
            ViewGUI.getInstance().showPlayers(figuresOnTile.get(p));
            clickable();
        };
    }

    private EventHandler<MouseEvent> handleBlackHoleOnExit(Point p){
        return (MouseEvent event)-> {
            ViewGUI.getInstance().hidePlayers(figuresOnTile.get(p));
            notClickable();
        };
    }

    private void initializeLoot(Map<Point, String> lootMap){
        try {
            ImageView lootPlace;
            Image image;
            for (Map.Entry<Point, String> e : lootMap.entrySet()) {
                lootPlace = ((ImageView) scene.lookup("#loot" + e.getKey().getX() + e.getKey().getY()));
                image = new Image(Paths.get("files/assets/ammo/" + lootMap.get(e.getKey()) + ".png").toUri().toURL().toString());
                lootPlace.setImage(image);
            }
        }catch (MalformedURLException e){
            Log.severe("Could not get loot to put on map");
        }
    }

    private void initializeSkulls(int skulls){
        try {
            FXMLLoader loader;
            Pane pane;
            for(int i = 0; i < skulls  ; i++) {
                loader = new FXMLLoader(Paths.get("files/fxml/board_skull.fxml").toUri().toURL());
                pane = loader.load();
                boardSkull.add(pane, 7 - i, 0);
            }
        }catch (MalformedURLException e){
            Log.severe("Could not get Skull fxml");
        }catch (IOException e){
            Log.severe("Could not get pane from skull fxml");
        }
    }

    private void initializeSpots(Map<String, String> weaponSpots){
        List<String> toAdd = new ArrayList<>();
        toAdd.add("Top");
        toAdd.add("Middle");
        toAdd.add("Bottom");
        redEmpty.addAll(toAdd);
        yellowEmpty.addAll(toAdd);
        blueEmpty.add("Right");
        blueEmpty.add("Left");
        blueEmpty.add("Middle");
        for(String w: weaponSpots.keySet()) {
            if(weaponSpots.get(w).equalsIgnoreCase("red")) {
                fillSpawnCard(w, weaponSpots.get(w), redEmpty.get(0));
                redEmpty.remove(0);
            }
            else if(weaponSpots.get(w).equalsIgnoreCase("blue")) {
                fillSpawnCard(w, weaponSpots.get(w), blueEmpty.get(0));
                blueEmpty.remove(0);
            }
            else if(weaponSpots.get(w).equalsIgnoreCase("yellow")) {
                fillSpawnCard(w, weaponSpots.get(w), yellowEmpty.get(0));
                yellowEmpty.remove(0);
            }
        }
    }

    private void fillSpawnCard(String weapon, String roomColour, String position){
        try {
            Image image = new Image(Paths.get("files/assets/cards/" + weapon + ".png").toUri().toURL().toString());
            ImageView current = ((ImageView) scene.lookup("#" + roomColour.toLowerCase() + position));
            current.setImage(image);

            current.setOnMouseEntered((MouseEvent event) -> {
                ViewGUI.getInstance().show(weapon);
                ((ImageView) event.getSource()).toFront();
            });

            current.setOnMouseExited((MouseEvent event) -> {
                ViewGUI.getInstance().hide(weapon);
                ((ImageView)event.getSource()).toFront();
            });
        }catch (MalformedURLException e){
            Log.severe("Could not load weapon card to put in weaponspot");
        }
    }

    @Override
    public void dispatch(UiHighlightTileEvent message) {
        try {
            ImageView toHighlight = (ImageView) scene.lookup("#tile" + message.getTile().getX() + message.getTile().getY());
            highlightedTiles.add(toHighlight);
            String toQuery;
            if (toHighlight.getParent().getParent().getId().equals("leftGrid"))
                toQuery = path + leftConfig + "/tile" + message.getTile().getX() + message.getTile().getY() + ".png";
            else
                toQuery = path + rightConfig + "/tile" + message.getTile().getX() + message.getTile().getY() + ".png";

            toHighlight.setImage(new Image(Paths.get(toQuery).toUri().toURL().toString()));
            toHighlight.setDisable(false);
            toHighlight.setOnMouseEntered((MouseEvent event) -> clickable());
            toHighlight.setOnMouseExited((MouseEvent event) -> notClickable());
            toHighlight.setOnMouseClicked((MouseEvent event) -> ViewGUI.getInstance().move(message.getTile()));
        }catch (MalformedURLException e){
            Log.severe("Could not get image for highlighting correct tiles");
        }
    }

    @Override
    public void dispatch(UiMoveFigure message) {
        place(message.getFigure(), message.getDestination());
    }

    private void setupMap(String leftConfig, String rightConfig){
        try {
            this.leftConfig = leftConfig;
            this.rightConfig = rightConfig;
            Image leftImage = new Image(Paths.get(path + leftConfig + "/board_" + leftConfig + ".png").toUri().toURL().toString());
            Image rightImage = new Image(Paths.get(path + rightConfig + "/board_" + rightConfig + ".png").toUri().toURL().toString());
            boardLeft.setImage(leftImage);
            boardRight.setImage(rightImage);

            FXMLLoader loader = new FXMLLoader(Paths.get("files/fxml/" + leftConfig + "_tiles.fxml").toUri().toURL());
            GridPane gridLeft = loader.load();
            loader = new FXMLLoader(Paths.get("files/fxml/" + rightConfig + "_tiles.fxml").toUri().toURL());
            GridPane gridRight = loader.load();
            ((Pane)scene.lookup("#leftPane")).getChildren().add(gridLeft);
            ((Pane)scene.lookup("#rightPane")).getChildren().add(gridRight);

        }catch (MalformedURLException e){
            Log.severe("Could not find part of the board");
        }catch (IOException e){
            Log.severe("Could not load map overlay");
        }
    }

    @FXML
    private void clickable() {
        board.getScene().setCursor(Cursor.HAND);
    }

    @FXML
    private void notClickable() {
        board.getScene().setCursor(Cursor.DEFAULT);
    }

    @Override
    public void dispatch(UiResetMap message) {
        for(ImageView i: highlightedTiles) {
            i.setImage(null);
        }
        highlightedTiles = new ArrayList<>();
    }
}
