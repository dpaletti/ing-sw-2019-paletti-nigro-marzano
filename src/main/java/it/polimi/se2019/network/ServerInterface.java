package it.polimi.se2019.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
    void startListening(String username, String password) throws RemoteException;
    String pullEvent(String username) throws RemoteException;
    void pushEvent(String username, String data) throws RemoteException;
    void ping(String username) throws RemoteException;
}
