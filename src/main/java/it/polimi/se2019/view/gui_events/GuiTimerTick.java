package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.view.GuiDispatcher;

public class GuiTimerTick extends GuiEvent {
    private int timeToGo;

    public GuiTimerTick(int timeToGo){
        this.timeToGo = timeToGo;
    }

    public int getTimeToGo() {
        return timeToGo;
    }

    @Override
    public void handle(GuiDispatcher guiDispatcher) {
        guiDispatcher.dispatch(this);
    }
}
