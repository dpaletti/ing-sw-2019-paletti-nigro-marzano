package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.MVEventDispatcher;

public abstract class MVEvent extends Event {

    private String destination;

    public MVEvent(){
        destination = null;
    }

    public MVEvent(String destination){
        this.destination=destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public abstract void handle(MVEventDispatcher dispatcher);


}
