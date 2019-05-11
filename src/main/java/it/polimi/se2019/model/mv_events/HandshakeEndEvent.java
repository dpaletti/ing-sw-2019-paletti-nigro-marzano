package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class HandshakeEndEvent extends MVEvent {
    private ArrayList<String> usernames;

    public HandshakeEndEvent(String destination, List<String> usernames){
        //ArrayList needed, List interface is not serializable
        super(destination);
        this.usernames = (ArrayList<String>) usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
