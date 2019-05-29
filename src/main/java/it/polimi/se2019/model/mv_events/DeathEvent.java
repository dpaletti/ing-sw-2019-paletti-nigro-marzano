package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class DeathEvent extends MVEvent { //notifies users when one dies
    String killer;

    public DeathEvent (String destination, String killer){
        super(destination);
        this.killer=killer;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
