package it.polimi.se2019.network;

import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionRMI implements Connection{
    private SynchronousQueue<String> in = new SynchronousQueue<>();
    private SynchronousQueue<String> out = new SynchronousQueue<>();
    private CallbackInterface gameClient;
    private boolean isConnected = true;
    //TODO manage disconnections through threads and waits
    //or maybe with client callbacks
    Timer timer;

    private String token = null;

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
    public void setToken(String token) throws RemoteException {
        try {
            this.token = token;
            gameClient.setToken(token);
        }catch (RemoteException e){
            throw new RemoteException("Error while setting token: " + e.getMessage(), e);
        }
    }

    @Override
    public synchronized String retrieve() {
        try {
            return in.take();
        }catch (InterruptedException e){
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public void submit(String data) {
        try {
            out.put(data);
        }catch (InterruptedException e){
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public String pull(){
        try {
            return out.take();
        }catch (InterruptedException e){
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public void push(String data){
        try {
            in.put(data);
        }catch (InterruptedException e){
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void stopPing(){
        timer.cancel();
        timer.purge();
    }

    public void timeOutCheck(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try{
                    gameClient.ping();
                }catch (RemoteException e) {
                    DisconnectionEvent event = new DisconnectionEvent(token);
                    stopPing();
                    push(JsonHandler.serialize(event, event.getClass().toString().replace("class ", "")));
                }
            }
        }, 0, 1000);
    }
}
