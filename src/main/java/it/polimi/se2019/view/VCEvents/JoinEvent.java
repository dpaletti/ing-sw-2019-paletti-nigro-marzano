package it.polimi.se2019.view.VCEvents;

import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class JoinEvent extends VCEvent {
    private String username;
    private String password;

    public JoinEvent(String source, String password, String username){
        super(source);
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public String getUsername(){
        return username;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
