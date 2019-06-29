package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

import java.util.List;

public class UiRemoveMarks extends UiEvent {
    private List<Integer> positon;

    public UiRemoveMarks(List<Integer> positon) {
        this.positon = positon;
    }

    public List<Integer> getPositon() {
        return positon;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
