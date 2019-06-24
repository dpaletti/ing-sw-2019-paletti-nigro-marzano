package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.PartialCombo;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class TurnEvent extends MVEvent {
    private ArrayList<ArrayList<PartialCombo>> possibleMoves;

    public TurnEvent(String destination, List<ArrayList<PartialCombo>> possibleMoves) {
        super(destination);
        this.possibleMoves = new ArrayList<>(possibleMoves);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public List<ArrayList<PartialCombo>> getPossibleMoves() {
        return possibleMoves;
    }
}
