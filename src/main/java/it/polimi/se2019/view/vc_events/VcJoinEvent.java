package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class VcJoinEvent extends VCEvent {
    private String username;

    public VcJoinEvent(String source, String username){
        super(source);
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
