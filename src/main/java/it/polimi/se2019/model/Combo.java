package it.polimi.se2019.model;

import it.polimi.se2019.utility.Action;

import java.util.List;

public class Combo {
    private ComboType comboType;
    private List<Action> actions;
    private List<Direction> directions;

    public ComboType getComboType() {
        return comboType;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setComboType(ComboType comboType) {
        this.comboType = comboType;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }
}
