package it.polimi.se2019.view.gui_events;

import it.polimi.se2019.view.UiDispatcher;

import java.util.ArrayList;
import java.util.List;

public class UiMatchSetup extends UiEvent {

    private List<String> configuration;

    public UiMatchSetup(List<String> configuration){
        this.configuration = configuration;
    }

    public List<String> getConfiguration() {
        return new ArrayList<>(configuration);
    }

    public void handle(UiDispatcher uiDispatcher){
        uiDispatcher.dispatch(this);
    }
}
