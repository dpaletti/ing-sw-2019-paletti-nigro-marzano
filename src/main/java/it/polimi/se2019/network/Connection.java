package it.polimi.se2019.network;


import it.polimi.se2019.view.VirtualView;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Scanner;

public class Connection {
    private VirtualView.EventLoop eventLoop;
    private InetAddress ip;
    private ConnectionType connectionType;
    private Scanner in;
    private PrintWriter out;


    public Connection(ConnectionType connectionType, VirtualView.EventLoop eventLoop){
        this.eventLoop = eventLoop;
        this.connectionType = connectionType;
        this.ip = eventLoop.getIp();
    }

    public Connection(ConnectionType connectionType, PrintWriter out, VirtualView.EventLoop eventLoop){
        this.eventLoop = eventLoop;
        this.connectionType = connectionType;
        this.ip = eventLoop.getIp();
        this.in = eventLoop.getIn();
        this.out = out;
    }

    public InetAddress getIp() {
        return ip;
    }

    public Scanner getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public VirtualView.EventLoop getEventLoop() {
        return eventLoop;
    }
}
