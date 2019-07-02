package it.polimi.se2019.commons.network;

import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.client.view.VCEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
    void startListening(CallbackInterface callbackInterface) throws RemoteException;
    MVEvent pullEvent(String token, int playerRoomNumber) throws RemoteException;
    void pushEvent(String token, VCEvent vcEvent, int playerRoomNumber) throws RemoteException;
}
