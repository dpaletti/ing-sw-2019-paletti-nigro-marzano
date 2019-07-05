package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This event sends the user the list of positions they can move their target to. Their target can both be the player itself or another one
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class AllowedMovementsEvent extends MVEvent {
    private ArrayList<Point> allowedPositions;
    private String userToMove;

    public AllowedMovementsEvent(String destination, List<Point> allowedPositions, String userToMove){
        super(destination);
        this.allowedPositions= new ArrayList<>(allowedPositions);
        this.userToMove = userToMove;
    }

    public List<Point> getAllowedPositions() {
        return new ArrayList<>(allowedPositions);
    }

    public String getUserToMove() {
        return userToMove;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
