package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiHighlightPlayer extends UiEvent {
    private String toHighlight;

    public UiHighlightPlayer(String toHighlight) {
        this.toHighlight = toHighlight;
    }

    public String getToHighlight() {
        return toHighlight;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
