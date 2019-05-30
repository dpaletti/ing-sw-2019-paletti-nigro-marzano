package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class UpdatePointsEvent extends MVEvent {
    private int points;

    public UpdatePointsEvent (String destination, int points){
        super(destination);
        this.points= points;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
