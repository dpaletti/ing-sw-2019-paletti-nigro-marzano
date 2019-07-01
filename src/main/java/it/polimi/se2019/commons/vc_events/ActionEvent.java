package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

public class ActionEvent extends VCEvent {
    String action;

    public ActionEvent(String source, String action){
        super(source);
        this.action=action;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getAction() {
        return action;
    }
}
