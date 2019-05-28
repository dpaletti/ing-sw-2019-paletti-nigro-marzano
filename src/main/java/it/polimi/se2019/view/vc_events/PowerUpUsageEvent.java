package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.model.AmmoColour;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class PowerUpUsageEvent extends VCEvent {
    private String usedPowerUp;
    private AmmoColour powerUpColour;

    public String getUsedPowerUp() {
        return usedPowerUp;
    }

    public AmmoColour getPowerUpColour() {
        return powerUpColour;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
