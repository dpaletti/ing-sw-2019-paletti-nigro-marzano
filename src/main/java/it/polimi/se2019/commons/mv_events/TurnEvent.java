package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This event communicates the user which combination of actions can be chosen by the player.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

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
