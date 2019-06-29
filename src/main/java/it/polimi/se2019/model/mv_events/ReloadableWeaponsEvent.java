package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.HashMap;

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
