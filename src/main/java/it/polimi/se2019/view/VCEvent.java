package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.VCEventDispatcher;

public abstract class VCEvent extends Event {
    //Event coming from the view to the controller

    private String source;

    public VCEvent(){
        source = null;
    }

    public VCEvent(String remoteEndId){
        this.source = remoteEndId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String remoteEndId) {
        if(this.source == (null))
            this.source = remoteEndId;
        else
            throw new UnsupportedOperationException("Cannot reset an event source");
    }

    public abstract void handle(VCEventDispatcher dispatcher);

}
