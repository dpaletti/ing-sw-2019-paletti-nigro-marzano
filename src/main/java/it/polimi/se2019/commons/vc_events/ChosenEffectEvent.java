package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

/**
 * This event communicates the effect of the chosen weapon the user wishes to use.
 * Each effect corresponds to a box shown on the weapon card.
 * See {@link it.polimi.se2019.client.view.VCEvent}.
 */

public class ChosenEffectEvent extends VCEvent {
    private String effectName;
    private String weapon;

    public ChosenEffectEvent (String source, String effectName, String weapon){
        super(source);
        this.effectName= effectName;
        this.weapon= weapon;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getEffectName() {
        return effectName;
    }

    public String getWeapon() {
        return weapon;
    }
}
