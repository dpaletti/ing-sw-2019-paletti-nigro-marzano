package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//event containing the targetset of a specific partial, it can be skipped and the next available partial is sent in case it exists

public class PartialSelectionEvent extends MVEvent {
    private ArrayList<String> targetPlayers;
    private ArrayList<Point> targetTiles;

    public PartialSelectionEvent(String destination, List<String> targetPlayers) {
        super(destination);
        this.targetPlayers = new ArrayList<>(targetPlayers);
    }

    public PartialSelectionEvent(List<Point> targetTiles, String destination) {
        super(destination);
        this.targetTiles = new ArrayList<>(targetTiles);
    }

    public List<String> getTargetPlayers() {
        return targetPlayers;
    }

    public List<Point> getTargetTiles() {
        return targetTiles;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
