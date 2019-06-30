package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.client.view.UiDispatcher;

import java.util.Map;

public class UiBoardRefresh extends UiEvent {
    private Map<String, String> weaponSpots;    //<weapon, room colour>
    private Map<Point, String> lootCards;       //<point, lootcard>

    public UiBoardRefresh(Map<String, String> weaponSpots, Map<Point, String> lootCards) {
        this.weaponSpots = weaponSpots;
        this.lootCards = lootCards;
    }

    public Map<String, String> getWeaponSpots() {
        return weaponSpots;
    }

    public Map<Point, String> getLootCards() {
        return lootCards;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
