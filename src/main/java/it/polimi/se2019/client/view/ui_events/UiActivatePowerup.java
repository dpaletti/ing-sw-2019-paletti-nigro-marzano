package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiActivatePowerup extends UiEvent{
    private String toActivate;

    public UiActivatePowerup(String toActivate) {
        this.toActivate = toActivate;
    }

    public String getToActivate() {
        return toActivate;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
