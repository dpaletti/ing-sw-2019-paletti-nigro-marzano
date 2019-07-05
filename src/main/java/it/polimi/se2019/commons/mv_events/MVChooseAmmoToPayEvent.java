package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This event sends the user the ammos available to pay for the price of a power up.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class MVChooseAmmoToPayEvent extends MVEvent {
    private ArrayList<String> availableAmmos;

    public MVChooseAmmoToPayEvent(String destination, List<String> availableAmmos) {
        super(destination);
        this.availableAmmos = new ArrayList<>(availableAmmos);
    }

    public ArrayList<String> getAvailableAmmos() {
        return availableAmmos;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
