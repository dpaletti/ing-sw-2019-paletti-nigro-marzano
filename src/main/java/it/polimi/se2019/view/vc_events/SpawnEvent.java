package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.model.AmmoColour;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class SpawnEvent extends VCEvent {
    private AmmoColour discardedPowerUp;

    public AmmoColour getDiscardedPowerUp() {
        return discardedPowerUp;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
