package it.polimi.se2019.client.view.ui_events;

import it.polimi.se2019.client.view.UiDispatcher;

import java.util.ArrayList;
import java.util.List;

public class UiAmmoUpdate extends UiEvent {
    private List<String> ammos = new ArrayList<>();

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }

    public UiAmmoUpdate(List<String> ammos) {
        this.ammos = ammos;
    }

    public List<String> getAmmos() {
        return ammos;
    }
}
