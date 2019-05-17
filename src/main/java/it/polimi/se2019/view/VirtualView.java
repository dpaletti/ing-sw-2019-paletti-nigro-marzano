package it.polimi.se2019.view;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.utility.*;
import it.polimi.se2019.network.*;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class VirtualView extends Observable<VCEvent> implements Observer<MVEvent> {
    private List<Connection> connections = new CopyOnWriteArrayList<>();

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private MVEventDispatcher dispatcher = new Dispatcher();

    private final BiSet<String, String> biTokenUsername = new BiSet<>(); //Pair<"token", "username">

    private boolean isMatchMaking = true;



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
                Log.fine("Here I am");
                biTokenUsername.add(new Pair<>(message.getDestination(), message.getUsername()));
                sem.release();
        }
        @Override
        public void update(UsernameDeletionEvent message) {
            if(biTokenUsername.containsFirst(message.getDestination())){
                //disconnection after username selection
                String username = biTokenUsername.getSecond(message.getDestination());
                biTokenUsername.remove(new Pair<>(message.getDestination(), username));
            }else
                //disconnection during username choice
                sem.release();
            Connection connection = getConnectionOnId(message.getDestination());
            connections.remove(connection);
            Log.fine("Deleted player");
        }

        @Override
        public void update(HandshakeEndEvent message){
            Log.fine("Sending handshake end event");
            submit(getConnectionOnId(message.getDestination()), message);
        }

        @Override
        public void update(MatchMakingEndEvent message){
            isMatchMaking = false;
            submit(new ConnectionBroadcast(connections), message);
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
        }
    }

    @Override
    public void update(MVEvent message) {
        try {
            if(biTokenUsername.containsSecond(message.getDestination()))
                //virtual view changes usernames in tokens while sending
                //connections are identified by tokens

                message.setDestination(biTokenUsername.getFirst(message.getDestination()));
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            Log.severe(e.getMessage());
        }
    }


    private void retrieve(Connection connection){
        //VirtualView changes token in usernames while receiving
        //players are identified by usernames

        VCEvent vcEvent = connection.retrieve();
        Log.fine(observers.toString());
        if(biTokenUsername.containsFirst(vcEvent.getSource()))
            vcEvent.setSource(biTokenUsername.getSecond(vcEvent.getSource()));
        notify(vcEvent);
    }

    private void submit(Connection connection, MVEvent event){
        connection.submit(event);
    }


    public void startListening (Connection connection){
        if(!isMatchMaking){
                //TODO
        }
        Log.fine("Start listening on token: " + connection.getToken());
        try {
            //TODO try a login screen and move wait on another thread
            sem.acquire();
        } catch (InterruptedException e) {
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
            connections.add(connection);
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
        Log.fine("Getting connection  with id: " + id);
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
