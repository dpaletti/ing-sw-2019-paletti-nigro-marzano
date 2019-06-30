package it.polimi.se2019.server.model;


import java.util.Set;

public abstract class Effect {
    protected int priority;
    protected Set<String> invalidEffects;
    protected String name;

    public String getName() {
        return name;
    }

    public Set<String> getInvalidEffects() {
        return invalidEffects;
    }


}
