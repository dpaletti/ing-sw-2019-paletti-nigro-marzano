package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.view.GuiDispatcher;

public class GuiRemovePlayer extends GuiEvent {
    private String player;

    public GuiRemovePlayer(String username){
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public void handle(GuiDispatcher guiDispatcher) {
        guiDispatcher.dispatch(this);
    }
}
