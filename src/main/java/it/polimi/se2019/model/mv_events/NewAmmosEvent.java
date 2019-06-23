package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.model.Ammo;
import it.polimi.se2019.model.AmmoColour;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.HashSet;
import java.util.Set;

public class NewAmmosEvent extends MVEvent {
    private HashSet<AmmoColour> ammos;
    private String user;
    private int usage;

    public NewAmmosEvent(String destination, Set<AmmoColour> ammos, String user) {
        super(destination);
        this.ammos = new HashSet<>(ammos);
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public Set<AmmoColour> getAmmos() {
        return new HashSet<>(ammos);
    }

    public int getUsage() {
        return usage;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
