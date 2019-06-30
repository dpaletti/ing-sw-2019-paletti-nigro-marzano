package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.MVEvent;

import java.util.HashMap;
import java.util.Map;

public class BoardRefreshEvent extends MVEvent {
    private HashMap<String, String> weaponSpots;    //<weapon, room colour>
    private HashMap<Point, String> lootCards;       //<point, lootcard>

    public BoardRefreshEvent(String destination, Map<String, String> weaponSpots, Map<Point, String> lootCards) {
        super(destination);
        this.weaponSpots = new HashMap<>(weaponSpots);
        this.lootCards = new HashMap<>(lootCards);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
