package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class GrabEvent extends VCEvent {
    private String grabbed;

    public GrabEvent(String source, String grabbed){
        super(source);
        this.grabbed=grabbed;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getGrabbed() {
        return grabbed;
    }
}
