package it.polimi.se2019.model;

import java.util.List;

public class Combo {
    private ComboType comboType;
    private List<Action> actions;
    private List<Direction> directions;
    private List<Integer> values;

    public ComboType getComboType() {
        return comboType;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public List<Integer> getValues() {
        return values;
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

    public void setValues(List<Integer> values) {
        this.values = values;
    }
}
