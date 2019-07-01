package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

import java.util.HashMap;
import java.util.Map;

public class UiActivateWeaponEffects extends UiEvent {
    private String weaponName;
    private Map<String, Integer> effects = new HashMap<>();

    public UiActivateWeaponEffects(String weaponName, Map<String, Integer> effects) {
        this.weaponName = weaponName;
        this.effects = effects;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public Map<String, Integer> getEffects() {
        return effects;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
