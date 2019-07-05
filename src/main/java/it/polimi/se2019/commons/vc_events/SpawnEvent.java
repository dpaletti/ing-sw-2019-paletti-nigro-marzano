package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event allows the player to spawn on a spawn tile of the same colour of the discarded power up.
 */

public class SpawnEvent extends VCEvent {
    private String discardedPowerUpColour; //colour of the power up

    /**
     * this field is always an empty string "" in the case of a respawn while, in the case of a spawn, it is a power up that the user keeps
     */
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
