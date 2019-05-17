package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.Server;
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

@RunWith(MockitoJUnitRunner.class)
public class TestMatchController {
    private List<String> usernames;

    @Mock
    Game model;
    @Mock
    Server server;

    private MatchController matchController;

    @Before
    public void setup(){
        usernames = new ArrayList<>();
        usernames.add("username1");
        usernames.add("username2");
        usernames.add("username3");
        matchController = new MatchController(model, server, usernames);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDispatcher(){
        matchController.update(new ChosenEffectEvent("source"));
    }

    @Test
    public void testDisconnection(){
        matchController.update(new DisconnectionEvent("test source"));
    }

    @Test
    public void testJoin(){
        matchController.update(new VcJoinEvent("test source", "test username"));
    }
}
