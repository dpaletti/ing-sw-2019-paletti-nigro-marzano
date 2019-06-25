package it.polimi.se2019.network;


import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;

public interface Connection {
    void submit(MVEvent mvEvent);
    VCEvent retrieve();
    String getToken();
    void disconnect();
    void reconnect();
}
