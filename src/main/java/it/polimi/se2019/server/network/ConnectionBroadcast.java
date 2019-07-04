package it.polimi.se2019.server.network;

import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.client.view.VCEvent;

import java.util.List;

public class ConnectionBroadcast implements Connection{
    private List<Connection> connectionList;

    public ConnectionBroadcast(List<Connection> connectionList){
        this.connectionList = connectionList;
    }

    @Override
    public void submit(MVEvent mvEvent) {
        connectionList.forEach(connection -> connection.submit(mvEvent));
    }

    @Override
    public VCEvent retrieve() {
        throw new UnsupportedOperationException("Cannot retrieve on broadcast connection");
    }


    @Override
    public String getToken() {
        throw new UnsupportedOperationException("Cannot get token on broadcast connection");
    }

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("Cannot disconnect a broadcast connection");
    }

    @Override
    public void reconnect(MVEvent reconnectionEvent, int roomNumber) {
        throw new UnsupportedOperationException("Cannot reconnect a broadcast connection");
    }

    @Override
    public boolean isDisconnected() {
        throw new UnsupportedOperationException("Broadcast connection cannot be disconnected");
    }
}
