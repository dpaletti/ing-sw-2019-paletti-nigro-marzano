package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.model.Point;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

public class DefineTeleportPositionEvent extends VCEvent {
    private Point teleportPosition;

    public DefineTeleportPositionEvent (String source, Point teleportPosition){
        super(source);
        this.teleportPosition= teleportPosition;
    }
    public Point getTeleportPosition() {
        return teleportPosition;
    }

    public void setTeleportPosition(Point teleportPosition) {
        this.teleportPosition = teleportPosition;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.update(this);
    }
}
