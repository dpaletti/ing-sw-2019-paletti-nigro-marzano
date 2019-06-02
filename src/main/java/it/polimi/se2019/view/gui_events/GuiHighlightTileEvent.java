package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.GuiDispatcher;

public class GuiHighlightTileEvent extends GuiEvent {
    private Point tile;

    public GuiHighlightTileEvent(Point tile){
        this.tile = tile;
    }

    public Point getTile() {
        return tile;
    }

    @Override
    public void handle(GuiDispatcher guiDispatcher) {
        guiDispatcher.dispatch(this);
    }
}
