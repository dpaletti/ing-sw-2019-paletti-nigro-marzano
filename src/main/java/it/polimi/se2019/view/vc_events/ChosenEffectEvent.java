package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

import java.util.List;

public class ChosenEffectEvent extends VCEvent {

    private List<String> effectNames;
    public ChosenEffectEvent (String source, List<String> effectNames){
        super(source);
        this.effectNames=effectNames;
    }

    public List<String> getEffectNames() {
        return effectNames;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

}
