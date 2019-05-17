package it.polimi.se2019.network;


import it.polimi.se2019.model.mv_events.SyncEvent;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;

import java.util.List;

public interface Connection {
    void submit(MVEvent mvEvent);
    VCEvent retrieve();
    String getToken();
    void disconnect();
    default void reconnect(List<MVEvent> eventBuffer){
        submit(new SyncEvent(this.getToken(), eventBuffer));
    }

    Iterable<MVEvent> getBufferedEvents();
}
