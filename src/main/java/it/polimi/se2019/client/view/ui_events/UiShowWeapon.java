package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiShowWeapon extends UiEvent {
    private String weapon;
    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public UiShowWeapon(String weapon) {
        this.weapon = weapon;
    }

    public String getWeapon() {
        return weapon;
    }
}

