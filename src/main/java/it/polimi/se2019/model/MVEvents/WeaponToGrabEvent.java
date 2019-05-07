package it.polimi.se2019.model.MVEvents;

import it.polimi.se2019.view.MVEvent;

import java.util.Set;

public class WeaponToGrabEvent extends MVEvent {
    private Set<String> availableWeapons;

    public WeaponToGrabEvent(){
        super(null);
    }
    public WeaponToGrabEvent(String destination){
        super(destination);
    }

    public Set<String> getAvailableWeapons() {
        return availableWeapons;
    }

    public void setAvailableWeapons(Set<String> availableWeapons) {
        this.availableWeapons = availableWeapons;
    }
}