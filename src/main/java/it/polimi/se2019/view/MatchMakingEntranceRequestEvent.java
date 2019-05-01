package it.polimi.se2019.view;

import it.polimi.se2019.controller.MatchMakingController;
import it.polimi.se2019.network.ConnectionType;

import java.net.InetAddress;

public class MatchMakingEntranceRequestEvent extends VCEvent {
    private ConnectionType connectionType;

    public MatchMakingEntranceRequestEvent(ConnectionType connectionType, InetAddress ip){
        super(ip);
        this.connectionType = connectionType;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }
    @Override
    public void handle(MatchMakingController controller) {
        controller.update(this);
    }
}
