package it.polimi.se2019.model;

import java.util.List;

public class Combo {
    private ComboType comboType;
    private List<Figure> targets;
    private List<Direction> directions;
    private List<Integer> values;

    public ComboType getComboType() {
        return comboType;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public List<Figure> getTargets() {
        return targets;
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

    public void setTargets(List<Figure> targets) {
        this.targets = targets;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }
}
