package it.polimi.se2019.server.network;

import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.client.view.VirtualView;
import it.polimi.se2019.commons.network.CallbackInterface;
import it.polimi.se2019.commons.network.ServerInterface;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.server.controller.Controller;
import it.polimi.se2019.server.controller.MatchMakingController;
import it.polimi.se2019.server.model.Game;

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
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Semaphore;

public class Server implements ServerInterface {
    private ServerSocket serverSocket;
    private List<VirtualView> virtualViews = new ArrayList<>();
    private List<Game> models = new ArrayList<>();
    private int roomNumber = -1;
    private List<String> usernames = new ArrayList<>();
    private List<String> tokens = new ArrayList<>();
    private Properties properties = new Properties();

    private Semaphore semRMI = new Semaphore(1, true);

    private boolean isMatchMaking = true;

    private Connection suspendedConnection = null;

    public Server(){
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

    public Server(int roomNumber){
        this.roomNumber=roomNumber;
    }

    public int getTurnTimer(){
        return Integer.parseInt(properties.getProperty("TURN_TIMER"));
    }

    public int getInterTurnTimer(){
        return Integer.parseInt(properties.getProperty("INTERTURN_TIMER"));
    }

    public List<String> getMapConfigs(int roomNumber){
        return models.get(roomNumber).getMapConfigs();
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

    public void deleteUsername(String username){
        usernames.remove(username);
    }

    public void addController(Controller controller, int roomNumber){
        virtualViews.get(roomNumber).register(controller);
    }

    public void removeController(Controller controller, int roomNumber){
        virtualViews.get(roomNumber).deregister(controller);
    }

    public void kickPlayer(String toKick) {
        VirtualView room = getPlayerRoomOnId(toKick);
        if(room!=null)
            room.removePlayer(toKick);
        else
            throw new IllegalArgumentException("Kick is impossible, player to kick cannot be found");
    }

    public void handleReconnection(String source, String oldToken){
        VirtualView oldRoom = getPlayerRoomOnId(oldToken);
        VirtualView tempRoom = getPlayerRoomOnId(source);
        if(oldRoom != null)
           oldRoom.reconnect(oldToken);
        else if(tempRoom != null){
            tempRoom.refuseReconnection(source);
        }else
            throw new IllegalArgumentException("Reconnection to handle is not valid");
    }

    private VirtualView getPlayerRoomOnId(String id){
        for (VirtualView v : virtualViews) {
            if (v.getBiTokenUsername().containsFirst(id)) {
                return v;
            }

            if (v.getBiTokenUsername().containsSecond(id)) {
                return v;
            }
        }
        return null;
    }

    private void openConnections()throws IOException, AlreadyBoundException{

        UnicastRemoteObject.exportObject(this, 0);
        serverSocket = new ServerSocket(getPort());

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind(properties.getProperty("SERVER_NAME"), this);
    }

    protected void newMVC() {

        Game model = new Game();
        models.add(model);
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
                suspendedConnection = new ConnectionSocket(generateToken(), socket);
                return;
            }
            virtualViews.get(roomNumber).startListening(new ConnectionSocket(generateToken(), socket));
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
            properties.load(new FileInputStream(Paths.get("files/server.properties").toFile()));
        }catch (IOException e){
            Log.severe("Could not load properties");
        }
    }

    private String generateToken(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        String token = Base64.getEncoder().encodeToString(bytes);
        if(tokens.contains(token))
            generateToken();
        tokens.add(token);
        return token;
    }

    //------------------------RMI REMOTE SERVER INTERFACE IMPLEMENTATION------------------------//

    @Override
    public void startListening(CallbackInterface client) {
        semRMI.acquireUninterruptibly();
        Log.fine("Accepted new client");
        if(!isMatchMaking) {
            isMatchMaking = true;
            suspendedConnection = new ConnectionRMI(generateToken(), client);
            semRMI.release();
            newMatch();
            return;
        }
        virtualViews.get(roomNumber).startListening(new ConnectionRMI(generateToken(), client));
        semRMI.release();
    }

    @Override
    public MVEvent pullEvent(String token) throws RemoteException {
        try {
            return ((ConnectionRMI) virtualViews.get(roomNumber).getConnectionOnToken(token)).pull();
        } catch (NullPointerException e) {
            Log.fine("Invalid token");
            throw new NullPointerException("You detain an invalid token");
        }
    }

    @Override
    public void pushEvent(String token, VCEvent vcEvent) throws RemoteException {
        try{
            ((ConnectionRMI) virtualViews.get(roomNumber).getConnectionOnToken(token)).push(vcEvent);
        }catch (NullPointerException e){
            Log.fine("Invalid token");
            throw new NullPointerException("You detain an invalid token");
        }
    }

    //------------------------RMI REMOTE SERVER INTERFACE IMPLEMENTATION------------------------//

    public static void main(String[] args){
        new Server();
    }

}
