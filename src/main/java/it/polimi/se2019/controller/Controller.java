package it.polimi.se2019.controller;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.view.VirtualView;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.VCEvent;

public abstract class Controller implements Observer<VCEvent> {
    //TODO evaluate the need for storing a reference to the model, probably needed
    protected VirtualView virtualView;
    protected Game model;

    public Controller(VirtualView virtualView){
        if (virtualView == (null))
            throw new NullPointerException();
        this.virtualView = virtualView;
        this.model = null;
    }

    public Controller(VirtualView virtualView, Game model) {
        if (virtualView == (null) || model == (null))
            throw new NullPointerException();
        this.virtualView = virtualView;
        this.model = model;
    }
}
