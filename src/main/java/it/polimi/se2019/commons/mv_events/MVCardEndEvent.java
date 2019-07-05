package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

/**
 * This event is sent whenever a card is over and cannot be used any longer. If it is a power up, it will be discarded,
 * while if it is a weapon it will simply be unloaded.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class MVCardEndEvent extends MVEvent {
    private boolean isWeapon;

    public MVCardEndEvent(String destination, boolean isWeapon) {
        super(destination);
        this.isWeapon = isWeapon;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public boolean isWeapon() {
        return isWeapon;
    }
}
