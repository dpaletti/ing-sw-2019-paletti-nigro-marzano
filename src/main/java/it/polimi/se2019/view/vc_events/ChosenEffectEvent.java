package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

import java.util.List;

public class ChosenEffectEvent extends VCEvent {
    private String effectPosition;
    private String weapon;

    public ChosenEffectEvent (String source, String effectPosition, String weapon){
        super(source);
        this.effectPosition= effectPosition;
        this.weapon= weapon;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getEffectPosition() {
        return effectPosition;
    }

    public String getWeapon() {
        return weapon;
    }
}
