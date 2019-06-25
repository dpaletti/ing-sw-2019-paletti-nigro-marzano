package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class UsablePowerUpEvent extends MVEvent {
    private String usablePowerUp;
    private boolean costs;

    public UsablePowerUpEvent(String destination, String usablePowerUp, boolean costs) {
        super(destination);
        this.usablePowerUp = usablePowerUp;
        this.costs = costs;
    }

    public String getUsablePowerUp() {
        return usablePowerUp;
    }

    public boolean isCosts() {
        return costs;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
