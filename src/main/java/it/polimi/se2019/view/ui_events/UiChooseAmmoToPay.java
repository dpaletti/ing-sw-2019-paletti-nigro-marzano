package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

import java.util.List;

public class UiChooseAmmoToPay extends UiEvent {
    private List<String> available;

    public UiChooseAmmoToPay(List<String> available) {
        this.available = available;
    }

    public List<String> getAvailable() {
        return available;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
