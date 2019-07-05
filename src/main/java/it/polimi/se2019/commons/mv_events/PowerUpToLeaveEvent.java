package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This event asks the user which power up they wish to leave.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class PowerUpToLeaveEvent extends MVEvent {
    private ArrayList<String> discardablePowerUps;

    public PowerUpToLeaveEvent(String destination, List<String> discardablePowerUps){
        super(destination);
        this.discardablePowerUps= new ArrayList<>(discardablePowerUps);
    }
    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public List<String> getDiscardablePowerUps() {
        return discardablePowerUps;
    }
}
