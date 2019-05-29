package it.polimi.se2019.network;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.VcJoinEvent;
import it.polimi.se2019.view.vc_events.VcReconnectionEvent;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class NetworkHandler extends Observable<MVEvent> implements Observer<Event>, Serializable, VCEventDispatcher {

    protected transient String token;
    protected transient String oldToken;
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

    public void setOldToken(String oldToken){
        this.oldToken = oldToken;
    }

    public void reconnect(String newToken){
        setToken(newToken);
        //for sync purposes the received token needs to be sent along with the old (but still valid) one
        submit(new VcReconnectionEvent(newToken, oldToken, client.getUsername())); //token here represents old token
    }


    public void chooseUsername(String username){
        update(new VcJoinEvent(token, username));
    }


    public NetworkHandler(String token, Client client){
        //reconnection after client crash
        this(client);
        setOldToken(token);
        reconnection = true;
    }

    public NetworkHandler(Client client){
        //first connection
        observers = new ArrayList<>();
        this.token = null;
        this.client = client;
        reconnection = false;
        client.getViewRegistration(this);
        client.setNetworkHandler(this); //this is needed for RMI sync on delayed connections.
    }

    public boolean isReconnection() {
        return reconnection;
    }
}
