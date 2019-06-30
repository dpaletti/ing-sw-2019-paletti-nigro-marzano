package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiAvailableMove extends UiEvent {
    private String Combo;

    public UiAvailableMove(String combo) {
        Combo = combo;
    }

    public String getCombo() {
        return Combo;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
