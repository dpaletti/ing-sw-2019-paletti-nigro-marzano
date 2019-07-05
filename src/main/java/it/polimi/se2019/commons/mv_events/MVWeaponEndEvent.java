package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

/**
 * This event notifies the user that their weapon is over and cannot be used any longer.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class MVWeaponEndEvent extends MVEvent {

    public MVWeaponEndEvent(String destination) {
        super(destination);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
