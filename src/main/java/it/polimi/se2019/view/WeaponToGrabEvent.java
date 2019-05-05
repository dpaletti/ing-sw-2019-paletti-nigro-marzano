package it.polimi.se2019.view;

import java.util.Set;

public class WeaponToGrabEvent extends MVEvent {
    private Set<String> availableWeapons;

    public Set<String> getAvailableWeapons() {
        return availableWeapons;
    }

    public void setAvailableWeapons(Set<String> availableWeapons) {
        this.availableWeapons = availableWeapons;
    }
}
