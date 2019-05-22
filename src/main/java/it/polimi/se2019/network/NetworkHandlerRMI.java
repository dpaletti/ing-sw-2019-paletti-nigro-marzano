package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.VcJoinEvent;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NetworkHandlerRMI extends NetworkHandler implements CallbackInterface {
    private transient ServerInterface gameServer;

    private transient Dispatcher dispatcher = new Dispatcher();


    public NetworkHandlerRMI(Client client){
        super(client);
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


    public NetworkHandlerRMI(String token, Client client){
        super(token, client);
        try {
            Registry importRegistry = LocateRegistry.getRegistry();
            gameServer = (ServerInterface) importRegistry.lookup(Settings.REMOTE_SERVER_NAME);

            UnicastRemoteObject.exportObject(this, 0);
            gameServer.startListening(this);
        }catch (RemoteException e){
            Log.severe("Could not get RMI registry " + e.getMessage());
        }catch (NotBoundException e) {
            Log.severe("Could not bind " + Settings.REMOTE_SERVER_NAME);
        }
    }

    private class Dispatcher extends VCEventDispatcher implements Serializable {


        @Override
        public void update(VcJoinEvent message){
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
        }catch(RemoteException e) {
            Log.severe("Server just disconnected");
        }catch (NullPointerException e){
            Log.severe("Null pointer on RMI submit, game server: " + e.getMessage());
        }
    }

    @Override
    public void retrieve() {
        try {
            Log.fine("retrieving on: " + observers);
            notify(gameServer.pullEvent(token));
        }catch (RemoteException e) {
            Log.severe("Server just disconnected");
            System.exit(0);
        }
    }

    @Override
    public void listenToEvent() {
        listener = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                retrieve();
            }
        });
        listener.start();
    }

    @Override
    public String ping() throws RemoteException {
        return "Still alive";
    }
}
