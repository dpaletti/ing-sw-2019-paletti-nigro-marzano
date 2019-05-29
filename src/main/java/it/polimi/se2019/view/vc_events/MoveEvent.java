package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.model.Point;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class MoveEvent extends VCEvent {
    private Point destionation;

    public MoveEvent (String source, Point destionation){
        super(source);
        this.destionation=destionation;
    }

    public Point getDestionation() {
        return destionation;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
