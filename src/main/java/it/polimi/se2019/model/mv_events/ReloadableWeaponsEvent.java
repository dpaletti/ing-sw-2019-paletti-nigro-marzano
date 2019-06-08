package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class ReloadableWeaponsEvent extends MVEvent {
    private ArrayList<String> reloadableWeapons;

    public ReloadableWeaponsEvent(String destination, List<String> reloadableWeapons) {
        super(destination);
        this.reloadableWeapons = new ArrayList<>(reloadableWeapons);
    }

    public List<String> getReloadableWeapons() {
        return reloadableWeapons;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
