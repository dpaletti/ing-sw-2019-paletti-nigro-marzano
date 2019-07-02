package it.polimi.se2019.client.network;

import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.client.view.View;
import it.polimi.se2019.commons.utility.Event;
import it.polimi.se2019.commons.utility.Observable;
import it.polimi.se2019.commons.utility.Observer;
import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.commons.vc_events.VcJoinEvent;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class NetworkHandler extends Observable<MVEvent> implements Observer<Event>, Serializable, VCEventDispatcher {

    protected transient String token;
    protected transient Client client;
    protected transient Thread listener;

    public abstract void submit(VCEvent vcEvent);
    public abstract void retrieve() throws ClassNotFoundException;
    protected abstract void listenToEvent();

    public void setToken(String token) {
        this.token = token;
    }

    public void stopListening(){
        listener.interrupt();
    }

    public void chooseUsername(String username){
        update(new VcJoinEvent(token, username));
    }

    public NetworkHandler(Client client, View view){
        //first connection
        observers = new ArrayList<>();
        this.token = null;
        this.client = client;
        client.setNetworkHandler(this); //this is needed for RMI sync on delayed connections.
        view.register(this);
        register(view);
    }

}
