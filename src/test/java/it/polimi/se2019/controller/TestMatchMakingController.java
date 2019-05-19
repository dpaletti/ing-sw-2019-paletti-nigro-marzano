package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.VirtualView;
import it.polimi.se2019.view.vc_events.ChosenEffectEvent;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;
import it.polimi.se2019.view.vc_events.VcJoinEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestMatchMakingController {
    private MatchMakingController matchMakingController;
    @Mock
    private VirtualView virtualView;
    @Mock
    private VcJoinEvent join;
    @Mock
    private DisconnectionEvent leave;
    @Mock
    private VCEvent vcEvent;
    @Mock
    private Game model;
    @Mock
    private Server server;
    private List<VcJoinEvent> vcJoinEvents = new ArrayList<>();



    @Before
    public void beforeTest(){
        matchMakingController = new MatchMakingController(model, server);
        virtualView.register(matchMakingController);
        vcJoinEvents.add(new VcJoinEvent("tok1", "username1"));
        vcJoinEvents.add(new VcJoinEvent("tok2", "username2"));
        vcJoinEvents.add(new VcJoinEvent("tok3", "username3"));
        vcJoinEvents.add(new VcJoinEvent("tok4", "username4"));
        vcJoinEvents.add(new VcJoinEvent("tok5", "username5"));
        vcJoinEvents.add(new VcJoinEvent("tok6", "username6"));
    }

    @Test
    public void testJoin3Clients(){
        for(int i = 0; i <= 2 ; i++){
            assertEquals(matchMakingController.getPlayerCount(), i);
            assertFalse(matchMakingController.isMatchMade());
            assertFalse(matchMakingController.isTimerRunning());
            matchMakingController.update(vcJoinEvents.get(i));
        }

    }

    @Test
    public void testJoin5Clients(){
        testJoin3Clients();
        for(int i = 3; i <= 4 ; i++){
            assertEquals(matchMakingController.getPlayerCount(), i);
            assertFalse(matchMakingController.isMatchMade());
            assertTrue(matchMakingController.isTimerRunning());
            matchMakingController.update(vcJoinEvents.get(i));
        }
        assertTrue(matchMakingController.isMatchMade());
        assertFalse(virtualView.getObservers().contains(matchMakingController));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDispatcher(){
        matchMakingController.update(new ChosenEffectEvent("source"));
    }
}
