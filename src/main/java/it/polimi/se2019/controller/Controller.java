package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.network.VirtualView;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.VCEvent;

public abstract class Controller implements Observer<VCEvent> {
    //TODO evaluate the need for storing a reference to the model, probably needed
    protected VirtualView virtualView;

    public Controller(VirtualView virtualView){
        this.virtualView = virtualView;
    }
}
