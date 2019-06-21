package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.view.UiDispatcher;

public abstract class UiEvent extends Event {

    public abstract void handle(UiDispatcher uiDispatcher);
}
