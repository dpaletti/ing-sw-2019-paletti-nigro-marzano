package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

//player selects which pUp to keep and where to spawn

public class SpawnEvent extends VCEvent {
    private String discardedPowerUpColour; //colour of the power up
    private String powerUpToKeep;   //name of the powerUp to keep, is always "" except for the first spawn event in the first match

    public SpawnEvent (String source, String discardedPowerUpColour){       //used for re-spawns
        super(source);
        this.discardedPowerUpColour = discardedPowerUpColour;
        this.powerUpToKeep= "";
    }

    public SpawnEvent (String source, String discardedPowerUpColour, String powerUpToKeep){     //used for spawns (only first turn)
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
