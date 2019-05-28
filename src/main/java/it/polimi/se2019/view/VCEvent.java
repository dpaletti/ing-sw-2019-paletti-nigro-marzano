package it.polimi.se2019.view;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.VCEventDispatcher;

public abstract class VCEvent extends Event {
    //Event coming from the view to the controller

    private String source;

    public VCEvent(String source){
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String remoteEndId) {
        this.source = remoteEndId;
    }

    public abstract void handle(VCEventDispatcher dispatcher);

}
