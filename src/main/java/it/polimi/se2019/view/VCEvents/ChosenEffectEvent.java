package it.polimi.se2019.view.VCEvents;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class ChosenEffectEvent extends VCEvent {

    String effectName;
    public ChosenEffectEvent (String source){
        super(source);
    }

    public String getEffectName() {
        return effectName;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
