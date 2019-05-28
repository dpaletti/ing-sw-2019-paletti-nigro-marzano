package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class SpawnEvent extends VCEvent {
    private String discardedPowerUp;

    public SpawnEvent (String source, String discardedPowerUp){
        super(source);
        this.discardedPowerUp= discardedPowerUp;
    }

    public String getDiscardedPowerUp() {
        return discardedPowerUp;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
