package it.polimi.se2019.model;

import java.util.Set;

public class WeaponEffect {
    private String name;
    private Set<Effect> effects;

    public Set<Effect> getEffects() {
        return effects;
    }

    public String getName() {
        return name;
    }

}
