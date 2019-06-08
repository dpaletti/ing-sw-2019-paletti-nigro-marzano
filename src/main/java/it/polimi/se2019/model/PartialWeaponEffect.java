package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;

import java.util.ArrayList;
import java.util.List;

public class PartialWeaponEffect extends Effect {

    private TargetSpecification targetSpecification;
    private List<Action> actions;
    private boolean endable;

    @Override
    public String toString() {
        return name;
    }

    public TargetSpecification getTargetSpecification() {
        return new TargetSpecification(targetSpecification);
    }

    public List<Action> getActions() {
        return new ArrayList<>(actions);
    }

    public boolean isEndable() {
        return endable;
    }

}
