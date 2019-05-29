package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.view.GuiDispatcher;

public abstract class GuiEvent extends Event {

    public abstract void handle(GuiDispatcher guiDispatcher);
}
