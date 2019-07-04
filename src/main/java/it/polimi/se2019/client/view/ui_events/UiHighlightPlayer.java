package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiHighlightPlayer extends UiEvent {
    private String toHighlight;
    private boolean isWeapon;

    public UiHighlightPlayer(String toHighlight, boolean isWeapon) {
        this.toHighlight = toHighlight;
        this.isWeapon = isWeapon;
    }

    public boolean isWeapon() {
        return isWeapon;
    }

    public String getToHighlight() {
        return toHighlight;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
