package it.polimi.se2019.view;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Connection;
import it.polimi.se2019.network.ConnectionBroadcast;
import it.polimi.se2019.utility.*;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class VirtualView extends Observable<VCEvent> implements Observer<MVEvent> {
    private List<Connection> connections = new CopyOnWriteArrayList<>();

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private MVEventDispatcher dispatcher = new Dispatcher();

    private final BiSet<String, String> biTokenUsername = new BiSet<>(); //Pair<"token", "username">

    private Semaphore sem = new Semaphore(1, true);

    public VirtualView(){
        observers = new ArrayList<>();
    }

    public List<Connection> getConnections() {
        return new ArrayList<>(connections);
    }


    public BiSet<String, String> getBiTokenUsername() {
        return biTokenUsername;
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
                    if(biTokenUsername.containsFirst(connection.getToken()))
                        VirtualView.this.notify(new DisconnectionEvent(biTokenUsername.getSecond(connection.getToken())));
                    else
                        VirtualView.this.notify(new DisconnectionEvent(connection.getToken()));
                    shutdown = true;
                }
            }
        }
    }

    private class Dispatcher extends MVEventDispatcher{

        @Override
        public void update(MvJoinEvent message) {
            biTokenUsername.add(new Pair<>(message.getDestination(), message.getUsername()));
            List<Connection> toBroadcast = new ArrayList<>(connections);
            toBroadcast.remove(getConnectionOnId(message.getDestination()));
            ConnectionBroadcast connection = new ConnectionBroadcast(toBroadcast);
            connection.submit(message);
            sem.release();
            Log.fine("Semaphore released");
        }
        @Override
        public void update(UsernameDeletionEvent message) {
            Log.fine("Deleting " + message.getUsername());
            if(biTokenUsername.containsSecond(message.getUsername())) {
                connections.remove(getConnectionOnId(biTokenUsername.getFirst(message.getUsername())));
                biTokenUsername.removeSecond(message.getUsername());
                submit(getConnectionOnId(message.getDestination()), message);
            }
            else {
                connections.remove(connections.get(connections.size() - 1));
                //disconnection during username choice
                sem.release();
                Log.fine("Semaphore released");
            }
        }

        @Override
        public void update(MvReconnectionEvent message) {
            Log.fine("handling reconnection " + message);
            Connection connection = getConnectionOnId(message.getDestination());
            Connection oldConnection = getConnectionOnId(message.getOldToken());
            String username;
            try {
                username = biTokenUsername.getSecond(message.getOldToken());
            }catch (NullPointerException e){
                submit(connection, new ConnectionRefusedEvent(message.getDestination(), "You got an invalid token"));
                sem.release();
                Log.fine("Semaphore released");
                return;
            }
            Log.severe("Here I am");
            biTokenUsername.remove(new Pair<>(message.getOldToken(), username));
            if(message.isMatchMaking()) {
                Log.fine("Refusing connection");
                submit(connection, new ConnectionRefusedEvent(message.getDestination(), "Cannot reconnect during matchmaking"));
                if (!connections.remove(connection)) {
                    Log.severe("Cannot find connection");
                } else if (!connections.remove(oldConnection)) {
                    Log.severe("Cannot find old connection");
                }
            } else{
                connection.reconnect((List<MVEvent>) oldConnection.getBufferedEvents());
                if(connections.remove(oldConnection)){
                    Log.severe("Cannot find oldConnection");
                }
                biTokenUsername.add(new Pair<>(message.getDestination(), username));
                Log.fine(username + " just reconnected");
            }
            sem.release();
            Log.fine("Semaphore released");
        }
    }

    @Override
    public void update(MVEvent message) {
        try {
            if(biTokenUsername.containsSecond(message.getDestination()))
                //virtual view changes usernames in tokens while sending
                //connections are identified by tokens
                //this branch guards against events that already contain tokens

                message.setDestination(biTokenUsername.getFirst(message.getDestination()));
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            Log.fine("Ignoring and submitting: " + message);
            //if an event cannot be handled is submitted to clients by default
            submit(getConnectionOnId(message.getDestination()), message);
        }
    }


    private void retrieve(Connection connection){
        //VirtualView changes token in usernames while receiving
        //players are identified by usernames

        VCEvent vcEvent = connection.retrieve();
        if(biTokenUsername.containsFirst(vcEvent.getSource()))
            vcEvent.setSource(biTokenUsername.getSecond(vcEvent.getSource()));
        notify(vcEvent);
    }

    private void submit(Connection connection, MVEvent event){
        connection.submit(event);
    }


    public void startListening (Connection connection){
        try {
            //TODO try a login screen and move wait on another thread
            sem.acquire();
            Log.fine("Semaphore acquired");
        } catch (InterruptedException e) {
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
            connections.add(connection);
            Log.fine(connections.toString());
            executorService.submit(new EventLoop(connection));
            ArrayList<String> usernames = new ArrayList<>();
            usernames.add("*"); //wildcard username not assignable
            for (Pair<String, String> s :
                    biTokenUsername) {
                usernames.add(s.getSecond());
            }
            submit(connection, new HandshakeEndEvent(connection.getToken(), usernames));
    }


    public Connection getConnectionOnId(String id){
        if(id.equals("*"))
            return new ConnectionBroadcast(connections);

        for (Connection c:
             connections) {
            if(c.getToken().equals(id))
                return c;
        }
        return null;
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
