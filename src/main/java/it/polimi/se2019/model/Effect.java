package it.polimi.se2019.model;

import java.util.List;

public class Effect {
    private Integer priority;
    private TargetSpecification targetSpecification;
    private List<Action> actions;
    private List<Ammo> price;
    private String name;

    public Integer getPriority() { return priority; }

    public List<Action> getActions() { return actions; }

    public TargetSpecification getTargetSpecification() { return targetSpecification; }

    public List<Ammo> getPrice() { return price; }

    public String getName() { return name; }

    public void setActions(List<Action> actions) { this.actions = actions; }

    public void setPriority(Integer priority) { this.priority = priority; }

    public void setTargetSpecification(TargetSpecification targetSpecification) { this.targetSpecification = targetSpecification; }

    public void setPrice(List<Ammo> price) { this.price = price; }

    public void setName(String name) { this.name = name; }
}
