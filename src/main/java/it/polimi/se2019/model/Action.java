package it.polimi.se2019.model;

import it.polimi.se2019.model.ActionType;
import it.polimi.se2019.model.Direction;
import it.polimi.se2019.model.Figure;

public class Action {
    private ActionType actionType;
    private Integer value;
    private Direction direction;

    public ActionType getActionType() { return actionType; }

    public Direction getDirection() { return direction; }

    public Integer getValue() { return value; }

    public void setActionType(ActionType actionType) { this.actionType = actionType; }

    public void setDirection(Direction direction) { this.direction = direction; }

    public void setValue(Integer value) { this.value = value; }
}
