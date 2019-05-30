package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.model.Point;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class MVMoveEvent extends MVEvent { //notifies all users when a user moves
    String username;
    Point finalPosition;

    public MVMoveEvent(String destination, String username, Point finalPosition){
        super(destination);
        this.username=username;
        this.finalPosition=finalPosition;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
