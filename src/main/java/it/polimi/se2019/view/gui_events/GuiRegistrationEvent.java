package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.view.GuiDispatcher;
import it.polimi.se2019.view.ViewGUI;

public class GuiRegistrationEvent extends GuiEvent {
    private ViewGUI reference;

    public GuiRegistrationEvent(ViewGUI reference){
        this.reference = reference;
    }

    public ViewGUI getReference() {
        return reference;
    }

    @Override
    public void handle(GuiDispatcher guiDispatcher) {

        guiDispatcher.dispatch(this);
    }
}
