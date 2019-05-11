package it.polimi.se2019.view;


import it.polimi.se2019.network.CallbackInterface;
import it.polimi.se2019.network.Connection;
import it.polimi.se2019.network.ConnectionRMI;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.Log;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class TestVirtualView {

    private VirtualView virtualView;
    @Mock
    Server server;
    @Mock
    CallbackInterface client;

    @Before
    public void setup() {
        try {
            server.startServer();
            virtualView = new VirtualView();
        } catch (Exception e) {
            Log.severe(e.getMessage());
        }
    }

    @Test
    public void testStartListening(){
        Connection connection= new ConnectionRMI("test token", client);
        virtualView.startListening(connection);
        assertTrue(virtualView.getConnections().contains(connection));
    }

}
