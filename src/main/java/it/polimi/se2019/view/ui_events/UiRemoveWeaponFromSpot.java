package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

public class UiRemoveWeaponFromSpot extends UiEvent {
    private String weapon;

    public UiRemoveWeaponFromSpot(String weapon) {
        this.weapon = weapon;
    }

    public String getWeapon() {
        return weapon;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
