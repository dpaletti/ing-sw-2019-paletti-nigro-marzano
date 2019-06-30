package it.polimi.se2019.server.model;

import java.util.Set;

public class WeaponEffect extends GenericWeaponEffect {
    private Set<PartialWeaponEffect> effects;
    //Effect type: -1 for basic effect, 0 for alternate mode effect or an only optional effect
    // , 1 optional effect on the left, 2 optional effect on the right

    public Set<PartialWeaponEffect> getEffects() {
        return effects;
    }

    @Override
    public String toString() {
        return name;
    }
}
