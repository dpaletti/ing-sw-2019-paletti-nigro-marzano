package it.polimi.se2019.server.model;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


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
