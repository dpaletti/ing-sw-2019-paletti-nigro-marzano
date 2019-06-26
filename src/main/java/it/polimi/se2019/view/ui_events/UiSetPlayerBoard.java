package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

public class UiSetPlayerBoard extends UiEvent {
    private String colour;

    public UiSetPlayerBoard(String colour) {
        this.colour = colour;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public String getColour() {
        return colour;
    }
}
