package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

//event sent to update ammos of a player

public class FinanceUpdateEvent extends MVEvent {
    private String username;
    private ArrayList<String> updatedAmmos;

    public FinanceUpdateEvent(String destination, String username, List<String> updatedAmmos) {
        super(destination);
        this.username = username;
        this.updatedAmmos = new ArrayList<>(updatedAmmos);
    }

    public List<String> getUpdatedAmmos() {
        return updatedAmmos;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
