package it.polimi.se2019.utility;

import it.polimi.se2019.model.Direction;

import java.io.Serializable;

public class Action implements Serializable {
    private ActionType actionType;
    private int value;
    private Direction direction;

    public ActionType getActionType() { return actionType; }

    public Direction getDirection() { return direction; }

    public Integer getValue() { return value; }

    public void setActionType(ActionType actionType) { this.actionType = actionType; }

    public void setDirection(Direction direction) { this.direction = direction; }

    public void setValue(Integer value) { this.value = value; }
}
