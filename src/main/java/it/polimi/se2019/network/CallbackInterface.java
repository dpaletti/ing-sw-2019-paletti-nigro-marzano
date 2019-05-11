package it.polimi.se2019.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackInterface extends Remote {
    void setToken(String token) throws RemoteException;
    String ping() throws RemoteException;
    void listenToEvent() throws RemoteException;
}
