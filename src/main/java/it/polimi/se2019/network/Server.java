package it.polimi.se2019.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    VirtualView virtualView;
    private int port;
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static final int MAXCLIENT = 5;

    public Server(int port){
        this.port = port;
    }

    public class ServerClientHandler implements Runnable {
        private Socket socket;


        public ServerClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                //TODO Understand what to do when a client connects

                Scanner in = new Scanner(socket.getInputStream());
                new SocketWatchdog(socket, virtualView);
                virtualView.addConnection(socket);
                socket.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }

    }

    public boolean startServer(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        virtualView = new VirtualView(this);

        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.log(Level.FINE, "Server ready");
            acceptClients(serverSocket, executorService);
        }catch(IOException e){
            //chosen port is already in use
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }

        executorService.shutdown();
        return true;
    }

    private void acceptClients(ServerSocket serverSocket, ExecutorService executorService){
        int clientCount = 0;

        while(clientCount < MAXCLIENT){
            //TODO add end of client registration mechanics
            //that is: client can register, up to 5, for a limited time that probably the controller manages
            //together with the room logic for match creation most likely
            try {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(200);
                executorService.submit(new ServerClientHandler(socket));
                clientCount++;
            } catch(IOException e){
                //serverSocket has been closed
                break;
            }
        }
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        int port;
        //TODO startup
        while(true) {
            logger.log(Level.INFO, "Input port number (> 1024): ");
            port = in.nextInt();
            Server server = new Server(port);
            if(port <= 1024 || !server.startServer()){
                logger.log(Level.FINE, "This port does not work");
            }
            else
                break;
        }

    }

}
