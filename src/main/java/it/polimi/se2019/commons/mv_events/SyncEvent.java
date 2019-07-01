package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class SyncEvent extends MVEvent {
    private ArrayList<MVEvent> events;

    public SyncEvent(String destination, List<MVEvent> events){
        super(destination);
        this.events = new ArrayList<>(events);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
