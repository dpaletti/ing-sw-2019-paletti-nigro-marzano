package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

import java.util.List;

public class UiLockPlayers extends UiEvent {
    private List<String> figuresToLock;

    public UiLockPlayers(List<String> figuresToLock) {
        this.figuresToLock = figuresToLock;
    }

    public List<String> getFiguresToLock() {
        return figuresToLock;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
