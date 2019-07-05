package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

/**
 * This event notifies all users when the last skull is removed from the killshot track and only in case final frenzy is activated.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class FinalFrenzyStartingEvent extends MVEvent {

    public FinalFrenzyStartingEvent (String destination){
        super(destination);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
