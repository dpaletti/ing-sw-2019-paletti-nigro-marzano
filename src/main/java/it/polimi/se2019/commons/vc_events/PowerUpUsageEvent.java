package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event communicates that the user wishes to use a power up they own.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class PowerUpUsageEvent extends VCEvent {
    private String usedPowerUp;

    public PowerUpUsageEvent (String source, String usedPowerUp){
        super(source);
        this.usedPowerUp= usedPowerUp;
    }

    public String getUsedPowerUp() {
        return usedPowerUp;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
