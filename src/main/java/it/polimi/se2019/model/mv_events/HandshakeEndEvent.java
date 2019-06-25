package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class HandshakeEndEvent extends MVEvent {
    private ArrayList<String> roomUsernames;
    private ArrayList<String> allUsernames;
    private ArrayList<String> configs; //small, mediumLeft, mediumRight, large

    public HandshakeEndEvent(String destination, List<String> roomUsernames, List<String> allUsernames, List<String> configs){
        //ArrayList needed, List interface is not serializable
        super(destination);
        this.roomUsernames = (ArrayList<String>) roomUsernames;
        this.allUsernames = (ArrayList<String>) allUsernames;
        this.configs = (ArrayList<String>) configs;
    }

    public List<String> getAllUsernames() {
        return allUsernames;
    }

    public List<String> getConfigs() {
        return configs;
    }

    public List<String> getRoomUsernames() {
        return roomUsernames;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
