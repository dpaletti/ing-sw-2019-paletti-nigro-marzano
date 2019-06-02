package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

import java.util.List;

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
