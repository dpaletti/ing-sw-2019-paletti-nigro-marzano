package it.polimi.se2019.view;

import java.net.Socket;

public class ConnectionEvent extends VCEvent{
    Socket socket;

    public ConnectionEvent(Socket socket){
        this.socket = socket;
    }

}
