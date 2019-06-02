package it.polimi.se2019.view;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.gui_events.GuiHighlightTileEvent;
import it.polimi.se2019.view.gui_events.GuiMoveFigure;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.lang.reflect.Field;


public class GuiControllerMap extends GuiController {
    @FXML
    private ImageView loot02;

    @FXML
    private ImageView loot10;

    @FXML
    private ImageView loot11;

    @FXML
    private ImageView loot12;

    @FXML
    private ImageView loot20;

    @FXML
    private ImageView loot21;

    @FXML
    private ImageView loot31;

    @FXML
    private ImageView loot32;


    @FXML
    private ImageView tile00;

    @FXML
    private ImageView tile01;

    @FXML
    private ImageView tile02;

    @FXML
    private ImageView tile10;

    @FXML
    private ImageView tile11;

    @FXML
    private ImageView tile12;

    @FXML
    private ImageView tile20;

    @FXML
    private ImageView tile21;

    @FXML
    private ImageView tile22;

    @FXML
    private ImageView tile30;

    @FXML
    private ImageView tile31;

    @FXML
    private ImageView tile32;

    @FXML
    private ImageView skull1;

    @FXML
    private ImageView skull2;

    @FXML
    private ImageView skull3;

    @FXML
    private ImageView skull4;

    @FXML
    private ImageView skull5;

    @FXML
    private ImageView skull6;

    @FXML
    private ImageView skull7;

    @FXML
    private ImageView skull8;

    @FXML
    private ImageView blueFigure;

    @FXML
    private ImageView greenFigure;

    @FXML
    private ImageView yellowFigure;

    @FXML
    private ImageView magentaFigure;

    @FXML
    private ImageView greyFigure;

    @Override
    public void dispatch(GuiHighlightTileEvent message) {
        ImageView tile = getTile(message.getTile());
        //TODO set correct overlay for specified tile
        //tile.setImage(new Image(getUrl(Paths.get("files/assets/tile_overlay.png"))));
    }

    public void dispatch(GuiMoveFigure message){
        //TODO use custom map FXML file for each half so to have best possible alignment
        if(message.getDestination().getX() == -1 && message.getDestination().getY() == -1){
            getFigure(message.getFigure()).setImage(null);
            return;
        }
        Pane tile = ((Pane) getTile(message.getDestination()).getParent());
        for (Node n:
                tile.getChildren()) {
            if(n.equals(loot02) ||
                    n.equals(loot10) ||
                    n.equals(loot11) ||
                    n.equals(loot12) ||
                    n.equals(loot20) ||
                    n.equals(loot21) ||
                    n.equals(loot31) ||
                    n.equals(loot32) ){

            }

        }
    }

    private ImageView getTile(Point p){
        try {
            Field tileField = this.getClass().getDeclaredField("tile" + p.getX() + p.getY());
            tileField.setAccessible(true);
            return (ImageView) tileField.get(this);
        }catch (NoSuchFieldException e){
            Log.severe("Could not find tile to modify");
        }catch (IllegalAccessException e){
            Log.severe("Could not access tile field");
        }
        throw new IllegalStateException("Error while getting tile given its coordinates");
    }

    private ImageView getFigure(String colour){
        try {
            Field figureField = this.getClass().getDeclaredField(colour + "Figure");
            figureField.setAccessible(true);
            return (ImageView) figureField.get(this);
        }catch (NoSuchFieldException e){
            Log.severe("Could not find figure to move");
        }catch (IllegalAccessException e){
            Log.severe("Could not access figure field");
        }
        throw new IllegalStateException("Error while getting figure given its colour");

    }
}
