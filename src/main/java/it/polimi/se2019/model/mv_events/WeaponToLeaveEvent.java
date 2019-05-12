package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.Set;

public class WeaponToLeaveEvent extends MVEvent {
    private Set<String> weaponsOwned;

    public WeaponToLeaveEvent(String destination){
        super(destination);
    }

    public Set<String> getWeaponsOwned() {
        return weaponsOwned;
    }

    public void setWeaponsOwned(Set<String> weaponsOwned) {
        this.weaponsOwned = weaponsOwned;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
