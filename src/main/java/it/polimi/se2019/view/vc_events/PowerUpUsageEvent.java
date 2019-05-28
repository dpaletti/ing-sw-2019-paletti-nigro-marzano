package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.model.AmmoColour;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class PowerUpUsageEvent extends VCEvent {
    private String usedPowerUp;
    private String powerUpColour;

    public PowerUpUsageEvent (String source, String usedPowerUp, String powerUpColour){
        super(source);
        this.powerUpColour= powerUpColour;
        this.usedPowerUp= usedPowerUp;
    }

    public String getUsedPowerUp() {
        return usedPowerUp;
    }

    public String getPowerUpColour() {
        return powerUpColour;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
