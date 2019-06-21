package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.UiDispatcher;

public class UiMoveFigure extends UiEvent {
    private String figure; //colour
    private Point destination; //if destination is (-1, -1) figure is to be taken out of map

    public UiMoveFigure(String figure, Point destination){
        this.figure = figure;
        this.destination = destination;
    }

    public String getFigure() {
        return figure;
    }

    public Point getDestination() {
        return destination;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
