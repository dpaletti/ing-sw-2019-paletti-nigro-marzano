package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

import java.util.List;

public class UiLockPlayers extends UiEvent {
    private List<String> figuresToLock;
    private List<String> highlighted;

    public UiLockPlayers(List<String> figuresToLock, List<String> highlighted) {
        this.figuresToLock = figuresToLock;
        this.highlighted = highlighted;
    }

    public List<String> getFiguresToLock() {
        return figuresToLock;
    }

    public List<String> getHighlighted() {
        return highlighted;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
