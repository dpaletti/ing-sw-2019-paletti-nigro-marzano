package it.polimi.se2019.commons.utility;


import it.polimi.se2019.client.view.UiDispatcher;

import java.io.Serializable;

/**
 * This class is extended by all model-view and view-controller events and it implements methods needed to allow the dispatching of all events.
 */

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
    public void handle(UiDispatcher uiDispatcher){
        uiDispatcher.dispatch(this);
    }

}
