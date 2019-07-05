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

/**
 * Network manager for client, it is extended by all concrete handlers that take into account
 * a specific protocol, see RMI or Socket implementation
 */
public abstract class NetworkHandler extends Observable<MVEvent> implements Observer<Event>, Serializable, VCEventDispatcher {

    protected transient String token;
    protected transient Client client;
    protected transient Thread listener;


    /**
     * Method for submitting a message to the other hand of the connection
     * @param vcEvent message to be submitted
     */
    public abstract void submit(VCEvent vcEvent);


    /**
     * Method for retrieving messages coming from the other end of the network
     * @throws ClassNotFoundException could be thrown by message deserializers
     */
    public abstract void retrieve() throws ClassNotFoundException;


    /**
     * Async listener on the connection, retrieves messages coming from the
     * connection to retrieve
     */
    protected abstract void listenToEvent();

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Method for communicating joining to the server
      * @param username chosen username for current session
     */
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
