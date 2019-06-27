package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

public class UiPutPowerUp extends UiEvent {
    private String powerup;

    public UiPutPowerUp(String powerup) {
        this.powerup = powerup;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public String getPowerup() {
        return powerup;
    }
}
