package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.commons.utility.Event;
import it.polimi.se2019.client.view.UiDispatcher;

public abstract class UiEvent extends Event {

    public abstract void handle(UiDispatcher uiDispatcher);
}
