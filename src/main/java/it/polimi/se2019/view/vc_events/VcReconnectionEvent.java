package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class VcReconnectionEvent extends VCEvent {
    private String oldToken;
    private String username;

    public VcReconnectionEvent(String source, String oldToken, String username){
        super(source);
        this.oldToken = oldToken;
        this.username = username;
    }

    public String getOldToken() {
        return oldToken;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
