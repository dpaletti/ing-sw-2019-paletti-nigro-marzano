package it.polimi.se2019.network;

import it.polimi.se2019.utility.*;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.JoinEvent;

import java.io.Serializable;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NetworkHandlerRMI extends NetworkHandler implements CallbackInterface {
    private transient ServerInterface gameServer;
    private transient Dispatcher dispatcher = new Dispatcher();

    public NetworkHandlerRMI(){
        super();
        try {
            Registry importRegistry = LocateRegistry.getRegistry();
            gameServer = (ServerInterface) importRegistry.lookup(Settings.REMOTE_SERVER_NAME);

           //TODO test remote call not on localhost
            UnicastRemoteObject.exportObject(this, 0);

            gameServer.startListening(this);
        }catch (RemoteException e){
            Log.severe("Could not get RMI registry " + e.getMessage());
        }catch (NotBoundException e) {
            Log.severe("Could not bind " + Settings.REMOTE_SERVER_NAME);
        }
    }


    public NetworkHandlerRMI(String token){
        super(token);
        try {
            Registry importRegistry = LocateRegistry.getRegistry();
            gameServer = (ServerInterface) importRegistry.lookup(Settings.REMOTE_SERVER_NAME);
            gameServer.startListening(this);
        }catch (RemoteException e){
            Log.severe("Could not get RMI registry " + e.getMessage());
        }catch (NotBoundException e) {
            Log.severe("Could not bind " + Settings.REMOTE_SERVER_NAME);
        }
    }

    @Override
    public void setToken(String token){
        if (this.token == (null)) {
            this.token = token;
            listenToEvent();
        }
    }

    private class Dispatcher extends VCEventDispatcher implements Serializable {


        @Override
        public void update(JoinEvent message){
            submit(message);
        }

    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            Log.severe(e.getMessage());
        }
    }


    @Override
    public void submit(VCEvent vcEvent) {
        try {
            Log.fine("submitted JSON: " + vcEvent);
            gameServer.pushEvent(token, vcEvent);
        }catch(Exception e) {
            Log.severe(e.getMessage());
        }
    }

    @Override
    public void retrieve() {
        try {
            notify(gameServer.pullEvent(token));
        }catch (RemoteException e) {
            Log.severe("Cannot pull event " + e.getMessage());
            Log.severe("Waiting for godot");
            try {
                while(!Thread.currentThread().isInterrupted())
                    wait();
            }catch (InterruptedException ex){
                Log.severe(ex.getMessage());
                Thread.currentThread().interrupt();
            }

        }
    }

    @Override
    public void listenToEvent() {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Log.fine("Listening");
                retrieve();
            }
        }).start();
    }

    @Override
    public String ping() throws RemoteException {
        return "Still alive";
    }
}
