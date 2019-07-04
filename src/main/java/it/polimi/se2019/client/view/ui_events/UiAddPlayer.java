package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiAddPlayer extends UiEvent {
    private String player;
    private int missingPlayers;

    public UiAddPlayer(String player, int missingPlayers){
        this.player = player;
        this.missingPlayers = missingPlayers;
    }

    public int getMissingPlayers() {
        return missingPlayers;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public String getPlayer() {
        return player;
    }
}
