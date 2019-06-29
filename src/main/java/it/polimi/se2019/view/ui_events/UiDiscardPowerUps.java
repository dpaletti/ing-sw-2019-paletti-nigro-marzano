package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

import java.util.List;

public class UiDiscardPowerUps extends UiEvent {
    private List<String> discardable;

    public UiDiscardPowerUps(List<String> discardable) {
        this.discardable = discardable;
    }

    public List<String> getDiscardable() {
        return discardable;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
