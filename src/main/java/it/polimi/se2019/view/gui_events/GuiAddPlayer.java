package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.view.GuiDispatcher;

public class GuiAddPlayer extends GuiEvent {
    private String player;

    public GuiAddPlayer(String player){
        this.player = player;
    }

    @Override
    public void handle(GuiDispatcher guiDispatcher) {
        guiDispatcher.dispatch(this);
    }

    public String getPlayer() {
        return player;
    }
}
