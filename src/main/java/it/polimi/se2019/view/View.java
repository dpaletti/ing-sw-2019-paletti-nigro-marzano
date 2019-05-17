package it.polimi.se2019.view;
import it.polimi.se2019.model.mv_events.ConnectionRefusedEvent;
import it.polimi.se2019.model.mv_events.HandshakeEndEvent;
import it.polimi.se2019.model.mv_events.MatchMakingEndEvent;
import it.polimi.se2019.model.mv_events.SyncEvent;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;

import java.util.ArrayList;

public abstract class View extends Observable<VCEvent> implements Observer<MVEvent>{
    protected Client client;


    public View(Client client){
        this.client = client;
        observers = new ArrayList<>();
    }

    protected class CommonDispatcher extends MVEventDispatcher{

        @Override
        public void update(HandshakeEndEvent message) {
            client.openSession(message.getDestination(), message.getUsernames());
        }

        @Override
        public void update(ConnectionRefusedEvent message) {
            client.connectionRefused(message.getCause());
        }

        @Override
        public void update(SyncEvent message) {
            Log.fine("Received Sync event");
            //TODO maybe parsing
        }

        @Override
        public void update(MatchMakingEndEvent message) {
            Log.fine("MatchMaking is over");
        }
    }

}
