package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

import java.util.ArrayList;
import java.util.List;

public class ChosenTargetAndEffectEvent extends VCEvent {
    ArrayList<ArrayList<String>> targetNames;
    ArrayList<String> effectNames;

    ChosenTargetAndEffectEvent (String source, ArrayList<ArrayList<String>> targetNames, List<String> effectNames){
        super(source);
        this.targetNames= new ArrayList<>(targetNames);
        this.effectNames= new ArrayList<>(effectNames);
    }
    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public ArrayList<ArrayList<String>> getTargetNames() {
        return targetNames;
    }

    public ArrayList<String> getEffectNames() {
        return effectNames;
    }
}
