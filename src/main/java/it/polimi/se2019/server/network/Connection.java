package it.polimi.se2019.server.network;


import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.client.view.VCEvent;

public interface Connection {
    void submit(MVEvent mvEvent);
    VCEvent retrieve();
    String getToken();
    void disconnect();
    void reconnect();
}
