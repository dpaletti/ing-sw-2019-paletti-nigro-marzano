package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

// actions player can do (eg movearound, shootpeople,...) or use power up

public class TurnEvent extends MVEvent {
    private ArrayList<String> combos;

    public TurnEvent(String destination, List<String> combos) {
        super(destination);
        this.combos = new ArrayList<>(combos);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public List<String> getCombos() {
        return combos;
    }
}
