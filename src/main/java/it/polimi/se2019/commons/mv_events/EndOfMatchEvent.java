package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.HashMap;
import java.util.Map;

public class EndOfMatchEvent extends MVEvent {
    private HashMap<String, Integer> finalPoints;

    public EndOfMatchEvent(String destination, Map<String, Integer> finalPoints) {
        super(destination);
        this.finalPoints = new HashMap<>(finalPoints);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}