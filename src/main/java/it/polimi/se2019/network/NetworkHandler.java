package it.polimi.se2019.network;

import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class NetworkHandler extends Observable<MVEvent> implements Observer<VCEvent>, Serializable{
    protected String username;
    protected String token;

    protected abstract void enterRoom();
    public abstract void submit(String toVirtualView);
    public abstract void retrieve() throws ClassNotFoundException;
    protected abstract void listenToEvent();


    public NetworkHandler(String username, String token){
        this(username);
        this.token = token;
    }

    public NetworkHandler(String username){
        observers = new ArrayList<>();
        this.username = username;
        this.token = null;
    }

}
