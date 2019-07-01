package it.polimi.se2019.commons.vc_events;

import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.utility.VCEventDispatcher;
import it.polimi.se2019.client.view.VCEvent;

public class VCPartialEffectEvent extends VCEvent {
    private boolean skip;
    private String targetPlayer;
    private Point targetTile;


    public VCPartialEffectEvent(String source) {
        super(source);
        this.skip = true;
    }

    public VCPartialEffectEvent(String source, String targetPlayer) {
        super(source);
        this.skip = false;
        this.targetPlayer = targetPlayer;
    }

    public VCPartialEffectEvent(String source, Point targetTile) {
        super(source);
        this.skip = false;
        this.targetTile = targetTile;
    }

    public boolean isSkip() {
        return skip;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getTargetPlayer() {
        return targetPlayer;
    }

    public Point getTargetTile() {
        return targetTile;
    }
}
