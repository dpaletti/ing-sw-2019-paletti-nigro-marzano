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
    public void setId(String username) {
        throw new UnsupportedOperationException("Cannot set id on broadcast connection");
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException("Cannot get password on broadcast connection");
    }

    @Override
    public void setPassword(String password) {
        throw new UnsupportedOperationException("Cannot set password on broadcast connection");
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException("Cannot get id on broadcast connection");
    }
}
