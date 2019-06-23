package it.polimi.se2019.network;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.MatchMakingController;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.VirtualView;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Semaphore;

public class Server implements ServerInterface {
    private ServerSocket serverSocket;
    private List<VirtualView> virtualViews = new ArrayList<>();
    private int roomNumber = -1;
    private List<String> usernames = new ArrayList<>();
    private List<String> tokens = new ArrayList<>();
    private Properties properties = new Properties();

    private Semaphore semRMI = new Semaphore(1, true);

    private boolean isMatchMaking = true;

    private Connection suspendedConnection = null;

    private Server(){
        usernames.add("*");

        try {
            fillProperties();
            openConnections();
            newMatch();
        }catch (AlreadyBoundException e){
            Log.severe("Could not bind RMI interface");
        }catch (IOException e){
            Log.severe("IO error while starting network up");
        }

    }

    public List<String> getUsernames() {
        return new ArrayList<>(usernames);
    }

    public List<String> getTokens() {
        return new ArrayList<>(tokens);
    }

    private int getPort(){
        return Integer.parseInt(properties.getProperty("PORT"));
    }

    public int getMatchMakingTimer(){
        return Integer.parseInt(properties.getProperty("MATCH_MAKING_TIMER"));
    }

    public int getMatchSetupTimer(){
        return Integer.parseInt(properties.getProperty("MATCH_SETUP_TIMER"));
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

    private void openConnections()throws IOException, AlreadyBoundException{

        UnicastRemoteObject.exportObject(this, 0);
        serverSocket = new ServerSocket(getPort());

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind(properties.getProperty("REMOTE_SERVER_NAME"), this);
    }

    private void newMVC() {

        Game model = new Game();
        virtualViews.add(new VirtualView(roomNumber, this));
        new MatchMakingController(model, this, roomNumber);


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
                return;
            }
            virtualViews.get(roomNumber).startListening(new ConnectionSocket(virtualViews.get(roomNumber).generateToken(), socket));
          }
    }

    private void newMatch(){
        while(!Thread.currentThread().isInterrupted()) {
            semRMI.acquireUninterruptibly();
            roomNumber++;
            newMVC();
            Log.fine("Creating new game");
        }
    }

    public void endMatchMaking(){
        Log.fine("Ending matchMaking");
        isMatchMaking = false;
    }

    private void fillProperties(){
        try {
            properties.load(new FileInputStream(Paths.get("files/server.config").toFile()));
        }catch (IOException e){
            Log.severe("Could not load properties");
        }
    }

    //------------------------RMI REMOTE SERVER INTERFACE IMPLEMENTATION------------------------//

    @Override
    public void startListening(CallbackInterface client) {
        semRMI.acquireUninterruptibly();
        Log.fine("Accepted new client");
        if(!isMatchMaking) {
            isMatchMaking = true;
            suspendedConnection = new ConnectionRMI(virtualViews.get(roomNumber).generateToken(), client);
            semRMI.release();
            newMatch();
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
        new Server();
    }

}
