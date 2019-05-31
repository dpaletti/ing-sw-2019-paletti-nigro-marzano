package it.polimi.se2019.view.vc_events;

import it.polimi.se2019.utility.Point;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;

import java.util.ArrayList;
import java.util.List;

public class VCSelectionEvent extends VCEvent {
    private ArrayList<Point> selectedTiles;
    private ArrayList<String> selectedPlayers;

    public VCSelectionEvent (String source, List<Point> selectedTiles, List<String> selectedPlayers){
        super(source);
        this.selectedPlayers = new ArrayList<>(selectedPlayers);
        this.selectedTiles = new ArrayList<>(selectedTiles);
    }

    @Override
    public void handle(VCEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public List<Point> getSelectedTiles() {
        return selectedTiles;
    }

    public List<String> getSelectedPlayers() {
        return selectedPlayers;
    }
}
