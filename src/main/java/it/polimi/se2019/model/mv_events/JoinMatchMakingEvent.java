package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

public class JoinMatchMakingEvent extends MVEvent {
    private String username;

    public JoinMatchMakingEvent(String destination, String username){
        super(destination);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}