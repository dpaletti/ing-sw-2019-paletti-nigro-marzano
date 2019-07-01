package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

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
