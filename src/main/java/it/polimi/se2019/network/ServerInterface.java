package it.polimi.se2019.network;

import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{
    void startListening(CallbackInterface callbackInterface) throws RemoteException;
    MVEvent pullEvent(String token) throws RemoteException;
    void pushEvent(String token, VCEvent vcEvent) throws RemoteException;
}
