package it.polimi.se2019.model;

import java.io.Serializable;
import java.util.List;

public class Effect implements Serializable {
    private Integer priority;
    private TargetSpecification targetSpecification;
    private List<ActionType> actions;
    private List<Ammo> price;
    private String name;

    public Integer getPriority() { return priority; }

    public List<ActionType> getActions() { return actions; }

    public TargetSpecification getTargetSpecification() { return targetSpecification; }

    public List<Ammo> getPrice() { return price; }

    public String getName() { return name; }

    public void setActions(List<ActionType> actions) { this.actions = actions; }

    public void setPriority(Integer priority) { this.priority = priority; }

    public void setTargetSpecification(TargetSpecification targetSpecification) { this.targetSpecification = targetSpecification; }

    public void setPrice(List<Ammo> price) { this.price = price; }

    public void setName(String name) { this.name = name; }
}
