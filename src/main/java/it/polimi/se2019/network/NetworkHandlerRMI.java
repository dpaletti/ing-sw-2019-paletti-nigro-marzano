package it.polimi.se2019.network;

import it.polimi.se2019.utility.*;
import it.polimi.se2019.view.vc_events.HandshakeEndEvent;
import it.polimi.se2019.view.vc_events.JoinEvent;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;

import java.io.Serializable;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NetworkHandlerRMI extends NetworkHandler implements CallbackInterface {
    private transient ServerInterface gameServer;
    private transient Dispatcher dispatcher = new Dispatcher();

    public NetworkHandlerRMI(String username){
        super(username);
        try {
            Registry importRegistry = LocateRegistry.getRegistry();
            gameServer = (ServerInterface) importRegistry.lookup(Settings.REMOTE_SERVER_NAME);

            UnicastRemoteObject.exportObject(this, 4000);

            gameServer.startListening(username, this);
            token = username;
            listenToEvent();
            enterRoom();
        }catch (RemoteException e){
            Log.severe("Could not get RMI registry " + e.getMessage());
        }catch (NotBoundException e) {
            Log.severe("Could not bind " + Settings.REMOTE_SERVER_NAME);
        }
    }


    public NetworkHandlerRMI(String username, String token){
        super(username, token);
        try {
            Registry importRegistry = LocateRegistry.getRegistry();
            gameServer = (ServerInterface) importRegistry.lookup(Settings.REMOTE_SERVER_NAME);
            gameServer.startListening(username, this);
            listenToEvent();
            enterRoom();
        }catch (RemoteException e){
            Log.severe("Could not get RMI registry " + e.getMessage());
        }catch (NotBoundException e) {
            Log.severe("Could not bind " + Settings.REMOTE_SERVER_NAME);
        }
    }

    @Override
    public void setToken(String token) {
        this.token = token;
        Log.fine("Token is set to: " + token);
    }

    private class Dispatcher extends VCEventDispatcher implements Serializable {

        @Override
        public void update(JoinEvent message){
            submit(JsonHandler.serialize(message, message.getClass().toString().replace("class ", "")));
        }

        @Override
        public void update(HandshakeEndEvent message) {
        }
    }

    @Override
    protected void enterRoom() {
        Log.info("Entering match making");
        update(new JoinEvent(username, username));
        //RMI connection knows its token when sending joinEven
        //repetition is used to hold the generalization over socket connections
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
    public void submit(String toVirtualView) {
        try {
            Log.fine("submitted JSON: " + toVirtualView);
            gameServer.pushEvent(token, toVirtualView);
        }catch(Exception e) {
            Log.severe(e.getMessage());
        }
    }

    @Override
    public void retrieve() {
        try {
            String data = gameServer.pullEvent(token);
            if(data != null)
                notify((MVEvent) JsonHandler.deserialize(data));
        }catch (RemoteException e){
            Log.severe("Cannot pull event " + e.getMessage());
            System.exit(0);
        }catch (ClassNotFoundException e){
            Log.severe("Cannot deserialize event while pulling " + e.getMessage());
        }
    }

    @Override
    protected void listenToEvent() {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                //TODO this will be restructured to terminate on EndEvent (that is the closing event that server sends to client)
                retrieve();
            }
        }).start();
    }

    @Override
    public String ping() throws RemoteException {
        return "Still alive";
    }
}
