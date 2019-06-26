package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

import java.util.List;

public class UiShowPlayers extends UiEvent {
    private List<String> figuresToShow;

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public UiShowPlayers(List<String> figuresToShow) {
        this.figuresToShow = figuresToShow;
    }

    public List<String> getFiguresToShow() {
        return figuresToShow;
    }
}
