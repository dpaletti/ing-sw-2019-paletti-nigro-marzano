package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

//event containing the targetset of a specific partial, it can be skipped and the next available partial is sent in case it exists

public class PartialSelectionEvent extends MVEvent {
    private ArrayList<String> targetPlayers;
    private ArrayList<Point> targetTiles;
    private boolean isSkippable;

    public PartialSelectionEvent(String destination, List<String> targetPlayers, boolean isSkippable) {
        super(destination);
        this.targetPlayers = new ArrayList<>(targetPlayers);
        this.isSkippable = isSkippable;
    }

    public PartialSelectionEvent(List<Point> targetTiles, String destination, boolean isSkippable) {
        super(destination);
        this.targetTiles = new ArrayList<>(targetTiles);
        this.isSkippable = isSkippable;
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

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
