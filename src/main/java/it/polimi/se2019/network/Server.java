package it.polimi.se2019.network;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.MatchMakingController;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Saves;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Server implements ServerInterface {
    //TODO move username and token uniqueness check here to support multiple parallel lobbys
    private ServerSocket serverSocket;
    private boolean socketOpen;
    private List<VirtualView> virtualViews = new ArrayList<>();
    private Saves saves = new Saves();
    private int roomNumber = 0;
    private List<String> usernames = new ArrayList<>();
    private List<String> tokens = new ArrayList<>();

    private int port;
    //TODO make it configurable together with RMI registry port
    private static final int DEFAULT_PORT = 2080;
    private Semaphore semRMI = new Semaphore(1, true);

    private boolean isMatchMaking = true;
    boolean firstMatch = true;

    Connection suspendedConnection = null;

    private Server(int port){
        this.port = port;
        socketOpen = false;

        try {
            UnicastRemoteObject.exportObject(this, 0);
        }catch (RemoteException e) {
            Log.severe("Cannot export server message" + e.getMessage());
        }

    }

    public List<String> getUsernames() {
        return new ArrayList<>(usernames);
    }

    public List<String> getTokens() {
        return new ArrayList<>(tokens);
    }

    public Saves getSaves() {
        return saves; //TODO correct getter
    }

    private int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }

    private boolean isSocketOpen(){
        return socketOpen;
    }

    public void addUsername(String username){
        usernames.add(username);
    }

    public void addToken(String token){
        tokens.add(token);
    }

    public void addController(Controller controller, int roomNumber){
        virtualViews.get(roomNumber).register(controller);
    }

    public void removeController(Controller controller, int roomNumber){
        virtualViews.get(roomNumber).deregister(controller);
    }

    private void openConnections()throws IOException, AlreadyBoundException {
        usernames.add("*");

        serverSocket = new ServerSocket(port);
        socketOpen = true;

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind(Settings.REMOTE_SERVER_NAME, this);
    }

    private void startServer() {

        Game model = new Game();
        virtualViews.add(new VirtualView(roomNumber, this)); //this needs to stay here
        Controller controller = new MatchMakingController(model, this, roomNumber);


        model.register(virtualViews.get(roomNumber));
        if(suspendedConnection != null) {
            Log.fine("Un-suspending connection");
            virtualViews.get(roomNumber).startListening(suspendedConnection);
            suspendedConnection = null;
        }
        Log.info("Server ready");
        try {
            acceptClients();
        }catch (IOException e){
            Log.severe("Server socket has been closed" + e.getMessage());
        }
    }

    private void acceptClients() throws IOException{
        semRMI.release();
        while(true){
            Socket socket = serverSocket.accept();
            if(!isMatchMaking) {
                isMatchMaking = true;
                suspendedConnection = new ConnectionSocket(virtualViews.get(roomNumber).generateToken(), socket);
                break;
            }
            virtualViews.get(roomNumber).startListening(new ConnectionSocket(virtualViews.get(roomNumber).generateToken(), socket));
          }
    }

    private void startMatch(){
        while(!Thread.currentThread().isInterrupted()) {
            semRMI.acquireUninterruptibly();
            if(firstMatch) {
                firstMatch = false;
            }else
                roomNumber++;
            startServer();
            Log.fine("Creating new game");
        }
    }

    public void endMatchMaking(){
        Log.fine("Ending matchMaking");
        isMatchMaking = false;
    }

    //------------------------RMI REMOTE SERVER INTERFACE IMPLEMENTATION------------------------//

    @Override
    public void startListening(CallbackInterface client) {
        semRMI.acquireUninterruptibly();
        semRMI.release();
        Log.fine("Accepted new client");
        if(!isMatchMaking) {
            isMatchMaking = true;
            suspendedConnection = new ConnectionRMI(virtualViews.get(roomNumber).generateToken(), client);
            startMatch();
            return;
        }
        virtualViews.get(roomNumber).startListening(new ConnectionRMI(virtualViews.get(roomNumber).generateToken(), client));
    }

    @Override
    public MVEvent pullEvent(String token) throws RemoteException {
        try {
            return ((ConnectionRMI) virtualViews.get(roomNumber).getConnectionOnId(token)).pull();
        } catch (NullPointerException e) {
            Log.fine(e.getMessage());
            throw new NullPointerException("You detain an invalid token");
        }
    }

    @Override
    public void pushEvent(String token, VCEvent vcEvent) throws RemoteException {
        try{
            ((ConnectionRMI) virtualViews.get(roomNumber).getConnectionOnId(token)).push(vcEvent);
        }catch (NullPointerException e){
            Log.fine(e.getMessage());
            throw new NullPointerException("You detain an invalid token");
        }
    }

    //------------------------RMI REMOTE SERVER INTERFACE IMPLEMENTATION------------------------//


    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        Log.input("Input socket port number (> 1024): ");
        Server server = new Server(in.nextInt());
        if(server.getPort() <= 1024){
            Log.info("This port does not work, default port will be used " + DEFAULT_PORT);
            server.setPort(DEFAULT_PORT);
        }

        try {
            server.openConnections();
        }catch (IOException e){
            if(server.isSocketOpen())
                Log.severe("Could not get RMI registry");
            else
                Log.severe("Could not open server socket");
        }catch (AlreadyBoundException e){
            Log.severe("Could not bind server interface to RMI registry");
        }

        server.startMatch();
    }

}
