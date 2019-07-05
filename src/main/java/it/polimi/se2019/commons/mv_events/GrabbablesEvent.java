package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This event is sent to the user to ask them what they wish to grab.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class GrabbablesEvent extends MVEvent {
    private ArrayList<String> grabbables;

    public GrabbablesEvent(String destination, List<String> grabbables) {
        super(destination);
        this.grabbables = new ArrayList<>(grabbables);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public List<String> getGrabbables() {
        return grabbables;
    }
}
