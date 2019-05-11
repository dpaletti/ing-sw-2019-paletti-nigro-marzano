package it.polimi.se2019.view;

import it.polimi.se2019.model.mv_events.HandshakeEndEvent;
import it.polimi.se2019.model.mv_events.JoinMatchMakingEvent;
import it.polimi.se2019.model.mv_events.MatchMakingEndEvent;
import it.polimi.se2019.utility.*;
import it.polimi.se2019.model.mv_events.UsernameDeletionEvent;
import it.polimi.se2019.network.*;
import it.polimi.se2019.view.vc_events.DisconnectionEvent;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class VirtualView extends View {
    private List<Connection> connections = new CopyOnWriteArrayList<>();
    private List<Connection> timeOuts = new CopyOnWriteArrayList<>();

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private MVEventDispatcher dispatcher = new Dispatcher();

    private BiSet<String, String> biTokenUsername = new BiSet<>(); //Pair<"token", "username">


    public VirtualView(){
        super();
    }

    public List<Connection> getConnections() {
        return new ArrayList<>(connections);
    }

    public List<Connection> getTimeOuts(){
        return new ArrayList<>(timeOuts);
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
                    VirtualView.this.notify(new DisconnectionEvent(biTokenUsername.getSecond(connection.getToken())));
                    shutdown = true;
                }
            }
        }
    }

    private class Dispatcher extends MVEventDispatcher{

        @Override
        public void update(JoinMatchMakingEvent message) {
            biTokenUsername.add(new Pair<>(message.getDestination(), message.getUsername()));
            Log.fine(biTokenUsername.toString());
        }
        @Override
        public void update(UsernameDeletionEvent message) {
            Connection connection = getConnectionOnId(message.getDestination(), connections);
            connections.remove(connection);
            Log.fine("Deleted player");
        }

        @Override
        public void update(HandshakeEndEvent message){
            submit(getConnectionOnId(message.getDestination(), connections), message);
        }
        @Override
        public void update(MatchMakingEndEvent message){
            submit(new ConnectionBroadcast(connections), message);
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
        if(biTokenUsername.containsFirst(vcEvent.getSource()))
            vcEvent.setSource(biTokenUsername.getSecond(vcEvent.getSource()));
        notify(vcEvent);
    }

    private void submit(Connection connection, MVEvent event){
        connection.submit(event);
    }


    public void startListening (Connection connection){
        connections.add(connection);
        executorService.submit(new EventLoop(connection));
        ArrayList<String> usernames = new ArrayList<>();
        for (Pair<String, String> s:
             biTokenUsername) {
            usernames.add(s.getSecond());
        }
        submit(connection, new HandshakeEndEvent(connection.getToken(), usernames));
    }


    public Connection getConnectionOnId(String id, List<Connection> toQuery){
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
