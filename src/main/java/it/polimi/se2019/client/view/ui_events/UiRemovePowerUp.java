package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiRemovePowerUp extends UiEvent {
    private String powerUp;

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public UiRemovePowerUp(String powerUp) {
        this.powerUp = powerUp;
    }

    public String getPowerUp() {
        return powerUp;
    }
}
