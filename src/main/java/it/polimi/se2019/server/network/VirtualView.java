package it.polimi.se2019.server.network;

import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.mv_events.*;
import it.polimi.se2019.commons.utility.*;
import it.polimi.se2019.commons.vc_events.DisconnectionEvent;
import it.polimi.se2019.commons.vc_events.VcReconnectionEvent;

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
        observers = new CopyOnWriteArrayList<>();
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void reconnect(String oldToken, Connection reconnected){
        //argument validity guaranteed by caller

        connections.add(reconnected); //re-established connection added

        String username = biTokenUsername.getSecond(oldToken);
        biTokenUsername.removeFirst(oldToken);
        biTokenUsername.add(new Pair<>(reconnected.getToken(), username)); //token-username correspondence updated

        if(!server.isSetUp())
            reconnected.reconnect(server.sync(roomNumber, biTokenUsername.getSecond(reconnected.getToken()), reconnected.getToken()), roomNumber);
        else
            reconnected.reconnect(server.setupSync(biTokenUsername.getSecond(reconnected.getToken()), roomNumber), roomNumber);

        Thread t = new Thread(new EventLoop(this, reconnected));
        t.start();
        eventLoops.put(reconnected.getToken(), t);

        notify(new VcReconnectionEvent(reconnected.getToken(), oldToken, username));
    }



    public void disconnect(Connection connection){
        Log.fine("Disconnecting: " + connection.getToken());
        sem.release();
        if (biTokenUsername.containsFirst(connection.getToken())) {

            String username = biTokenUsername.getSecond(connection.getToken());
            server.disconnectUsername(username); //telling orchestrator about disconnection

            connection.disconnect(); //disabling connection

            Thread toShutdown = eventLoops.get(connection.getToken()); //ensuring that eventLoop is down
            if(toShutdown != null && !toShutdown.isInterrupted())
                toShutdown.interrupt();

            eventLoops.remove(connection.getToken()); //deleting dead eventloop

            connections.remove(connection); //deleting connection from connections

            //keeping entry in biTokenUsername to find the room for eventual reconnection

            notify(new DisconnectionEvent(username, false));
        }

        else {
            notify(new DisconnectionEvent(connection.getToken(), false));
            connection.disconnect();
        }
    }

    public void removePlayer(String username){
        //this method avoid re-connections, essentially this is a kick from this room
        try {
            String token = biTokenUsername.getFirst(username);

            if(token == null){
                Log.fine("Could not remove player: " + username);
                sem.release();
                return;
            }

            biTokenUsername.removeFirst(token);
            sem.release();
        }catch (NullPointerException e){
            Log.severe("Could not remove unregistered player: " + username);
        }
    }

        @Override
        public void dispatch(MvJoinEvent message) {
            if (server.isReconnection(message.getUsername())) {
                //reconnection case, toReJoin is always not null (guaranteed by the condition above)

                VirtualView toReJoin = server.getPlayerRoomOnId(message.getUsername()); // getting room to join

                String tokenToReJoin = toReJoin.getBiTokenUsername().getFirst(message.getUsername()); //getting oldToken (old spot in biTokenUser)

                toReJoin.reconnect(tokenToReJoin, getConnectionOnToken(message.getDestination()));
                biTokenUsername.add(new Pair<>(message.getDestination(), message.getUsername()));
                notify(new DisconnectionEvent(message.getUsername(), true));
                sem.release();
                return;
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
            submit(new ConnectionBroadcast(connections), message);
            sem.release();
        }


    @Override
    public void dispatch(MatchConfigurationEvent message) {
        if(!message.isReconnection()) {
            server.endMatchMaking();
            submit(getConnectionOnToken(message.getDestination()), message);
        }
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
        try {
            VCEvent vcEvent = connection.retrieve();
            if (biTokenUsername.containsFirst(vcEvent.getSource()))
                vcEvent.setSource(biTokenUsername.getSecond(vcEvent.getSource()));

            notify(vcEvent);
        }catch (NullPointerException e){
            Log.fine("NullPointer while retrieving on: " + connection );
        }
    }

    private void submit(Connection connection, MVEvent event){
        try {
            if (!(event instanceof TimerEvent)) {
                Log.fine("Submitting: " + event);
            }
            connection.submit(event);
        }catch (NullPointerException e){
            Log.severe("NullPointerException tryinig to submit on: " + connection + " in room " + roomNumber);
        }
    }


    public void startListening (Connection connection){
        sem.acquireUninterruptibly();
        connections.add(connection);
        Thread t = new Thread(new EventLoop(this, connection));
        t.start();
        eventLoops.put(connection.getToken(), t);
        List<String> roomUsernames = new ArrayList<>();
        for(Pair<String, String> p: biTokenUsername)
            roomUsernames.add(p.getSecond());
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
