package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.List;
import java.util.Set;

public class WeaponToLeaveEvent extends MVEvent {
    private List<String> weaponsOwned;

    public WeaponToLeaveEvent(String destination,List<String> weaponsOwned){
        super(destination);
        this.weaponsOwned=weaponsOwned;
    }

    public List<String> getWeaponsOwned() {
        return weaponsOwned;
    }

    public void setWeaponsOwned(List<String> weaponsOwned) {
        this.weaponsOwned = weaponsOwned;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
