package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.client.view.MVEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * This event is sent to the user in order to substitute the grabbed cards at the end of each turn.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class BoardRefreshEvent extends MVEvent {
    private HashMap<String, String> weaponSpots;    //<weapon, room colour>
    private HashMap<Point, String> lootCards;       //<point, lootcard>

    public BoardRefreshEvent(String destination, Map<String, String> weaponSpots, Map<Point, String> lootCards) {
        super(destination);
        this.weaponSpots = new HashMap<>(weaponSpots);
        this.lootCards = new HashMap<>(lootCards);
    }

    public Map<String, String> getWeaponSpots() {
        return weaponSpots;
    }

    public Map<Point, String> getLootCards() {
        return lootCards;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
