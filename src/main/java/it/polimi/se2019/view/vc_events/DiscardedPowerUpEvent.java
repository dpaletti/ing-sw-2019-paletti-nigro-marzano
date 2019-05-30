package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

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
