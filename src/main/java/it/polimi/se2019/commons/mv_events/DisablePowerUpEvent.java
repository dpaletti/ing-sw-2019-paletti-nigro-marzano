package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

public class DisablePowerUpEvent extends MVEvent {
    private String powerUp;

    public DisablePowerUpEvent(String destination, String powerUp) {
        super(destination);
        this.powerUp = powerUp;
    }

    public String getPowerUp() {
        return powerUp;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
