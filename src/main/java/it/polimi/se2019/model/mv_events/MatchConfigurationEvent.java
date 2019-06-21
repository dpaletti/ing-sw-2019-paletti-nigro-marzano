package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

//ask all players their preferred configuration, whether final frenzy= true and the number of skulls (5 to 8)

public class MatchConfigurationEvent extends MVEvent {
    private ArrayList<String> configurations; //small, medium_left, medium_right, large

    public MatchConfigurationEvent(String destination, List<String> configurations) {
        super(destination);
        this.configurations = new ArrayList<>(configurations);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public List<String> getConfigurations() {
        return configurations;
    }
}
