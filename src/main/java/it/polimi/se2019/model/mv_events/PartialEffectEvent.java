package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.Action;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

public class PartialEffectEvent extends MVEvent {
    private ArrayList<ArrayList<Action>> possibleActions;
    private ArrayList<ArrayList<String>> previousTargets;

    public PartialEffectEvent(String destination, List<ArrayList<Action>> possibleActions, List<ArrayList<String>> previousTargets) {
        super(destination);
        this.possibleActions = new ArrayList<>(possibleActions);
        this.previousTargets = new ArrayList<>(previousTargets);
    }

    public List<ArrayList<Action>> getPossibleActions() {
        return possibleActions;
    }

    public List<ArrayList<String>> getPreviousTargets() {
        return previousTargets;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
