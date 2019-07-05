package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This event communicates the user the reload prices of the weapons.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class ReloadableWeaponsEvent extends MVEvent {
    private HashMap<String, ArrayList<String>> priceMap;

    public ReloadableWeaponsEvent(String destination, HashMap<String, ArrayList<String>> priceMap) {
        super(destination);
        this.priceMap = priceMap;
    }

    public HashMap<String, ArrayList<String>> getPriceMap() {
        return priceMap;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
