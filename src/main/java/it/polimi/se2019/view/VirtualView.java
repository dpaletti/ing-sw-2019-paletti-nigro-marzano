package it.polimi.se2019.view;

import it.polimi.se2019.utility.*;
import it.polimi.se2019.model.mv_events.MatchMakingEndEvent;
import it.polimi.se2019.model.mv_events.UsernameDeletionEvent;
import it.polimi.se2019.model.mv_events.UsernameEvaluationEvent;
import it.polimi.se2019.network.*;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualView extends View implements ServerInterface {
    private List<Connection> connections = new CopyOnWriteArrayList<>();
    private List<Connection> timeOuts = new CopyOnWriteArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private MVEventDispatcher dispatcher = new Dispatcher();
    private static final String PREFIX = "class ";
    private BiSet<String, String> biTokenUsername = new BiSet<>(); //Pair<"token", "username">

    public VirtualView(){
        super();
        try {
            UnicastRemoteObject.exportObject(this, 0);
        }catch(RemoteException e){
            Log.severe("Remote Exception while exporting ServerInterface: " + e.getMessage());
        }
    }

    public List<Connection> getConnections() {
        return new ArrayList<>(connections);
    }

    public List<Connection> getTimeOuts(){
        return new ArrayList<>(timeOuts);
    }

    private class EventLoop implements Runnable{
        Connection connection;

        private EventLoop(Connection c){
            this.connection = c;
        }

        private boolean shutdown = false;

        @Override
        public void run() {
            while (!shutdown) {
                try {
                    retrieve(connection);
                } catch (NoSuchElementException e) {
                    VirtualView.this.notify(new DisconnectionEvent(biTokenUsername.getSecond(connection.getToken())));
                    shutdown = true;
                }
            }
        }
    }

    private class Dispatcher extends MVEventDispatcher{

        @Override
        public void update(UsernameEvaluationEvent message){
            try{
                Connection connection = getConnectionOnId(message.getDestination(), timeOuts);
                if(message.isValidUsername()) {
                    String token = generateToken();
                    connection.setToken(token);
                    biTokenUsername.add(new Pair<>(connection.getToken(), message.getDestination()));
                    connections.add(connection);
                    timeOuts.remove(connection);
                }
                submit(connection, JsonHandler.serialize(message, message.getClass().toString().replace(PREFIX, "")));
            }catch (NullPointerException e){
                Log.severe("Trying to notify over a non-existent connection about username");
            }catch (RemoteException e){
                Log.severe("Remote exception while handling Username evaluatioin event");
            }
        }

        @Override
        public void update(UsernameDeletionEvent message) {
            Connection connection = getConnectionOnId(message.getDestination(), connections);
            timeOuts.remove(connection);
            Log.fine("Deleted player");
        }

        @Override
        public void update(MatchMakingEndEvent message) {
            submit(getConnectionOnId(message.getDestination(), connections), JsonHandler.serialize(message, message.getClass().toString().replace(PREFIX, "")));
        }
    }

    @Override
    public void update(MVEvent message) {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            Log.severe(e.getMessage());
        }
    }


    private void retrieve(Connection connection){
        try {
            String data = connection.retrieve();
            notify((VCEvent) JsonHandler.deserialize(data));
        }catch (ClassNotFoundException e) {
            Log.severe("Error during deserialization: " + e.getMessage());
            System.exit(0);
        }
    }

    private void submit(Connection connection, String data){
        connection.submit(data);
    }


    public void startListening (Connection connection){
        timeOuts.add(connection); //until the join request, connected clients are considered time-outed
        EventLoop eventLoop= new EventLoop(connection);
        executorService.submit(eventLoop);
    }

    //------------------------RMI REMOTE SERVER INTERFACE IMPLEMENTATION------------------------//

    @Override
    public void startListening(String username, CallbackInterface client) {
        Connection connection = new ConnectionRMI(username,  client);
        startListening(connection);
    }

    @Override
    public String pullEvent(String token) throws RemoteException {
        try {
            ConnectionRMI connection;
            if(!biTokenUsername.containsFirst(token))
                connection = (ConnectionRMI) getConnectionOnId(token, timeOuts);
            else
                connection = (ConnectionRMI) getConnectionOnId(token, connections);
            return connection.pull();
        } catch (Exception e) {
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
            System.exit(0);
        }
        return null;
    }

    @Override
    public void pushEvent(String token, String data) throws RemoteException {
        try{
            ConnectionRMI connection;
            if(!biTokenUsername.containsFirst(token))
                connection = (ConnectionRMI) getConnectionOnId(token, timeOuts);
            else
                connection = (ConnectionRMI) getConnectionOnId(token, connections);
            connection.push(data);
        }catch (Exception e){
            Log.severe(e.getMessage());
            System.exit(0);
        }
    }

    //------------------------RMI REMOTE SERVER INTERFACE IMPLEMENTATION------------------------//

    private Connection getConnectionOnId(String id, List<Connection> toQuery){
        Log.fine("Getting connection  with id: " + id);
        if(id.equals("*"))
            return new ConnectionBroadcast(connections);

        for (Connection c:
             toQuery) {
            if(c.getToken().equals(id))
                return c;
        }
        throw new NullPointerException("Did not find any connection with " + id + " as identification");
    }

    public String generateToken(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        String token = Base64.getEncoder().encodeToString(bytes);
        if(biTokenUsername.containsFirst(token))
            generateToken();
        return token;
    }
    
}
