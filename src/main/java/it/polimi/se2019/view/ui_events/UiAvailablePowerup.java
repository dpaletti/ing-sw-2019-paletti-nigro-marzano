package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

public class UiAvailablePowerup extends UiEvent {
    private String powerUp;

    public UiAvailablePowerup(String powerUp) {
        this.powerUp = powerUp;
    }

    public String getPowerUp() {
        return powerUp;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
