package it.polimi.se2019.server.network;

import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.mv_events.HandshakeEndEvent;
import it.polimi.se2019.commons.mv_events.MvJoinEvent;
import it.polimi.se2019.commons.mv_events.SetUpEvent;
import it.polimi.se2019.commons.mv_events.UsernameDeletionEvent;
import it.polimi.se2019.commons.utility.*;
import it.polimi.se2019.commons.vc_events.DisconnectionEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

public class VirtualView extends Observable<VCEvent> implements Observer<MVEvent>, MVEventDispatcher {
    private List<Connection> connections = new CopyOnWriteArrayList<>();

    private final BiSet<String, String> biTokenUsername = new BiSet<>(); //Pair<"token", "username">

    private final Map<String, Thread> eventLoops = new HashMap<>();

    private Semaphore sem = new Semaphore(1, true);

    private int roomNumber;

    public VirtualView(){
        observers = new ArrayList<>();
    }

    public List<Connection> getConnections() {
        return new ArrayList<>(connections);
    }


    public BiSet<String, String> getBiTokenUsername() {
        return biTokenUsername;
    }


    private Server server;

    public VirtualView(int matchNumber, Server server){
        this.roomNumber = matchNumber;
        this.server = server;
        observers = new ArrayList<>();
    }


    public void reconnect(String token){
        if(!biTokenUsername.containsFirst(token))
            throw new IllegalArgumentException("Trying to reconnect invalid player in room " + roomNumber);
        getConnectionOnToken(token).reconnect(server.sync(roomNumber, biTokenUsername.getSecond(token), token));
    }

    public void refuseReconnection(String token){
        removePlayer(token);
        sem.release();
    }

    public void disconnect(Connection connection){
        if (biTokenUsername.containsFirst(connection.getToken())) {
            notify(new DisconnectionEvent(biTokenUsername.getSecond(connection.getToken())));
            server.disconnectUsername(biTokenUsername.getSecond(connection.getToken()));
        }

        else {
            notify(new DisconnectionEvent(connection.getToken()));
            connection.disconnect();
        }
    }

    public void removePlayer(String token){
        try {
            connections.remove(getConnectionOnToken(token));
            Log.fine("Interrupting event loop");
            eventLoops.get(token).interrupt();
            eventLoops.remove(token);
            sem.release();
        }catch (NullPointerException e){
            throw new NullPointerException("Could not remove unregistred player");
        }
    }

        @Override
        public void dispatch(MvJoinEvent message) {
            if (server.isReconnection(message.getUsername())) {
                //reconnection case
                VirtualView toReJoin = server.getPlayerRoomOnId(message.getUsername());
                String tokenToReJoin = toReJoin.getBiTokenUsername().getFirst(message.getUsername());
                toReJoin.reconnect(tokenToReJoin);
            }

            //new connection
            biTokenUsername.add(new Pair<>(message.getDestination(), message.getUsername()));
            List<Connection> toBroadcast = new ArrayList<>(connections);
            toBroadcast.remove(getConnectionOnToken(message.getDestination()));
            ConnectionBroadcast connection = new ConnectionBroadcast(toBroadcast);
            connection.submit(message);
            sem.release();
        }


        @Override
        public void dispatch(UsernameDeletionEvent message) {
            server.deleteUsername(message.getUsername());
            submit(new ConnectionBroadcast(connections), message);
            sem.release();
        }

        @Override
        public void dispatch(SetUpEvent message) {
            server.endMatchMaking();
            submit(getConnectionOnToken(message.getDestination()), message);
        }

    @Override
    public void update(MVEvent message) {
        try {
            if(biTokenUsername.containsSecond(message.getDestination()))
                //virtual view changes usernames in tokens while sending
                //connections are identified by tokens
                //this branch guards against events that already contain tokens or *

                message.setDestination(biTokenUsername.getFirst(message.getDestination()));
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //if an event cannot be handled is submitted to clients by default

            submit(getConnectionOnToken(message.getDestination()), message);
        }
    }


    public void retrieve(Connection connection){
        //VirtualView changes token in usernames while receiving
        //players are identified by usernames

        VCEvent vcEvent = connection.retrieve();
        if(biTokenUsername.containsFirst(vcEvent.getSource()))
            vcEvent.setSource(biTokenUsername.getSecond(vcEvent.getSource()));
        notify(vcEvent);
    }

    private void submit(Connection connection, MVEvent event){
        Log.fine("Submitting: " + event);
        connection.submit(event);
    }


    public void startListening (Connection connection){
        sem.acquireUninterruptibly();
        connections.add(connection);
        Thread t = new Thread(new EventLoop(this, connection));
        t.start();
        eventLoops.put(connection.getToken(), t);
        List<String> roomUsernames = new ArrayList<>();
        for(Pair<String, String> p: biTokenUsername){
            if(!getConnectionOnToken(p.getFirst()).isDisconnected())
                //player can reconnect inputting disconnected username
                roomUsernames.add(p.getSecond());
        }
        submit(connection, new HandshakeEndEvent(connection.getToken(), roomUsernames, server.getActiveUsernames(), server.getMapConfigs(roomNumber)));
    }


    public Connection getConnectionOnToken(String token){
        if(token.equals("*"))
            return new ConnectionBroadcast(connections);

        for (Connection c:
             connections) {
            if(c.getToken().equals(token))
                return c;
        }
        return null;
    }

}