package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class GrabEvent extends VCEvent {

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
