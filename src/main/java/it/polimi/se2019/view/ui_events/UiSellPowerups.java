package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

import java.util.List;

public class UiSellPowerups extends UiEvent {
    private List<String> sellable;

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public UiSellPowerups(List<String> sellable) {
        this.sellable = sellable;
    }

    public List<String> getSellable() {
        return sellable;
    }
}

