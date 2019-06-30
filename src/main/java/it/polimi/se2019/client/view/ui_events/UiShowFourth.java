package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiShowFourth extends UiEvent{
    private String card;
    private boolean isWeapon;

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public UiShowFourth(String card, boolean isWeapon) {
        this.card = card;
        this.isWeapon = isWeapon;
    }

    public boolean isWeapon() {
        return isWeapon;
    }

    public String getCard() {
        return card;
    }
}
