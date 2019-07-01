package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiContextSwitch extends UiEvent {
    private String newContext;

    public UiContextSwitch(String newContext) {
        this.newContext = newContext;
    }

    public String getNewContext() {
        return newContext;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
