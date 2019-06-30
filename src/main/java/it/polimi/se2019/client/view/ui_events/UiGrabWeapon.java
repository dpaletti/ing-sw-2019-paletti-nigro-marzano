package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

import java.util.List;

public class UiGrabWeapon extends UiEvent{
    private String colour;
    private List<String> tooExpensiveWeapons;

    public UiGrabWeapon(String colour) {
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
