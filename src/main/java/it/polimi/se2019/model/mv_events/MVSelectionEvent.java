package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MVSelectionEvent extends MVEvent { //Arlecchino
    private ArrayList<Point> tiles;
    private ArrayList<String> players;
    private boolean isArea; //  if isArea is true, all tiles or players in the event must be attacked, else a subset can be selected

    public MVSelectionEvent(String destination, List<Point> tiles, List<String> players, boolean isArea){
        super(destination);
        this.players= new ArrayList<>(players);
        this.tiles= new ArrayList<>(tiles);
        this.isArea= isArea;
    }


    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public List<Point> getTiles() {
        return tiles;
    }

    public List<String> getPlayers() {
        return players;
    }

    public boolean isArea() {
        return isArea;
    }
}
