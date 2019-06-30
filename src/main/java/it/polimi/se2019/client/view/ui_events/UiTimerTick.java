package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

public class UiTimerTick extends UiEvent {
    private int timeToGo;

    public UiTimerTick(int timeToGo){
        this.timeToGo = timeToGo;
    }

    public int getTimeToGo() {
        return timeToGo;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
