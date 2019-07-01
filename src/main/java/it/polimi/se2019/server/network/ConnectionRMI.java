package it.polimi.se2019.server.network;

import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.mv_events.SyncEvent;
import it.polimi.se2019.commons.network.CallbackInterface;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.vc_events.DisconnectionEvent;

import java.rmi.RemoteException;
import java.util.concurrent.SynchronousQueue;

public class ConnectionRMI implements Connection{
    private SynchronousQueue<VCEvent> in = new SynchronousQueue<>();
    private SynchronousQueue<MVEvent> out = new SynchronousQueue<>();
    private boolean remoteClientRetrieving = false;
    private boolean disconnected = false;


    private CallbackInterface gameClient;

    private String token;

    public ConnectionRMI(String token, CallbackInterface gameClient) {
        this.token = token;
        this.gameClient = gameClient;
        timeOutCheck();
    }


    public CallbackInterface getGameClient() {
        return gameClient;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public synchronized VCEvent retrieve() {
        try {
            return in.take();
        }catch (InterruptedException e){
            Log.severe("Retrieving interrupted");
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public void reconnect(SyncEvent sync) {
        disconnected = false;
        submit(sync);
    }

    @Override
    public void submit(MVEvent mvEvent) {
        try {
            if(!disconnected) {
                if (!remoteClientRetrieving) {
                    gameClient.setToken(token);
                    gameClient.listenToEvent();
                    remoteClientRetrieving = true;
                }
                out.put(mvEvent);
            }
        }catch (InterruptedException e){
            Log.severe("Submitting interrupted");
            submit(mvEvent);
        }catch (RemoteException e){
            Log.severe("Cannot start remote event loop");
        }
    }

    @Override
    public void disconnect() {
        disconnected = true;
    }

    @Override
    public boolean isDisconnected() {
        return disconnected;
    }

    public MVEvent pull(){
        try {
            return out.take();
        }catch (InterruptedException e){
            Log.severe("Pulling interrupted");
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public void push(VCEvent vcEvent){
        try {
            in.put(vcEvent);
        }catch (InterruptedException e){
            Log.severe("Pushing interrupted");
            Thread.currentThread().interrupt();
        }
    }

    public void timeOutCheck(){
        new Thread(() -> {
            try {
                while(!Thread.currentThread().isInterrupted()) {
                    gameClient.ping();
                    Thread.sleep(1000);
                }
            }catch (RemoteException e){
                push(new DisconnectionEvent(token));
                disconnect();
            }catch (InterruptedException e){
                Log.severe("Interrupted");
                Thread.currentThread().interrupt();
            }
        }).start();

    }

    @Override
    public String toString() {
        return ("{Connection type: " + "RMI " +
                "Connection token: " + token + "}");
    }
}
