package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class ActionEvent extends VCEvent {
    String action;

    public ActionEvent(String source, String action){
        super(source);
        this.action=action;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }

    public String getAction() {
        return action;
    }
}
