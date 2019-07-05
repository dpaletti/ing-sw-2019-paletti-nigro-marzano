package it.polimi.se2019.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class extends Effect and describes a partial effect, which is part of a Weapon Effect.
 * Each effect has its own target specification{@link it.polimi.se2019.server.model.TargetSpecification}.
 */

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
