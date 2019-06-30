package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class NewAmmosEvent extends MVEvent {
    private ArrayList<String> ammos;
    private String user;
    private int usage;

    public NewAmmosEvent(String destination, List<String> ammos, String user) {
        super(destination);
        this.ammos = new ArrayList<>(ammos);
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public List<String> getAmmos() {
        return ammos;
    }

    public int getUsage() {
        return usage;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
