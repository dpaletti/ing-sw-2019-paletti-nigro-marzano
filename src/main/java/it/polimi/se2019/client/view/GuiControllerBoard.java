package it.polimi.se2019.client.view;

import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.client.view.ui_events.*;
import it.polimi.se2019.commons.vc_events.GrabEvent;
import it.polimi.se2019.commons.vc_events.VCMoveEvent;
import it.polimi.se2019.commons.vc_events.VCPartialEffectEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import java.util.*;

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
    private int skullToAdd = 0;

    private List<String> redEmpty = new ArrayList<>();
    private List<String> yellowEmpty = new ArrayList<>();
    private List<String> blueEmpty = new ArrayList<>();
    private Map<Point, ArrayList<String>> figuresOnTile = new HashMap<>();
    private List<ImageView> highlightedTiles = new ArrayList<>();
    private Map<Point, String> lootsOnTile;
    private Map<String, String> fromPositionToWeapon = new HashMap<>(); //given the id of the slot one can get the weapon that sits there
    private Map<String, ImageView> highlightedFigures = new HashMap<>(); //given the name of a figure one can get the weapon
    private Map<Integer, Pane> skullMap = new HashMap<>();

    //--------------------------------------------Initialization---------------------------------------------//
    @Override
    public void dispatch(UiBoardInitialization message) {
        scene = board.getScene();

        initializeSpots(message.getWeaponSpots());
        setupMap(message.getLeftConfig(), message.getRightConfig());
        initializeSkulls(message.getSkulls());
        initializeLoot(message.getLootCards());
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

            fromPositionToWeapon.put(roomColour.toLowerCase() + position, weapon);

            current.setOnMouseEntered(show(weapon));
            current.setOnMouseExited(hide(weapon));
        }catch (MalformedURLException e){
            Log.severe("Could not load weapon card to put in weaponspot");
        }
    }

    private EventHandler<MouseEvent> show(String weapon){
        return (MouseEvent event) -> ViewGUI.getInstance().send(new UiShowWeapon(weapon));

    }

    private EventHandler<MouseEvent> hide(String weapon){
        return (MouseEvent event) -> ViewGUI.getInstance().send(new UiHideWeapon(weapon));
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
            ((Pane)scene.lookup("#rightPane")).getChildren().add(gridRight);
            ((Pane)scene.lookup("#leftPane")).getChildren().add(gridLeft);

        }catch (MalformedURLException e){
            Log.severe("Could not find part of the board");
        }catch (IOException e){
            Log.severe("Could not load map overlay");
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
                skullMap.put(7 - i, pane);

            }
            this.skullToAdd = 8 - skulls;
        }catch (MalformedURLException e){
            Log.severe("Could not get Skull fxml");
        }catch (IOException e){
            Log.severe("Could not get pane from skull fxml");
        }
    }

    private void initializeLoot(Map<Point, String> lootMap){
        try {
            ImageView lootPlace;
            lootsOnTile = new HashMap<>();
            Image image;
            for (Map.Entry<Point, String> e : lootMap.entrySet()) {
                lootPlace = ((ImageView) scene.lookup("#loot" + e.getKey().getX() + e.getKey().getY()));
                image = new Image(Paths.get("files/assets/ammo/" + lootMap.get(e.getKey()) + ".png").toUri().toURL().toString());
                lootsOnTile.put(e.getKey(), lootMap.get(e.getKey()));
                lootPlace.setImage(image);
            }
        }catch (MalformedURLException e){
            Log.severe("Could not get loot to put on map");
        }
    }




    //-------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------Turn Management---------------------------------------------//

    //----------------------------------------------------Moving------------------------------------------------------//

    @Override
    public void dispatch(UiMoveFigure message) {
        place(message.getFigure(), message.getDestination());
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

    private void leaveTile(Point toUpdate, String figureLeaving){
        try {
            if(toUpdate.getX() == -1 && toUpdate.getY() == -1)
                return;

            List<String> figuresOnTileToLeave = getFiguresOnTile(toUpdate);

            if (figuresOnTileToLeave.isEmpty())
                throw new IllegalStateException(figureLeaving + " leaving" + toUpdate + "but not registered there");

            ImageView centerToUpdate = (ImageView) scene.lookup("#center" + toUpdate.getX() + toUpdate.getY());

            figuresOnTileToLeave.remove(figureLeaving);
            if (figuresOnTileToLeave.isEmpty()) {
                centerToUpdate.setImage(null);
                centerToUpdate.setOnMouseExited(null);
                centerToUpdate.setOnMouseEntered(null);
                centerToUpdate.setOnMouseClicked(null);
            }
            if (figuresOnTileToLeave.size() == 1) {
                centerToUpdate.setImage(new Image(Paths.get("files/assets/player/figure_" + figuresOnTileToLeave.get(0).toLowerCase() + ".png").toUri().toURL().toString()));
                toFigure(centerToUpdate, figuresOnTileToLeave.get(0).toLowerCase());
            }else
                removeFigureOnTile(figureLeaving);

        }catch (MalformedURLException e){
            Log.severe("Could not");
        }

    }

    private List<String> getFiguresOnTile(Point tile){
        for(Point p: figuresOnTile.keySet()){
            if(p.equals(tile))
                return figuresOnTile.get(p);
        }
        return new ArrayList<>();
    }

    private void removeFigureOnTile(String figure){
        boolean removed = false;
        for(Point p: figuresOnTile.keySet()){
            if (!removed && figuresOnTile.get(p).contains(figure)){
                removed=true;
                figuresOnTile.get(p).remove(figure);
            }else if(removed && figuresOnTile.get(p).contains(figure))
                throw new IllegalStateException("Figure: " + figure + " found in two different tiles");
        }
    }

    private void enterTile(Point toUpdate, String figureEntering ){
        try {
            if(toUpdate.getX() == -1 && toUpdate.getY() == -1)
                return;

            ImageView centerToUpdate = (ImageView) scene.lookup("#center" + toUpdate.getX() + toUpdate.getY());



            if(figuresOnTile.get(toUpdate) != null)
                figuresOnTile.get(toUpdate).add(figureEntering);
            else {
                figuresOnTile.put(toUpdate, new ArrayList<>());
                figuresOnTile.get(toUpdate).add(figureEntering);
            }

            if (figuresOnTile.get(toUpdate).size() == 1) {
                centerToUpdate.setImage(new Image(Paths.get("files/assets/player/figure_" + figureEntering.toLowerCase() + ".png").toUri().toURL().toString()));
                toFigure(centerToUpdate, figureEntering.toLowerCase());
            } else if (figuresOnTile.get(toUpdate).size() >= 2) {
                centerToUpdate.setImage(new Image(Paths.get("files/assets/black_hole.png").toUri().toURL().toString()));
                toBlackHole(centerToUpdate, toUpdate);
            }
        }catch (MalformedURLException e){
            Log.severe("Could not position " + figureEntering + "on tile: wrong URL");
        }
    }

    private void toFigure(ImageView toUpdate, String figure){
        toUpdate.setOnMouseClicked(handleFigureOnClick(figure));
        toUpdate.setOnMouseEntered(handleFigureOnEntrance());
        toUpdate.setOnMouseExited(handleFigureOnExit());
    }


    private EventHandler<MouseEvent> handleFigureOnEntrance(){
        return (MouseEvent event)-> clickable(scene);
    }

    private EventHandler<MouseEvent> handleFigureOnExit(){
        return (MouseEvent event)-> notClickable(scene);
    }

    private EventHandler<MouseEvent> handleFigureOnClick(String figure) {
        return (MouseEvent event)-> {
            ViewGUI.getInstance().setCurrentlyShownFigure(figure);
            ViewGUI.getInstance().send(new UiContextSwitch(figure));
        };
    }


    private void toBlackHole(ImageView toUpdate, Point p){
        toUpdate.setOnMouseClicked(handleBlackHoleOnFirstClick(p));
        toUpdate.setOnMouseEntered(handleBlackHoleOnEntrance(p));
        toUpdate.setOnMouseExited(handleBlackHoleOnExit(p));

    }


    private EventHandler<MouseEvent> handleBlackHoleOnFirstClick(Point p){
        return (MouseEvent event)-> {
            ViewGUI.getInstance().send(new UiLockPlayers(figuresOnTile.get(p), new ArrayList<>(highlightedFigures.keySet())));
            ((ImageView) event.getSource()).setOnMouseClicked(handleBlackHoleOnSecondClick(p));
        };
    }

    private EventHandler<MouseEvent> handleBlackHoleOnSecondClick(Point p){
        return (MouseEvent event)-> {
            ViewGUI.getInstance().send(new UiUnlockPlayers());
            ((ImageView) event.getSource()).setOnMouseClicked(handleBlackHoleOnFirstClick(p));
        };
    }

    private EventHandler<MouseEvent> handleBlackHoleOnEntrance(Point p){
        return (MouseEvent event)-> {
            ViewGUI.getInstance().send(new UiShowPlayers(figuresOnTile.get(p), new ArrayList<>(highlightedFigures.keySet())));
            clickableNoHandler(scene);
        };
    }

    private EventHandler<MouseEvent> handleBlackHoleOnExit(Point p){
        return (MouseEvent event)-> {
            ViewGUI.getInstance().send(new UiHidePlayers(figuresOnTile.get(p)));
            notClickableNoHandler(scene);
        };
    }

    @Override
    public void dispatch(UiResetMap message) {
        for(ImageView i: highlightedTiles) {
            i.setImage(null);
        }
        highlightedTiles = new ArrayList<>();
    }
//-----------------------------------------------------------------------------------------------------//

//---------------------------------------------------Grabbing------------------------------------------//

    @Override
    public void dispatch(UiGrabLoot message) {
        ImageView loot = (ImageView) scene.lookup("#loot" +
                ViewGUI.getInstance().getPosition().getX() +
                ViewGUI.getInstance().getPosition().getY());

        loot.setOnMouseClicked((MouseEvent event) -> {
            ViewGUI.getInstance().send(
                    new GrabEvent(ViewGUI.getInstance().getUsername(),
                            getGrabbed(new Point(ViewGUI.getInstance().getPosition().getX(), ViewGUI.getInstance().getPosition().getY()))));
            loot.setImage(null);
            loot.setOnMouseClicked(null);
            loot.setOnMouseEntered(null);
            loot.setOnMouseExited(null);
        });
        loot.setOnMouseEntered(clickable(scene));
        loot.setOnMouseExited(notClickable(scene));
    }


    public String getGrabbed(Point grabPoint){
        for(Point p: lootsOnTile.keySet()){
            if(grabPoint.getX() == p.getX() && grabPoint.getY() == p.getY())
                return lootsOnTile.get(p);
        }
        throw new IllegalArgumentException("Could not grab");
    }

    @Override
    public void dispatch(UiGrabWeapon message) {
        List<String> positions = new ArrayList<>();
        ImageView currentSpot;
        if(message.getColour().equalsIgnoreCase("red") || message.getColour().equalsIgnoreCase("yellow")) {
            positions.add("Top");
            positions.add("Bottom");
        }else {
            positions.add("Left");
            positions.add("Right");
        }
        positions.add("Middle");

        for(String position: positions){
            currentSpot = ((ImageView) scene.lookup("#" + message.getColour().toLowerCase() + position));
            currentSpot.setOnMouseEntered(show(postionToWeapon(message.getColour().toLowerCase() + position)));
            currentSpot.setOnMouseExited(hide(postionToWeapon(message.getColour().toLowerCase() + position)));
            currentSpot.setOnMouseClicked((MouseEvent event) -> {
                ViewGUI.getInstance().send(new GrabEvent(ViewGUI.getInstance().getUsername(),
                        (postionToWeapon(message.getColour().toLowerCase() + position))));
                ViewGUI.getInstance().send(new UiPutWeapon(postionToWeapon(message.getColour().toLowerCase() + position)));
                removeHandlers((ImageView) event.getSource());
            });
        }
    }

    public String postionToWeapon(String p) {
        for (String position : fromPositionToWeapon.keySet()) {
            if (position.equalsIgnoreCase(p))
                return fromPositionToWeapon.get(position);
        }
        return null;
    }

    @Override
    public void dispatch(UiRemoveWeaponFromSpot message) {
        String position = null;

        for(String pos: fromPositionToWeapon.keySet()){
            if(fromPositionToWeapon.get(pos).equals(message.getWeapon())) {
                position = pos;
                break;
            }
        }
        if(position == null)
            throw new IllegalArgumentException("Could not find such weapon" +
                    "among weapon in weaponSpots: " + message.getWeapon());

        ((ImageView)scene.lookup("#" + position)).setImage(null);
        fromPositionToWeapon.remove(position);

    }

    @Override
    public void dispatch(UiGrabbedLoot message) {
        for(Point p: lootsOnTile.keySet()){
            if(lootsOnTile.get(p).equals(message.getGrabbed())){
                lootsOnTile.remove(p);
                break;
            }

        }
    }

    @Override
    public void dispatch(UiBoardRefresh message) {
        initializeSpots(message.getWeaponSpots());
        initializeLoot(message.getLootCards());
    }

    //----------------------------------------------------------------------------------------------------------------//

    //--------------------------------------------------Highlighting--------------------------------------------------//

    @Override
    public void dispatch(UiHighlightTileEvent message) {
        try {
            ViewGUI.getInstance().setIsWeapon(message.isWeapon());
            ImageView toHighlight = (ImageView) scene.lookup("#tile" + message.getTile().getX() + message.getTile().getY());
            highlightedTiles.add(toHighlight);
            String toQuery;
            if (toHighlight.getParent().getParent().getId().equals("leftGrid"))
                toQuery = path + leftConfig + "/tile" + message.getTile().getX() + message.getTile().getY() + ".png";
            else
                toQuery = path + rightConfig + "/tile" + message.getTile().getX() + message.getTile().getY() + ".png";

            toHighlight.setImage(new Image(Paths.get(toQuery).toUri().toURL().toString()));
            toHighlight.setOnMouseEntered(clickable(scene));
            toHighlight.setOnMouseExited(notClickable(scene));
            if(!message.isTargeting()) //moving
                toHighlight.setOnMouseClicked((MouseEvent event) -> {
                    ViewGUI.getInstance().send(new VCMoveEvent(ViewGUI.getInstance().getUsername(), message.getTile(), false, message.getToMove()));
                    removeHandlers(toHighlight);
                    for(ImageView i: highlightedTiles)
                        i.setImage(null);
                    highlightedTiles = new ArrayList<>();
                });
            else //shooting
                toHighlight.setOnMouseClicked((MouseEvent event) -> {
                    ViewGUI.getInstance().send(new VCPartialEffectEvent(ViewGUI.getInstance().getUsername(), message.getTile(), message.isWeapon()));
                    removeHandlers(toHighlight);
                    for(ImageView i: highlightedTiles)
                        i.setImage(null);
                    highlightedTiles = new ArrayList<>();
                });
        }catch (MalformedURLException e){
            Log.severe("Could not get image for highlighting correct tiles");
        }
    }

    public void dispatch(UiHighlightPlayer message){
        ViewGUI.getInstance().setIsWeapon(message.isWeapon());
        highlightedFigures.put(message.getToHighlight().toLowerCase(), null);
        try{
        ImageView imageToUpdate;
        for(Point tile: figuresOnTile.keySet()){
            if(figuresOnTile.get(tile).size() == 1 && figuresOnTile.get(tile).contains(message.getToHighlight().toLowerCase())){
                imageToUpdate = ((ImageView) scene.lookup("#center" + tile.getX() + tile.getY()));
                imageToUpdate.setImage(new Image(Paths.get("files/assets/player/figure_" + message.getToHighlight().toLowerCase() + "_targeted.png").toUri().toURL().toString()));
                highlightedFigures.put(message.getToHighlight().toLowerCase(), imageToUpdate);
                imageToUpdate.setOnMouseClicked((MouseEvent event) -> {
                    try {
                        ViewGUI.getInstance().send(new VCPartialEffectEvent(ViewGUI.getInstance().getUsername(), ViewGUI.getInstance().getPlayerOnColour(message.getToHighlight().toLowerCase()).getUsername(),message.isWeapon()));
                        ImageView ii;
                        for(String s: highlightedFigures.keySet()){
                            ii = highlightedFigures.get(s);
                            if(ii != null){
                                ii.setImage(new Image(Paths.get("files/assets/player/figure_" + s + ".png").toUri().toURL().toString()));
                                ii.setOnMouseClicked(handleFigureOnClick(message.getToHighlight().toLowerCase()));

                            }
                        }
                        highlightedFigures = new HashMap<>();
                    }catch (MalformedURLException e){
                        Log.severe("MalformedURL");
                    }
                });
            }
        }
        }catch (MalformedURLException e){
            Log.severe("Could not retrieve targeted version for: " + message.getToHighlight());
        }
    }

    @Override
    public void dispatch(UiDarken message) {
        try {
            for (ImageView i : highlightedTiles)
                i.setImage(null);
            highlightedTiles = new ArrayList<>();
            for (Map.Entry<String, ImageView> i : highlightedFigures.entrySet())
                i.getValue().setImage(null);
            highlightedFigures = new HashMap<>();
        }catch (NullPointerException e){
            Log.severe("Could not darken null Imageview on board: ");
        }
    }

    //----------------------------------------------------------------------------------------------------------------//

    //-------------------------------------------------------------Skulls--------------------------------------------------//

    @Override
    public void dispatch(UiAddKillOnSkulls message) {
        try {
            FXMLLoader loader;
            Pane pane;
            if (message.isOverkill())
                pane= new FXMLLoader(Paths.get("files/fxml/tear_board_stacked.fxml").toUri().toURL()).load();
            else
                pane= new FXMLLoader(Paths.get("files/fxml/tear_board_single.fxml").toUri().toURL()).load();

            for(Node n: pane.getChildren()){
                if(message.getColour().equalsIgnoreCase("blue"))
                    ((Circle) n).setFill(Color.BLUE);
                else if(message.getColour().equalsIgnoreCase("green"))
                    ((Circle) n).setFill(Color.GREEN);
                else if(message.getColour().equalsIgnoreCase("yellow"))
                    ((Circle) n).setFill(Color.YELLOW);
                else if(message.getColour().equalsIgnoreCase("magenta"))
                    ((Circle) n).setFill(Color.RED);
                else if(message.getColour().equalsIgnoreCase("grey"))
                    ((Circle) n).setFill(Color.GREY);
                else
                    throw new IllegalArgumentException("Could not find colour: " + message.getColour() + " while setting colour for drops on skulls");
            }


            boardSkull.getChildren().remove(skullMap.get(skullToAdd));
            boardSkull.add(pane, skullToAdd, 0);
            skullToAdd++;

        }catch (MalformedURLException e){
            Log.severe("Could not retrieve fxml to put on skull board");
        }catch (IOException e){
            Log.severe("Could not load fxml to put on skull board");
        }
    }


    //---------------------------------------------------------------------------------------------------------------------//

//--------------------------------------------------------------------------------------------------------------------------//

}
