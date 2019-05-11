package it.polimi.se2019.network;

import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;

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

}
