package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AvailableWeaponsEvent extends MVEvent {
    ArrayList<String> weapons;

    public AvailableWeaponsEvent (String destination, List<String> weapons){
        super(destination);
        this.weapons= new ArrayList<>(weapons);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
