package it.polimi.se2019.controller;

import it.polimi.se2019.network.Server;
import it.polimi.se2019.view.VirtualView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TestMatchBeginning {
    List<String> usernames;

    @Mock
    VirtualView virtualView;
    @Mock
    Server server;

    @Before
    public void setup(){
        usernames = new ArrayList<>();
        usernames.add("username1");
        usernames.add("username2");
        usernames.add("username3");
    }


    @Test(expected = NullPointerException.class)
    public void testNullView(){
        new MatchController(null, server);
    }


}
