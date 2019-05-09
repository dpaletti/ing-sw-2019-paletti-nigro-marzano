package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class JoinEvent extends VCEvent {
    private String username;
    private String token;

    public JoinEvent(String source, String username){
        super(source);
        this.username = username;
        this.token = null;
    }

    public JoinEvent(String source, String username, String token){
        super(source);
        this.username = username;
        this.token = token;
    }


    public String getToken() {
        return token;
    }
    public String getUsername(){
        return username;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
