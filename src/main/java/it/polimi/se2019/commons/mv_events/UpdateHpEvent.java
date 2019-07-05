package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

/**
 * This event notifies all users when the hp of a player is updated, communicating the name of the player that attacked them.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

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
