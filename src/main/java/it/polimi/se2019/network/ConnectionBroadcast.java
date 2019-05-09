package it.polimi.se2019.network;

import java.util.List;

public class ConnectionBroadcast implements Connection{
    private List<Connection> connectionList;

    public ConnectionBroadcast(List<Connection> connectionList){
        this.connectionList = connectionList;
    }

    @Override
    public void submit(String data) {
        connectionList.forEach(connection -> connection.submit(data));
    }

    @Override
    public String retrieve() {
        throw new UnsupportedOperationException("Cannot retrieve on broadcast connection");
    }


    @Override
    public String getToken() {
        throw new UnsupportedOperationException("Cannot get token on broadcast connection");
    }

    @Override
    public void setToken(String password) {
        throw new UnsupportedOperationException("Cannot set token on broadcast connection");
    }

}
