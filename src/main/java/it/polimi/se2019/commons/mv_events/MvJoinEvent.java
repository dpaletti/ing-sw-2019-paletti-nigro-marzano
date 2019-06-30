package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

public class MvJoinEvent extends MVEvent {
    private String username;

    public MvJoinEvent(String destination, String username){
        super(destination);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
