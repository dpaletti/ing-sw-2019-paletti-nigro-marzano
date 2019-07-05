package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * This event is sent to all players when the match is over and the final points have been calculated.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class EndOfMatchEvent extends MVEvent {
    private HashMap<String, Integer> finalPoints;

    public EndOfMatchEvent(String destination, Map<String, Integer> finalPoints) {
        super(destination);
        this.finalPoints = new HashMap<>(finalPoints);
    }

    public HashMap<String, Integer> getFinalPoints() {
        return finalPoints;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
