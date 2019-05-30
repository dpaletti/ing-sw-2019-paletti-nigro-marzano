package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class UpdateHpEvent extends MVEvent {
    private String attacked;
    private String attacker;

    public UpdateHpEvent (String destination, String attacked, String attacker){
        super(destination);
        this.attacked= attacked;
        this.attacker= attacker;
    }

    public String getAttacked() {
        return attacked;
    }

    public String getAttacker() {
        return attacker;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
