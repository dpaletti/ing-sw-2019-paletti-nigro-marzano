package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

import java.util.List;

public class UiHidePlayers extends UiEvent {
    private List<String> figuresToHide;


    public UiHidePlayers(List<String> figuresToHide) {
        this.figuresToHide = figuresToHide;
    }

    public List<String> getFiguresToHide() {
        return figuresToHide;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }


}
