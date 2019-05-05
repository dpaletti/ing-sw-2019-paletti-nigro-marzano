package it.polimi.se2019.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
    void register() throws RemoteException;
    String pullEvent() throws RemoteException;
    void pushEvent(String data) throws RemoteException;
    void ping() throws RemoteException;
}
