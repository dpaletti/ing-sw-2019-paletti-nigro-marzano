package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;

public class Effect {


    private Integer priority;
    private TargetSpecification targetSpecification;
    private List<Action> actions;
    private List<Ammo> price;
    private String name;
    private Map<String, Effect> map;

    public Integer getPriority() { return priority; }

    public List<Action> getActions() { return actions; }

    public TargetSpecification getTargetSpecification() { return targetSpecification; }

    public List<Ammo> getPrice() { return price; }

    public String getName() { return name; }

    public void setActions(List<Action> actions) { this.actions = actions; }

    public void setPriority(Integer priority) { this.priority = priority; }

    public Map<String, Effect> getMap() {
        return map;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
