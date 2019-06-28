package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class MVChooseAmmoToPayEvent extends MVEvent {
    private ArrayList<String> availableAmmos;

    public MVChooseAmmoToPayEvent(String destination, List<String> availableAmmos) {
        super(destination);
        this.availableAmmos = new ArrayList<>(availableAmmos);
    }

    public ArrayList<String> getAvailableAmmos() {
        return availableAmmos;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
