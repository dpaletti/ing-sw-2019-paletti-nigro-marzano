package it.polimi.se2019.model;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class AllowedWeaponsEvent extends MVEvent {
    ArrayList<String> weapons;

    public AllowedWeaponsEvent (String destination, List<String> weapon){
        super(destination);
        this.weapons= new ArrayList<>(weapon);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
