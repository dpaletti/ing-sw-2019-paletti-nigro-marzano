package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event is sent to the Controller when a user wishes to end their turn before the possible actions they can do are over.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class VCEndOfTurnEvent extends VCEvent {

    public VCEndOfTurnEvent(String source){
        super(source);
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
