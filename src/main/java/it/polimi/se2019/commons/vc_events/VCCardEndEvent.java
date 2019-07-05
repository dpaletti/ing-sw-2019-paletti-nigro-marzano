package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event is sent from the view when receiving a MVWeaponEndEvent.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class VCCardEndEvent extends VCEvent {
    private boolean isWeapon;

    public VCCardEndEvent(String source, boolean isWeapon) {
        super(source);
        this.isWeapon = isWeapon;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);

    }

    public boolean isWeapon() {
        return isWeapon;
    }
}
