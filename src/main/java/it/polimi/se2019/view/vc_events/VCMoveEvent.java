package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.Point;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class VCMoveEvent extends VCEvent {
    private Point destination;
    private boolean isTeleport;

    public VCMoveEvent(String source, Point destination, boolean isTeleport){
        super(source);
        this.destination = destination;
        this.isTeleport = isTeleport;
    }

    public Point getDestination() {
        return destination;
    }

    public boolean getIsTeleport(){
        return isTeleport;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
