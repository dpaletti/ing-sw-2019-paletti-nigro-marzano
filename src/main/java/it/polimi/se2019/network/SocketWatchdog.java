package it.polimi.se2019.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketWatchdog implements Runnable {
    private Socket socket;
    private VirtualView virtualView;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public SocketWatchdog(Socket socket, VirtualView virtualView){
        this.socket = socket;
        this.virtualView = virtualView;
    }

    @Override
    public void run(){
        final int WATCH_RATE = 1; //milliseconds
        while(true) {
            try {
                socket.getInputStream();
                sleep(WATCH_RATE);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
                virtualView.handleDisconnection(socket);
                break;
            }
        }
    }

    private void sleep(int watchRate){
        try{
            Thread.sleep(watchRate);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, e.getMessage());
        }

    }

}
