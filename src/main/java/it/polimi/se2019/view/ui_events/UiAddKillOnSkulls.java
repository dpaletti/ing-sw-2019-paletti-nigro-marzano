package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

public class UiAddKillOnSkulls extends UiEvent {
    private boolean isOverkill;
    private String colour;

    public UiAddKillOnSkulls(boolean isOverkill, String colour) {
        this.isOverkill = isOverkill;
        this.colour = colour;
    }

    public boolean isOverkill() {
        return isOverkill;
    }

    public String getColour() {
        return colour;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
