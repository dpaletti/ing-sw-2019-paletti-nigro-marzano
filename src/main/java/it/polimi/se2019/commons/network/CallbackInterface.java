package it.polimi.se2019.commons.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CallbackInterface extends Remote {
    void setRoomNumber(int roomNumber) throws RemoteException;
    void setToken(String token) throws RemoteException;
    String ping() throws RemoteException;
    void listenToEvent() throws RemoteException;
}
