package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.model.Point;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class MoveEvent extends MVEvent { //notifies user when movement takes place
    Point finalPosition;

    public MoveEvent (String destination, Point finalPosition){
        super(destination);
        this.finalPosition=finalPosition;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
