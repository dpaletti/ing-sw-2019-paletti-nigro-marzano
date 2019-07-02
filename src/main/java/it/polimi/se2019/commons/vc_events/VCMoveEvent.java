package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

public class VCMoveEvent extends VCEvent {
    private Point destination;
    private boolean isTeleport;
    private String toMove;

    public VCMoveEvent(String source, Point destination, boolean isTeleport,String toMove){
        super(source);
        this.destination = destination;
        this.isTeleport = isTeleport;
        this.toMove=toMove;
    }

    public Point getDestination() {
        return destination;
    }

    public boolean getIsTeleport(){
        return isTeleport;
    }

    public String getToMove() {
        return toMove;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
