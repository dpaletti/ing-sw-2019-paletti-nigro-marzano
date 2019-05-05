package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.View;


public abstract class NetworkHandler extends Observable<MVEvent> implements Observer<VCEvent>{
    protected abstract void enterMatchMaking();
    public abstract void submit(String toVirtualView);
    public abstract void retrieve() throws ClassNotFoundException;
    protected abstract void listenToEvent();
    protected String username;
    protected String password;

    public NetworkHandler(String u, String p, View view){
        this.username = u;
        this.password = p;
        register(view);
        view.register(this);
    }

    public void changeUsernameAndPassword(){
    }
}
