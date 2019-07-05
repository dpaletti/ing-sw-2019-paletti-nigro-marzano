package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event is sent when a weapon usage is over.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class VCWeaponEndEvent extends VCEvent {

    public VCWeaponEndEvent(String source) {
        super(source);
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
