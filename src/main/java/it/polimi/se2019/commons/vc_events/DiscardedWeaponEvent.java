package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event communicates the weapon the user wishes to discard.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class DiscardedWeaponEvent extends VCEvent {
    private String weapon;

    public DiscardedWeaponEvent(String source, String weapon) {
        super(source);
        this.weapon = weapon;
    }

    public String getWeapon() {
        return weapon;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
