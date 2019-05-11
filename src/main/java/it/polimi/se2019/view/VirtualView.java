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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class VirtualView extends View {
    private List<Connection> connections = new CopyOnWriteArrayList<>();
    private List<Connection> timeOuts = new CopyOnWriteArrayList<>();

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private MVEventDispatcher dispatcher = new Dispatcher();

    private final BiSet<String, String> biTokenUsername = new BiSet<>(); //Pair<"token", "username">

    private Semaphore sem = new Semaphore(1, true);

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
                    if(biTokenUsername.containsFirst(connection.getToken()))
                        VirtualView.this.notify(new DisconnectionEvent(biTokenUsername.getSecond(connection.getToken())));
                    else
                        //TODO further inspect disconnection on username choiche
                            //may need to save refence to eventLoop
                        //disconnection while choosing username on Socket
                        VirtualView.this.notify(new DisconnectionEvent(connection.getToken()));
                    shutdown = true;
                }
            }
        }
    }

    private class Dispatcher extends MVEventDispatcher{

        @Override
        public void update(JoinMatchMakingEvent message) {
                Log.fine("Here I am");
                biTokenUsername.add(new Pair<>(message.getDestination(), message.getUsername()));
                sem.release();
        }
        @Override
        public void update(UsernameDeletionEvent message) {
            Connection connection = getConnectionOnId(message.getDestination(), connections);
            connections.remove(connection);
            String username = biTokenUsername.getSecond(message.getDestination());
            biTokenUsername.remove(new Pair<>(connection.getToken(), username));
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
        Log.fine(observers.toString());
        if(biTokenUsername.containsFirst(vcEvent.getSource()))
            vcEvent.setSource(biTokenUsername.getSecond(vcEvent.getSource()));
        notify(vcEvent);
    }

    private void submit(Connection connection, MVEvent event){
        connection.submit(event);
    }


    public void startListening (Connection connection){
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }
            connections.add(connection);
            executorService.submit(new EventLoop(connection));
            ArrayList<String> usernames = new ArrayList<>();
            for (Pair<String, String> s :
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
