package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

public class MVDeathEvent extends MVEvent { //notifies users when one dies
    private String dead;
    private String killer;
    private boolean overkill;
    private boolean isMatchOver;

    public MVDeathEvent(String destination, String dead, String killer, boolean overkill, boolean isMatchOver){
        super(destination);
        this.dead= dead;
        this.killer=killer;
        this.overkill= overkill;
        this.isMatchOver= isMatchOver;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getDead() {
        return dead;
    }

    public String getKiller() {
        return killer;
    }

    public boolean isOverkill() {
        return overkill;
    }

    public boolean isMatchOver() {
        return isMatchOver;
    }
}
