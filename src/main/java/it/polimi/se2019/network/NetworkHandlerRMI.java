package it.polimi.se2019.network;

import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.JoinEvent;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.View;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NetworkHandlerRMI extends NetworkHandler{
    private Thread listener;
    private ServerInterface gameServer;

    public NetworkHandlerRMI(String u, String p, View view){
        super(u, p, view);
        try {
            Registry importRegistry = LocateRegistry.getRegistry();
            gameServer = (ServerInterface) importRegistry.lookup(RMIRemoteObjects.REMOTE_SERVER_NAME);
            gameServer.register(username, password);
            listenToEvent();
            startPinging();
            enterMatchMaking();

        }catch (RemoteException e){
            Log.severe("Could not get RMI registry " + e.getMessage());
        }catch (NotBoundException e) {
            Log.severe("Could not bind " + RMIRemoteObjects.REMOTE_SERVER_NAME);
        }
    }

    @Override
    protected void enterMatchMaking() {
        Log.info("Entering match making");
        update(new JoinEvent(username, password, username));
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.severe(e.getMessage());
        }
    }

    public void update(JoinEvent message){
        submit(JsonHandler.serialize(message, message.getClass().toString().replace("class ", "")));
    }

    @Override
    public void submit(String toVirtualView) {
        try {
            Log.fine("submitted JSON: " + toVirtualView);
            gameServer.pushEvent(toVirtualView);
        }catch(Exception e) {
            Log.severe(e.getMessage());
        }
    }

    @Override
    public void retrieve() throws ClassNotFoundException {
        try {
            notify((MVEvent) JsonHandler.deserialize(gameServer.pullEvent()));
        }catch (RemoteException e){
            Log.severe("Cannot pull event " + e.getMessage());
            System.exit(0);
        }catch (Exception e){
            Log.severe(e.getMessage());
        }
    }

    @Override
    protected void listenToEvent() {
        listener = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        //TODO this will be restructured to terminate on EndEvent (that is the closing event that server sends to client)
                        retrieve();
                    } catch (ClassNotFoundException e) {
                        Log.severe("Error during deserialization of a MVEvent" + e.getMessage());
                    }
                }
        });
        listener.start();
    }

    private void startPinging(){
        new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    gameServer.ping();
                    Thread.sleep(100);
                    //TODO make it configurable and compliant with timeout
                }
            }catch (RemoteException e){
                Log.severe("Server is down" + e.getMessage());
            }catch (InterruptedException e){
                Log.severe(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
