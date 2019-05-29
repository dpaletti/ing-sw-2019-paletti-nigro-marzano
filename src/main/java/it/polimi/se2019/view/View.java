package it.polimi.se2019.view;
import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Client;
import it.polimi.se2019.utility.*;

import java.util.ArrayList;
import java.util.List;

public abstract class View extends Observable<Event> implements Observer<MVEvent>, MVEventDispatcher{
    //View is observable of VCEvent and GuiEvents to facilitate gui management
    //TODO see whether CliEvents make sense
    protected Client client;

    public View(){

    }

    public View(Client client){
        this.client = client;
        observers = new ArrayList<>();

    }

    public abstract void matchMaking(List<String> usernames);
    public abstract void addPlayer(String username);

    @Override
    public void dispatch(MatchMakingEndEvent message) {
        Log.fine("MatchMaking is over");
    }
    @Override
    public void dispatch(HandshakeEndEvent message) {
        client.openSession(message.getDestination(), message.getUsernames());
    }

    @Override
    public void dispatch(MvJoinEvent message) {
        addPlayer(message.getUsername());
    }

    @Override
    public void dispatch(ConnectionRefusedEvent message) {
        client.connectionRefused(message.getCause());
    }

    @Override
    public void dispatch(SyncEvent message) {
        Log.fine("Received Sync event");
        //TODO maybe parsing
    }
}
