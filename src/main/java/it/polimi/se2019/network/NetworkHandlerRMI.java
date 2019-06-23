package it.polimi.se2019.network;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.VcJoinEvent;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NetworkHandlerRMI extends NetworkHandler implements CallbackInterface {
    private transient ServerInterface gameServer;

    public NetworkHandlerRMI(Client client){
        super(client);
        try {
            Registry importRegistry = LocateRegistry.getRegistry();
            gameServer = (ServerInterface) importRegistry.lookup(client.getRemoteServerName());
           //TODO test remote call not on localhost

            UnicastRemoteObject.exportObject(this, 0);

            gameServer.startListening(this);
        }catch (RemoteException e){
            Log.severe("Could not get RMI registry " + e.getMessage());
        }catch (NotBoundException e) {
            Log.severe("Could not bind " + client.getRemoteServerName());
        }
    }


    public NetworkHandlerRMI(String token, Client client){
        super(token, client);
        try {
            Registry importRegistry = LocateRegistry.getRegistry();
            gameServer = (ServerInterface) importRegistry.lookup(client.getRemoteServerName());

            UnicastRemoteObject.exportObject(this, 0);
            gameServer.startListening(this);
        }catch (RemoteException e){
            Log.severe("Could not get RMI registry " + e.getMessage());
        }catch (NotBoundException e) {
            Log.severe("Could not bind " + client.getRemoteServerName());
        }
    }

    @Override
    public void dispatch(VcJoinEvent message){
        submit(message);
    }


    @Override
    public void update(Event message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.fine("Ignored " + e.getMessage());
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
