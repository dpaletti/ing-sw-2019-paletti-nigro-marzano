package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This event send the user the weapons they own and that are reloaded.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class AllowedWeaponsEvent extends MVEvent {
    private ArrayList<String> weapons;

    public AllowedWeaponsEvent (String destination, List<String> weapon){
        super(destination);
        this.weapons= new ArrayList<>(weapon);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public ArrayList<String> getWeapons() {
        return weapons;
    }

}
