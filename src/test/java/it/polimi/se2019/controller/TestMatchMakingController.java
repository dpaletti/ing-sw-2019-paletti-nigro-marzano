package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.view.VirtualView;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvents.DisconnectionEvent;
import it.polimi.se2019.view.VCEvents.JoinEvent;
import it.polimi.se2019.view.VCEvent;
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
    private JoinEvent join;
    @Mock
    private DisconnectionEvent leave;
    @Mock
    private VCEvent vcEvent;
    @Mock
    private Game model;
    @Mock
    private Server server;
    private List<JoinEvent> joinEvents = new ArrayList<>();



    @Before
    public void beforeTest(){
        matchMakingController = new MatchMakingController(model, server);
        virtualView.register(matchMakingController);
        joinEvents.add(new JoinEvent("username1", "password1", "boot1"));
        joinEvents.add(new JoinEvent("username2", "password2", "boot2"));
        joinEvents.add(new JoinEvent("username3", "password3", "boot3"));
        joinEvents.add(new JoinEvent("username4", "password4", "boot4"));
        joinEvents.add(new JoinEvent("username5", "password5", "boot5"));
        joinEvents.add(new JoinEvent("username6", "password6", "boot6"));
    }

    @Test(expected = NullPointerException.class)
    public void testControllerConstruction() throws NullPointerException {
        MatchMakingController nullView = new MatchMakingController(model, server);
    }

    @Test
    public void testJoin3Clients(){
        for(int i = 0; i <= 2 ; i++){
            assertEquals(matchMakingController.getPlayerCount(), i);
            assertFalse(matchMakingController.isMatchMade());
            assertFalse(matchMakingController.isTimerRunning());
            matchMakingController.update(joinEvents.get(i));
        }

    }

    @Test
    public void testJoin5Clients(){
        testJoin3Clients();
        for(int i = 3; i <= 4 ; i++){
            assertEquals(matchMakingController.getPlayerCount(), i);
            assertFalse(matchMakingController.isMatchMade());
            assertTrue(matchMakingController.isTimerRunning());
            matchMakingController.update(joinEvents.get(i));
        }
        assertTrue(matchMakingController.isMatchMade());
        assertFalse(virtualView.getObservers().contains(matchMakingController));
    }

    @Test
    public void testLeave(){
        testJoin3Clients();
        assertTrue(matchMakingController.isTimerRunning());
        matchMakingController.update(leave);
        assertFalse(matchMakingController.isTimerRunning());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLeave(){
        matchMakingController.update(leave);
    }

    @Test
    public void testDispatching(){
        matchMakingController.update(join);
        assertFalse(matchMakingController.isTimerRunning());
        assertFalse(matchMakingController.isMatchMade());
        assertEquals(1, matchMakingController.getPlayerCount());

        matchMakingController.update(vcEvent);
        assertFalse(matchMakingController.isTimerRunning());
        assertFalse(matchMakingController.isMatchMade());
        assertEquals(1, matchMakingController.getPlayerCount());
    }

    @Test
    public void testTimer(){
        testJoin3Clients();
        try {
            //TODO make time configurable from file once the one on MatchMakingController is
            //time needs to be a little bit more than the actual timer
            //there is no strict timer requisite so it is no issue
            Thread.sleep(1100); //NOSONAR
            assertTrue(matchMakingController.isMatchMade());
            assertFalse(virtualView.getObservers().contains(matchMakingController));
        }catch(InterruptedException e){
            Log.severe("Thread sleep interrupted during testing, timer test skipped");
        }
    }
}
