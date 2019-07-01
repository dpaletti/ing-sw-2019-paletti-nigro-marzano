package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

public class DiscardedPowerUpEvent extends VCEvent {
    private String discardedPowerUp;

    public DiscardedPowerUpEvent (String source, String discardedPowerUp){
        super(source);
        this.discardedPowerUp= discardedPowerUp;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getDiscardedPowerUp() {
        return discardedPowerUp;
    }
}
