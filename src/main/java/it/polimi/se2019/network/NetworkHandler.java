package it.polimi.se2019.network;

import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.JoinEvent;
import it.polimi.se2019.view.vc_events.ReconnectionEvent;

import java.io.*;
import java.util.ArrayList;


public abstract class NetworkHandler extends Observable<MVEvent> implements Observer<VCEvent>, Serializable{
    protected transient String token;
    protected transient Client client;
    protected transient boolean reconnection;
    protected transient Thread listener;

    public abstract void submit(VCEvent vcEvent);
    public abstract void retrieve() throws ClassNotFoundException;
    protected abstract void listenToEvent();

    public void stopListening(){
        listener.interrupt();
    }

    public void setToken(String token){
        this.token = token;
        client.writeToken(token);
    }

    public void reconnect(String temporaryToken){
        //for sync purposes the received token needs to be sent along with the old (but still valid) one
        submit(new ReconnectionEvent(token, temporaryToken));
    }


    public void chooseUsername(String username){
        update(new JoinEvent(token, username));
    }


    public NetworkHandler(String token, Client client){
        //reconnection after client crash
        this(client);
        this.token = token;
        reconnection = true;
    }

    public NetworkHandler(Client client){
        //first connection
        observers = new ArrayList<>();
        this.token = null;
        this.client = client;
        reconnection = false;
    }

    public boolean isReconnection() {
        return reconnection;
    }
}
