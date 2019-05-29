package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.view.GuiDispatcher;

public class GuiTimerStart extends GuiEvent {

    @Override
    public void handle(GuiDispatcher guiDispatcher) {
        guiDispatcher.dispatch(this);
    }
}
