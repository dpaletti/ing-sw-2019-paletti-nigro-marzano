package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class PowerUpUsageEvent extends VCEvent {
    private String usedPowerUp;

    public String getUsedPowerUp() {
        return usedPowerUp;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
