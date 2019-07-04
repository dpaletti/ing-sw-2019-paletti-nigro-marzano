package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiRemovePlayer extends UiEvent {
    private String player;
    private int missingPlayers;

    public UiRemovePlayer(String username, int missingPlayers){
        this.player = username;
        this.missingPlayers = missingPlayers;
    }

    public int getMissingPlayers() {
        return missingPlayers;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
