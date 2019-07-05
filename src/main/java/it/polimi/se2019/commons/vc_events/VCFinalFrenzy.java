package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.utility.VCEventDispatcher;

public class VCFinalFrenzy extends VCEvent {

    public VCFinalFrenzy(String source) {
        super(source);
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
