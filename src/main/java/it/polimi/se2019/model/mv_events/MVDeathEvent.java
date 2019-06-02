package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class MVDeathEvent extends MVEvent { //notifies users when one dies
    String dead;
    String killer;

    public MVDeathEvent(String destination, String dead, String killer){
        super(destination);
        this.dead= dead;
        this.killer=killer;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
