package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.client.view.MVEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This event notifies all users of the available configurations and of the players connected.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class MatchConfigurationEvent extends MVEvent {
    private ArrayList<String> configurations; //small, medium_left, medium_right, large
    private ArrayList<String> connectedPlayers;
    private boolean isReconnection;

    public MatchConfigurationEvent(String destination, List<String> configurations) {
        super(destination);
        this.configurations = new ArrayList<>(configurations);
        isReconnection = false;
    }

    public MatchConfigurationEvent(String destination, List<String> configurations, List<String> connectedPlayers ,boolean isReconnection) {
        super(destination);
        this.configurations = new ArrayList<>(configurations);
        this.isReconnection = isReconnection;
        this.connectedPlayers = new ArrayList<>(connectedPlayers);
    }

    public ArrayList<String> getConnectedPlayers() {
        return connectedPlayers;
    }

    public boolean isReconnection() {
        return isReconnection;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public List<String> getConfigurations() {
        return configurations;
    }
}
