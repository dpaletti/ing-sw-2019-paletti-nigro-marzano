package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

/**
 * This event notifies all users when a player is moved.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class MVMoveEvent extends MVEvent {
    private String username;
    private Point finalPosition;

    public MVMoveEvent(String destination, String username, Point finalPosition){
        super(destination);
        this.username=username;
        this.finalPosition=finalPosition;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public Point getFinalPosition() {
        return finalPosition;
    }

    public String getUsername() {
        return username;
    }
}
