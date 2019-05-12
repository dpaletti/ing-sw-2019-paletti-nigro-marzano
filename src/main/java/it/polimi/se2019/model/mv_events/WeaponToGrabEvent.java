package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.Set;

public class WeaponToGrabEvent extends MVEvent {
    private Set<String> availableWeapons;

    public WeaponToGrabEvent(String destination){
        super(destination);
    }

    public Set<String> getAvailableWeapons() {
        return availableWeapons;
    }

    public void setAvailableWeapons(Set<String> availableWeapons) {
        this.availableWeapons = availableWeapons;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
