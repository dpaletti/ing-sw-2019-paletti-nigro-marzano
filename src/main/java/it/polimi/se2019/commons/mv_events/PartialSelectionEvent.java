package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

//event containing the targetset of a specific partial, it can be skipped and the next available partial is sent in case it exists

public class PartialSelectionEvent extends MVEvent {
    private ArrayList<String> targetPlayers;
    private ArrayList<Point> targetTiles;
    private boolean isSkippable;
    private boolean isWeapon;

    public PartialSelectionEvent(String destination, List<String> targetPlayers, boolean isSkippable,boolean isWeapon) {
        super(destination);
        this.targetPlayers = new ArrayList<>(targetPlayers);
        this.isSkippable = isSkippable;
        this.isWeapon= isWeapon;
    }

    public PartialSelectionEvent(List<Point> targetTiles, String destination, boolean isSkippable,boolean isWeapon) {
        super(destination);
        this.targetTiles = new ArrayList<>(targetTiles);
        this.isSkippable = isSkippable;
        this.isWeapon=isWeapon;
    }

    public List<String> getTargetPlayers() {
        return targetPlayers;
    }

    public List<Point> getTargetTiles() {
        return targetTiles;
    }

    public boolean isSkippable() {
        return isSkippable;
    }

    public boolean isWeapon() {
        return isWeapon;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
