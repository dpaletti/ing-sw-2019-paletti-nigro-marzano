package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiUnpausePlayer extends UiEvent{
    private String playerToUnpause;

    public String getPlayerToUnpause() {
        return playerToUnpause;
    }

    public UiUnpausePlayer(String playerToUnpause) {
        this.playerToUnpause = playerToUnpause;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
