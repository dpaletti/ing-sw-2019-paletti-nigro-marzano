package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

public class UiAddDamage extends UiEvent{
    private String colour;
    private int position;
    private boolean isMark;

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public UiAddDamage(String colour, int position, boolean isMark) {
        this.colour = colour;
        this.position = position;
        this.isMark = isMark;
    }

    public String getColour() {
        return colour;
    }

    public int getPosition() {
        return position;
    }

    public boolean isMark() {
        return isMark;
    }
}
