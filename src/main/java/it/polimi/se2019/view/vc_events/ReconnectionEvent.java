package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class ReconnectionEvent extends VCEvent {
    private String temporaryToken;

    public ReconnectionEvent(String source, String temporaryToken){
        super(source);
        this.temporaryToken = temporaryToken;
    }

    public String getTemporaryToken() {
        return temporaryToken;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
