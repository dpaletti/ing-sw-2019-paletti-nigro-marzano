package it.polimi.se2019.view;

import it.polimi.se2019.model.MVEvents.MatchMakingEndEvent;
import it.polimi.se2019.model.MVEvents.UsernameDeletionEvent;
import it.polimi.se2019.model.MVEvents.UsernameEvaluationEvent;
import it.polimi.se2019.network.*;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.VCEvents.DisconnectionEvent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class VirtualView extends View implements ServerInterface {
    private List<Connection> connections = new CopyOnWriteArrayList<>();
    private List<Connection> timeOuts = new CopyOnWriteArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Dispatcher dispatcher = new Dispatcher();
    private static final String PREFIX = "class ";

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

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    retrieve(connection);
                }catch(NoSuchElementException e) {
                    VirtualView.this.notify(new DisconnectionEvent(connection.getId()));
                    Thread.currentThread().interrupt();
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
                    if(connection.getId() == (null) && connection.getPassword() == (null)) {
                        connection.setId(message.getDestination());
                        connection.setPassword(message.getPassword());
                    }
                    connections.add(connection);
                    timeOuts.remove(connection);
                }
                submit(connection, JsonHandler.serialize(message, message.getClass().toString().replace(PREFIX, "")));
            }catch (NullPointerException e){
                Log.severe("Trying to notify over a non-existent connection about username");
            }
        }

        @Override
        public void update(UsernameDeletionEvent message) {
            Connection connection = null;
            try {
                Log.info("Deleting" + message.getDestination());
                connection = getConnectionOnId(message.getDestination(), connections);
                connections.remove(connection);
            }catch (NullPointerException e){
                Log.info("Deleting" + message.getDestination() + "from TimeOuts");
                connection = getConnectionOnId(message.getDestination(), timeOuts);
                timeOuts.remove(connection);
            }finally {
                if(connection != (null))
                    submit(connection, JsonHandler.serialize(message, message.getClass().toString().replace(PREFIX, "")));
            }
        }

        @Override
        public void update(MatchMakingEndEvent message) {
            submit(getConnectionOnId(message.getDestination(), connections), JsonHandler.serialize(message, message.getClass().toString().replace(PREFIX, "")));
        }
    }

    @Override
    public void update(MVEvent message) {
        message.handle(dispatcher);
    }


    private void retrieve(Connection connection){
        try {
            notify((VCEvent) JsonHandler.deserialize(connection.retrieve()));
        }catch (ClassNotFoundException e){
            Log.severe("Error during deserialization: " + e.getMessage());
        }

    }

    private void submit(Connection connection, String data){
        connection.submit(data);
    }


    public void startListening (Connection connection){
        timeOuts.add(connection); //until the join request, connected clients are considered time-outed
        executorService.submit(new EventLoop(connection));
    }

    //------------------------RMI REMOTE SERVER INTERFACE IMPLEMENTATION------------------------//

    @Override
    public void startListening(String username, String password) {
        startListening(new ConnectionRMI(username, password));
    }

    @Override
    public String pullEvent(String username) throws RemoteException {
        try {
            return ((SynchronousQueue<String>) ((ConnectionRMI) getConnectionOnId(username, connections)).getOut()).take();
        } catch (Exception e) {
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public void pushEvent(String username, String data) throws RemoteException {
        try{
            ((SynchronousQueue<String>) ((ConnectionRMI) getConnectionOnId(username, connections)).getIn()).put(data);
        }catch(InterruptedException e){
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }catch (Exception e){
            Log.severe(e.getMessage());
        }
    }

    @Override
    public void ping(String username) throws RemoteException {
        try {
            ((ConnectionRMI) getConnectionOnId(username, connections)).ping();
        }catch (Exception e){
            Log.severe(e.getMessage());
        }
    }

    //------------------------RMI REMOTE SERVER INTERFACE IMPLEMENTATION------------------------//

    private Connection getConnectionOnId(String id, List<Connection> toQuery){
        if(id.equals("*"))
            return new ConnectionBroadcast(connections);

        for (Connection c:
             toQuery) {
            if(c.getId().equals(id))
                return c;
        }
        throw new NullPointerException();
    }

}
