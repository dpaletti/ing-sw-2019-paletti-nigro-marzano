package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.view.UiDispatcher;

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
