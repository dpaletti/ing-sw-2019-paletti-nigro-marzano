package it.polimi.se2019.model;

import java.io.Serializable;
import java.util.Set;

public class WeaponEffect implements Serializable {
    private String name;
    private Set<Effect> effects;

    public Set<Effect> getEffects() {
        return effects;
    }

    public String getName() {
        return name;
    }

    public void setEffects(Set<Effect> effects) {
        this.effects = effects;
    }

    public void setName(String name) {
        this.name = name;
    }
}
