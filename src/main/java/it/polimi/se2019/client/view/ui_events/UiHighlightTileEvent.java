package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.client.view.UiDispatcher;

public class UiHighlightTileEvent extends UiEvent {
    private Point tile;
    private boolean targeting;
    private String toMove;

    public UiHighlightTileEvent(Point tile, boolean targeting,String toMove){
        this.tile = tile;
        this.targeting = targeting;
        this.toMove=toMove;
    }

    public Point getTile() {
        return tile;
    }

    public boolean isTargeting() {
        return targeting;
    }

    public String getToMove() {
        return toMove;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
