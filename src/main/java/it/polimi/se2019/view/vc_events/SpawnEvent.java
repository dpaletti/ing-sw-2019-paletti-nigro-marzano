package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class SpawnEvent extends VCEvent {
    private String discardedPowerUpColour; //colour of the power up
    private String powerUpToKeep;   //name of the powerUp to keep, is always null except for the first spawn event in the first match

    public SpawnEvent (String source, String discardedPowerUpColour){
        super(source);
        this.discardedPowerUpColour = discardedPowerUpColour;
        this.powerUpToKeep= null;
    }

    public SpawnEvent (String source, String discardedPowerUpColour, String powerUpToKeep){
        super(source);
        this.discardedPowerUpColour = discardedPowerUpColour;
        this.powerUpToKeep= powerUpToKeep;
    }

    public String getDiscardedPowerUpColour() {
        return discardedPowerUpColour;
    }

    public String getPowerUpToKeep() {
        return powerUpToKeep;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
