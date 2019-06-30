package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.client.view.UiDispatcher;

public class UiGrabLoot extends UiEvent {
    private Point p;

    public UiGrabLoot(Point p){
        this.p = p;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public Point getP() {
        return p;
    }
}
