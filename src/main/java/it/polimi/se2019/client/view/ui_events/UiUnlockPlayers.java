package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiUnlockPlayers extends UiEvent {
    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
