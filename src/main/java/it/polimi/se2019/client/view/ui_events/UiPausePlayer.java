package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiPausePlayer extends UiEvent {
    private String playerToPause;

    public UiPausePlayer(String playerToPause) {
        this.playerToPause = playerToPause;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public String getPlayerToPause() {
        return playerToPause;
    }
}
