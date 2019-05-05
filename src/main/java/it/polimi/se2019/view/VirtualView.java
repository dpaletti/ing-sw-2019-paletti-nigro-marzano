package it.polimi.se2019.view;

import it.polimi.se2019.controller.MatchMakingController;
import it.polimi.se2019.network.*;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import javax.management.remote.rmi.RMIConnection;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
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
                    Log.info("Client " + connection.getRemoteEnd() + " just disconnected");
                    VirtualView.this.notify(new DisconnectionEvent(connection.getRemoteEnd()));
                    Thread.currentThread().interrupt();
                }
            }
        }

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
    public void register() {
        try {
            newEventLoop(new ConnectionRMI(RemoteServer.getClientHost()));
        }catch (ServerNotActiveException e){
            Log.severe(e.getMessage());
        }
    }

    @Override
    public String pullEvent() throws RemoteException {
        try {
            return ((SynchronousQueue<String>) ((ConnectionRMI) getConnectionOnIp(InetAddress.getByName(RemoteServer.getClientHost()))).getOut()).take();
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
            event.setSource(InetAddress.getByName(RemoteServer.getClientHost()));
            data = JsonHandler.serialize(event, event.getClass().toString().replace("class ", ""));
            ((SynchronousQueue<String>) ((ConnectionRMI) getConnectionOnIp(InetAddress.getByName(RemoteServer.getClientHost()))).getIn()).put(data);
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
            ((ConnectionRMI) getConnectionOnIp(InetAddress.getByName(RemoteServer.getClientHost()))).ping();
        }catch (Exception e){
            Log.severe(e.getMessage());
        }
    }

    private Connection getConnectionOnIp(InetAddress ip){
        for (Connection c:
             connections) {
            if(c.getRemoteEnd().equals(ip))
                return c;
        }
        //TODO differentiate those two cases
        for(Connection c:
            timeOuts){
            if(c.getRemoteEnd().equals(ip))
                return c;
        }
        throw new ValueException("There is no connection corresponding to the specified ip");
    }

    public void newEventLoop (Connection connection){
        timeOuts.add(connection); //until the join request connected clients are considered time-outed
        executorService.submit(new EventLoop(connection));
    }


    public void addPlayer(JoinEvent clientInfo) {
        for(Connection c:
                timeOuts) {
            if (c.getRemoteEnd().equals(clientInfo.getSource())) {
                connections.add(c);
                timeOuts.remove(c);
            }
        }
    }

    public void timeOut(InetAddress inetAddress){
        //TODO maybe better to identify everything with connection rather than with inetAddress
        for (Connection c:
                connections) {
            if(c.getRemoteEnd().equals(inetAddress)){
                connections.remove(c);
                timeOuts.add(c);
            }
        }
    }
}
