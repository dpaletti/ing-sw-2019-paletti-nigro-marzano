package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiHideWeapon extends UiEvent{
    private String weapon;


    public UiHideWeapon(String weapon) {
        this.weapon = weapon;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public String getWeapon() {
        return weapon;
    }
}
