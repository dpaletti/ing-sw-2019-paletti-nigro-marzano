package it.polimi.se2019.view;

import java.net.Socket;

public class DisconnectionEvent extends VCEvent {
    Socket socket;

    public DisconnectionEvent(Socket socket){
        super();
        this.socket = socket;
    }
    
    public Socket getSocket() {
        return socket;
    }
}
