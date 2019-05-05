package it.polimi.se2019.network;

import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;



public abstract class NetworkHandler extends Observable<MVEvent> implements Observer<VCEvent>{
    protected abstract void enterMatchMaking();
    public abstract void submit(String toVirtualView);
    public abstract MVEvent retrieve() throws ClassNotFoundException;
    protected abstract void listenToEvent();
}
