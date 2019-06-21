package it.polimi.se2019.model;
import it.polimi.se2019.utility.Action;
import it.polimi.se2019.utility.ActionType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


@Ignore
public class TestAction {
    Action action= new Action();

    @Before
    public void setAction(){
        action.setActionType(ActionType.MOVE);
        action.setValue(2);
        action.setDirection(Direction.NORTH);
    }

    @Test
    public void testActionGetters(){
        assertEquals(ActionType.MOVE,action.getActionType());
        Integer integer=2;
        assertEquals(integer,action.getValue());
        assertEquals(Direction.NORTH,action.getDirection());
    }
}
