package it.polimi.se2019.client.view;

import it.polimi.se2019.client.network.Client;
import it.polimi.se2019.commons.mv_events.*;
import it.polimi.se2019.commons.utility.*;

import java.util.ArrayList;
import java.util.List;

public abstract class View extends Observable<Event> implements Observer<MVEvent>, MVEventDispatcher {
    //View is observable of VCEvent and GuiEvents to facilitate gui management
    //TODO see whether CliEvents make sense
    protected Client client;

    public View(){

    }

    public View(Client client){
        this.client = client;
        observers = new ArrayList<>();

    }

    public abstract void matchMaking(List<String> usernames, List<String> configs);
    public abstract void addPlayer(String username);

    @Override
    public void dispatch(SetUpEvent message) {
        Log.fine("MatchMaking is over");
    }
    @Override
    public void dispatch(HandshakeEndEvent message) {
        client.openSession(message.getDestination(), message.getRoomUsernames(), message.getAllUsernames(), message.getConfigs());
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
