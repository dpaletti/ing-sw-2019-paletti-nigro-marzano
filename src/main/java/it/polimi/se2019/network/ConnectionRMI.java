package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;

import java.rmi.RemoteException;
import java.util.concurrent.SynchronousQueue;

public class ConnectionRMI implements Connection{
    private SynchronousQueue<VCEvent> in = new SynchronousQueue<>();
    private SynchronousQueue<MVEvent> out = new SynchronousQueue<>();
    private boolean remoteClientRetrieving = false;

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
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public void submit(MVEvent mvEvent) {
        try {
            if(!remoteClientRetrieving){
                gameClient.setToken(token);
                gameClient.listenToEvent();
                remoteClientRetrieving = true;
            }
            out.put(mvEvent);
        }catch (InterruptedException e){
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }catch (RemoteException e){
            Log.severe("Cannot start remote event loop");
        }
    }

    public MVEvent pull(){
        try {
            return out.take();
        }catch (InterruptedException e){
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public void push(VCEvent vcEvent){
        try {
            in.put(vcEvent);
        }catch (InterruptedException e){
            Log.severe(e.getMessage());
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
            }catch (InterruptedException e){
                Log.severe("Interrupted");
                Thread.currentThread().interrupt();
            }
        }).start();

    }
}
