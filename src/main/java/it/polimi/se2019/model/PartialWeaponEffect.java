package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;

public class PartialWeaponEffect extends Effect{

    private TargetSpecification targetSpecification;
    private List<Action> actions;

    @Override
    public String toString() {
        return name;
    }
}
