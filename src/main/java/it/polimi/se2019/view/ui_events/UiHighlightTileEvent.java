package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.UiDispatcher;

public class UiHighlightTileEvent extends UiEvent {
    private Point tile;
    private boolean targeting;

    public UiHighlightTileEvent(Point tile, boolean targeting){
        this.tile = tile;
        this.targeting = targeting;
    }

    public Point getTile() {
        return tile;
    }

    public boolean isTargeting() {
        return targeting;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
