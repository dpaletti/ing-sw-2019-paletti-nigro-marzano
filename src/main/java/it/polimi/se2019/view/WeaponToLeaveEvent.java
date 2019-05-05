package it.polimi.se2019.view;

import java.util.Set;

public class WeaponToLeaveEvent extends MVEvent {
    private Set<String> weaponsOwned;

    public Set<String> getWeaponsOwned() {
        return weaponsOwned;
    }

    public void setWeaponsOwned(Set<String> weaponsOwned) {
        this.weaponsOwned = weaponsOwned;
    }
}
