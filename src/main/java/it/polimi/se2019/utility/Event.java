package it.polimi.se2019.utility;


import it.polimi.se2019.view.GuiDispatcher;

import java.io.Serializable;

public abstract class Event implements Serializable {
    @Override
    public String toString() {
        return JsonHandler.serialize(this);
    }

    public void handle(EventDispatcher eventDispatcher){
        eventDispatcher.dispatch(this);
    }
    public void handle(MVEventDispatcher mvEventDispatcher){
        mvEventDispatcher.dispatch(this);
    }
    public void handle(VCEventDispatcher vcEventDispatcher){
        vcEventDispatcher.dispatch(this);
    }
    public void handle(GuiDispatcher guiDispatcher){
        guiDispatcher.dispatch(this);
    }

}
