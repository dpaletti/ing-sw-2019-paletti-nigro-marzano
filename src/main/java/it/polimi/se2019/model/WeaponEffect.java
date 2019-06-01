package it.polimi.se2019.model;

import java.util.Set;

public class WeaponEffect extends GenericWeaponEffect {
    private Set<PartialWeaponEffect> effects;

    public Set<PartialWeaponEffect> getEffects() {
        return effects;
    }

    @Override
    public String toString() {
        return name;
    }
}
