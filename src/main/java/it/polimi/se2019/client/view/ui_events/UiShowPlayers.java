package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

import java.util.List;

public class UiShowPlayers extends UiEvent {
    private List<String> figuresToShow;
    private List<String> highlightedFigures;

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public UiShowPlayers(List<String> figuresToShow, List<String> highlightedFigures) {
        this.figuresToShow = figuresToShow;
        this.highlightedFigures = highlightedFigures;
    }


    public List<String> getHighlightedFigures() {
        return highlightedFigures;
    }

    public List<String> getFiguresToShow() {
        return figuresToShow;
    }
}
