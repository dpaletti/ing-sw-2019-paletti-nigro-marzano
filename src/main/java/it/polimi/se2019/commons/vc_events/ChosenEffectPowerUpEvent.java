package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event is used to communicate the chosen power up effect the user wishes to apply. Effects correspond to a "box"
 * as shown on the card. Although all the power ups used in the game only have one effect, this system allows future expansions
 * of the cards and allows power ups to be treated similarly to weapons.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class ChosenEffectPowerUpEvent extends VCEvent {
    private String effectName;
    private String powerUp;

    public ChosenEffectPowerUpEvent (String source, String effectName, String powerUp){
        super(source);
        this.effectName= effectName;
        this.powerUp= powerUp;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getEffectName() {
        return effectName;
    }

    public String getPowerUp() {
        return powerUp;
    }
}
