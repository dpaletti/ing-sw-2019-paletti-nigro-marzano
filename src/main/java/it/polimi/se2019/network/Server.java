package it.polimi.se2019.network;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.MatchMakingController;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.utility.Log;
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
import java.util.Scanner;

public class Server implements ServerInterface {
    private ServerSocket serverSocket;
    private boolean socketOpen;
    private Controller controller;
    private VirtualView virtualView;
    private Game model;

    private int port;
    //TODO make it configurable together with RMI registry port
    private static final int DEFAULT_PORT = 2080;

    private Server(int port){
        this.port = port;
        socketOpen = false;
        try {
            UnicastRemoteObject.exportObject(this, 0);
        }catch (RemoteException e) {
            Log.severe("Cannot export server message" + e.getMessage());
        }

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

    public void addController(Controller controller){
        virtualView.register(controller);
    }

    public void removeController(Controller controller){
        virtualView.deregister(controller);
    }

    public void startServer() throws IOException, AlreadyBoundException {
        model = new Game();
        controller = new MatchMakingController(model, this);
        virtualView = new VirtualView();


        addController(controller);
        model.register(virtualView);

        serverSocket = new ServerSocket(port);
        socketOpen = true;

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind(Settings.REMOTE_SERVER_NAME, this);
        Log.info("Server ready");
        try {
            acceptClients();
        }catch (IOException e){
            Log.severe("Server socket has been closed" + e.getMessage());
        }
    }

    private void acceptClients() throws IOException{
        while(!serverSocket.isClosed()){
            Socket socket = serverSocket.accept();
            Log.fine("Accepted new client");
            virtualView.startListening(new ConnectionSocket(virtualView.generateToken(), socket));
        }
    }

    //------------------------RMI REMOTE SERVER INTERFACE IMPLEMENTATION------------------------//

    @Override
    public void startListening(CallbackInterface client) {
        Log.severe("Accepted new client");
        virtualView.startListening(new ConnectionRMI(virtualView.generateToken(), client));
    }

    @Override
    public MVEvent pullEvent(String token) throws RemoteException {
        try {
            return ((ConnectionRMI) virtualView.getConnectionOnId(token, virtualView.getConnections())).pull();
        } catch (Exception e) {
            Log.severe(e.getMessage());
            Thread.currentThread().interrupt();
            System.exit(0);
        }
        return null;
    }

    @Override
    public void pushEvent(String token, VCEvent vcEvent) throws RemoteException {
        try{
            ((ConnectionRMI) virtualView.getConnectionOnId(token, virtualView.getConnections())).push(vcEvent);
        }catch (Exception e){
            Log.severe(e.getMessage());
            System.exit(0);
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
            server.startServer();
        }catch (IOException e){
            if(server.isSocketOpen())
                Log.severe("Could not get RMI registry");
            else
                Log.severe("Could not open server socket");
        }catch (AlreadyBoundException e){
            Log.severe("Could not bind server interface to RMI registry");
        }
    }

}
