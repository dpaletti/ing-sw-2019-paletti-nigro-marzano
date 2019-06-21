package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.view.UiDispatcher;
import it.polimi.se2019.view.ViewGUI;

public class UiRegistrationEvent extends UiEvent {
    private ViewGUI reference;

    public UiRegistrationEvent(ViewGUI reference){
        this.reference = reference;
    }

    public ViewGUI getReference() {
        return reference;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {

        uiDispatcher.dispatch(this);
    }
}
