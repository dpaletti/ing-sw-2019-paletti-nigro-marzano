package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private ServerSocket serverSocket;
    private List<Socket> socketBuffer = new CopyOnWriteArrayList<>();
    private VirtualView virtualView;

    private int port;
    //TODO make it configurable
    private static final int DEFAULT_PORT = 2080;

    public Server(int port){
        this.port = port;
    }


    private boolean startServer(){
        //TODO: add RMI register opening
        //non blocking call should probably be put here or better in a different method

        try{
            serverSocket = new ServerSocket(port);
            Log.info("Server ready");
            acceptClients();
        }catch(IOException e){
            //chosen port is already in use
            Log.severe(e.getMessage());
            return false;
        }

        return true;
    }

    public void acceptClients(){
        while(true){ //NOSONAR
            try {
                Socket socket = serverSocket.accept();
                Log.fine("Accepted new client");
                socketBuffer.add(socket);
                virtualView.newEventLoop(socket.getInetAddress(), new Scanner(socket.getInputStream()));
            } catch(IOException e){
                //serverSocket has been closed
                Log.severe(e.getMessage());
                break;
            }
        }
    }

    public List<Socket> getSocketBuffer() {
        return new CopyOnWriteArrayList<>(socketBuffer);
    }

    private int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }

    private void setVirtualView(VirtualView v){
        this.virtualView = v;
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        Log.input("Input port number (> 1024): ");
        Server server = new Server(in.nextInt());
        server.setVirtualView(new VirtualView(server));
        if(server.getPort() <= 1024 || !server.startServer()){
            Log.info("This port does not work, default port will be used " + DEFAULT_PORT);
        }
        server.setPort(DEFAULT_PORT);
        server.startServer();
    }

}
