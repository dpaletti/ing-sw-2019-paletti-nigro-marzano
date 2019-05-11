package it.polimi.se2019.network;

import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.JoinEvent;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class NetworkHandler extends Observable<MVEvent> implements Observer<VCEvent>, Serializable{
    protected String token;

    public abstract void submit(VCEvent vcEvent);
    public abstract void retrieve() throws ClassNotFoundException;
    protected abstract void listenToEvent();
    public abstract void setToken(String token);


    public void chooseUsername(String username){
        update(new JoinEvent(token, username));
    }


    public NetworkHandler(String token){
        //reconnection after client crash
        this();
        this.token = token;
    }

    public NetworkHandler(){
        //first connection
        observers = new ArrayList<>();
        this.token = null;
    }

}
