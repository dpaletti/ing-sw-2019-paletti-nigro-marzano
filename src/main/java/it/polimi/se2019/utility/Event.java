package it.polimi.se2019.utility;


import java.io.Serializable;

public abstract class Event implements Serializable {
    @Override
    public String toString() {
        return JsonHandler.serialize(this);
    }

    public void handle(EventDispatcher eventDispatcher){
        eventDispatcher.update(this);
    }
}
