package it.polimi.se2019.view;

import it.polimi.se2019.controller.MatchMakingController;
import it.polimi.se2019.network.*;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class VirtualView extends View implements ServerInterface {
    private List<Connection> connections = new CopyOnWriteArrayList<>();
    private List<Connection> timeOuts = new CopyOnWriteArrayList<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();


    public VirtualView(){
        super();
        register(new MatchMakingController(this));
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
                    VirtualView.this.notify(new DisconnectionEvent(connection.getUsername()));
                    Thread.currentThread().interrupt();
                }
            }
        }

    }

    @Override
    public void update(MVEvent message) {
        //TODO think about MVEVent dispatching, maybe take use a separate dispatcher
    }

    public void update(WrongUsernameEvent message){
        message.getConnection().submit(JsonHandler.serialize(message, message.getClass().toString().replace("class ", "")));
    }

    public void retrieve(Connection connection){
        try {
            notify((VCEvent) JsonHandler.deserialize(connection.retrieve()));
        }catch (ClassNotFoundException e){
            Log.severe("Error during deserialization: " + e.getMessage());
        }

    }

    public void submit(Connection connection, String data){
        connection.submit(data);
    }

    @Override
    public void register(String username, String password) {
        ConnectionRMI connection = new ConnectionRMI();
        connection.setPassword(password);
        connection.setUsername(username);
        newEventLoop(connection);
    }

    @Override
    public String pullEvent() throws RemoteException {
        try {
            return ((SynchronousQueue<String>) ((ConnectionRMI) getConnectionOnId(RemoteServer.getClientHost())).getOut()).take();
        } catch (Exception e) {
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public void pushEvent(String data) throws RemoteException {
        try{
            VCEvent event = (VCEvent) JsonHandler.deserialize(data);
            //event.setSource(RemoteServer.getClientHost());
            data = JsonHandler.serialize(event, event.getClass().toString().replace("class ", ""));
            ((SynchronousQueue<String>) ((ConnectionRMI) getConnectionOnId(RemoteServer.getClientHost())).getIn()).put(data);
        }catch(InterruptedException e){
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
        }catch (Exception e){
            Log.severe(e.getMessage());
        }
    }

    @Override
    public void ping() throws RemoteException {
        try {
            ((ConnectionRMI) getConnectionOnId(RemoteServer.getClientHost())).ping();
        }catch (Exception e){
            Log.severe(e.getMessage());
        }
    }

    public Connection getConnectionOnId(String id){
        for (Connection c:
             connections) {
            if(c.getUsername().equals(id))
                return c;
        }
        //TODO differentiate those two cases
        for(Connection c:
            timeOuts){
            if(c.getUsername().equals(id))
                return c;
        }
        throw new InvalidParameterException("There is no connection corresponding to the specified ip");
    }

    public void newEventLoop (Connection connection){
        timeOuts.add(connection); //until the join request, connected clients are considered time-outed
        executorService.submit(new EventLoop(connection));
    }


    public void addPlayer(JoinEvent clientInfo) {
        for(Connection c:
                timeOuts) {
            if(clientInfo.getBootstrapId().equals(c.getBootstrapId())){
                timeOuts.remove(c);
                c.setUsername(clientInfo.getUsername());
                c.setPassword(clientInfo.getPassword());
                connections.add(c);
            }

        }
    }

    public void wrongUsername(JoinEvent message){
        WrongUsernameEvent event = new WrongUsernameEvent();
        for(Connection c:
            timeOuts){
            if(c.getBootstrapId().equals(message.getBootstrapId())){
                event.setConnection(c);
                update(event);
                return;
            }
        }
    }

    public void timeOut(Connection connection){
        //TODO maybe better to identify everything with connection rather than with inetAddress
        connections.remove(connection);
        timeOuts.add(connection);
        }

    public void reconnectPlayer(JoinEvent clientInfo){
        //connections.add(getConnectionOnId(clientInfo.getSource()));
        //timeOuts.remove(getConnectionOnId(clientInfo.getSource()));
        //timeOuts.remove(getConnectionOnId(clientInfo.getSource()));
        }

}
