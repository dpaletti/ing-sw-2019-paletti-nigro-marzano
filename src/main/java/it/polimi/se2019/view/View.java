package it.polimi.se2019.view;
import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class View extends Observable<VCEvent> implements Observer<MVEvent>{
    protected Client client;

    public View(){

    }

    public View(Client client){
        this.client = client;
        observers = new ArrayList<>();

    }

    public abstract void matchMaking(List<String> usernames);
    public abstract void addPlayer(String username);

    protected class CommonDispatcher extends MVEventDispatcher{

        @Override
        public void update(HandshakeEndEvent message) {
            client.openSession(message.getDestination(), message.getUsernames());
        }

        @Override
        public void update(MvJoinEvent message) {
            addPlayer(message.getUsername());
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
