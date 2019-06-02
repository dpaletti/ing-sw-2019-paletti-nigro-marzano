package it.polimi.se2019.view;

import it.polimi.se2019.network.Client;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

@Ignore
public class TestMatchMaking {
    @Mock
    Client client;

    ViewGUI viewGui = new ViewGUI(client);
    List<String> usernames = new ArrayList<>();

    @Before
    public void setup(){
        usernames.add("test");
        usernames.add("test2");
        usernames.add("test3");
    }
    @Test
    public void matchMaking(){
        viewGui.matchMaking(usernames);
    }
}
