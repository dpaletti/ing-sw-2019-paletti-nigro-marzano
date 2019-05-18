package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

import java.util.ArrayList;

public class ChosenTargetAndEffectEvent extends VCEvent {
    ArrayList<ArrayList<String>> targetNames;
    ArrayList<String> effectNames;

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }

    public ArrayList<ArrayList<String>> getTargetNames() {
        return targetNames;
    }

    public ArrayList<String> getEffectNames() {
        return effectNames;
    }
}
