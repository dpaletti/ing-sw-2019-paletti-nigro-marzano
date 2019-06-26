package it.polimi.se2019.view.ui_events;

import it.polimi.se2019.view.UiDispatcher;

import java.util.ArrayList;
import java.util.List;

public class UiMapConfigEvent extends UiEvent {
    private ArrayList<String> configs;

    public UiMapConfigEvent(List<String> configs){
        this.configs = new ArrayList<>(configs);
    }


    public List<String> getConfigs() {
        return configs;
    }

    @Override
    public void handle(UiDispatcher uiDispatcher) {
        uiDispatcher.dispatch(this);
    }
}
