package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.model.Point;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class VCMoveEvent extends VCEvent {
    private Point destination;

    public VCMoveEvent(String source, Point destination){
        super(source);
        this.destination = destination;
    }

    public Point getDestination() {
        return destination;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
