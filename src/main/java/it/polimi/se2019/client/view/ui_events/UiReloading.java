package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

import java.util.ArrayList;
import java.util.HashMap;

public class UiReloading extends UiEvent {
    private HashMap<String, ArrayList<String>> priceMap;

    public UiReloading(HashMap<String, ArrayList<String>> priceMap) {
        this.priceMap = priceMap;
    }

    public HashMap<String, ArrayList<String>> getPriceMap() {
        return priceMap;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
