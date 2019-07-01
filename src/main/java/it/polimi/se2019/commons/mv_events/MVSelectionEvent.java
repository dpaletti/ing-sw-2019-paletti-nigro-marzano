package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.server.model.Action;
import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MVSelectionEvent extends MVEvent { //Arlecchino
    private HashMap<ArrayList<Action>, ArrayList<ArrayList<String>>> actionOnPlayers = new HashMap<>();
    private HashMap<ArrayList<Action>, ArrayList<ArrayList<Point>>> actionOnTiles = new HashMap<>();

    public MVSelectionEvent(String destination){
        super(destination);
    }


    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public void addActionOnPlayer(List<Action> actions, List<String> targets){
        if(actionOnPlayers.containsKey(new ArrayList<>(actions))) {
            List<ArrayList<String>> list = actionOnPlayers.get(new ArrayList<>(actions));
            list.add(new ArrayList<>(targets));
            actionOnPlayers.put(new ArrayList<>(actions), new ArrayList<>(list));
            return;
        }
        List<ArrayList<String>> list = new ArrayList<>();
        list.add(new ArrayList<>(targets));
        actionOnPlayers.put(new ArrayList<>(actions), new ArrayList<>(list));
    }

    public void addActionOnTiles(List<Action> actions, List<Point> targets){
        if(actionOnTiles.containsKey(new ArrayList<>(actions))) {
            List<ArrayList<Point>> list = actionOnTiles.get(new ArrayList<>(actions));
            list.add(new ArrayList<>(targets));
            actionOnTiles.put(new ArrayList<>(actions), new ArrayList<>(list));
            return;
        }
        List<ArrayList<Point>> list = new ArrayList<>();
        list.add(new ArrayList<>(targets));
        actionOnTiles.put(new ArrayList<>(actions), new ArrayList<>(list));
    }

    public Map<ArrayList<Action>, ArrayList<ArrayList<Point>>> getActionOnTiles() {
        return new HashMap<>(actionOnTiles);
    }

    public Map<ArrayList<Action>, ArrayList<ArrayList<String>>> getActionOnPlayers() {
        return new HashMap<>(actionOnPlayers);
    }
}
