package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.UiDispatcher;

import java.util.Map;

public class UiBoardInitialization extends UiEvent{
    private Map<String, String> weaponSpots;
    private Map<Point, String> lootCards;
    private int skulls;
    private String leftConfig;
    private String rightConfig;

    public UiBoardInitialization(Map<String, String> weaponSpots, Map<Point, String> lootCards, String leftConfig, String rightConfig, int skulls) {
        this.weaponSpots = weaponSpots;
        this.lootCards = lootCards;
        this.leftConfig = leftConfig;
        this.rightConfig = rightConfig;
        this.skulls = skulls;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public Map<Point, String> getLootCards() {
        return lootCards;
    }

    public Map<String, String> getWeaponSpots() {
        return weaponSpots;
    }

    public String getRightConfig() {
        return rightConfig;
    }

    public String getLeftConfig() {
        return leftConfig;
    }

    public int getSkulls() {
        return skulls;
    }
}
