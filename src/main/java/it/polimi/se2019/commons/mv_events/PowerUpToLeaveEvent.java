package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class PowerUpToLeaveEvent extends MVEvent {
    private ArrayList<String> discardablePowerUps= new ArrayList<>();

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
