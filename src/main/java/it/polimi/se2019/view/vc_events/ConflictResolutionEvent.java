package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.Action;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

import java.util.ArrayList;
import java.util.List;

public class ConflictResolutionEvent extends VCEvent {
    private ArrayList<String> selectedPlayers;
    private ArrayList<Action> selectedActions;

    public ConflictResolutionEvent(String source, List<String> selectedPlayers, List<Action> selectedActions) {
        super(source);
        this.selectedPlayers = new ArrayList<>(selectedPlayers);
        this.selectedActions = new ArrayList<>(selectedActions);
    }

    public List<String> getSelectedPlayers() {
        return selectedPlayers;
    }

    public List<Action> getSelectedActions() {
        return selectedActions;
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
