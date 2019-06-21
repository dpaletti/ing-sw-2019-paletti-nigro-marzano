package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.UiDispatcher;

public class UiHighlightTileEvent extends UiEvent {
    private Point tile;

    public UiHighlightTileEvent(Point tile){
        this.tile = tile;
    }

    public Point getTile() {
        return tile;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
