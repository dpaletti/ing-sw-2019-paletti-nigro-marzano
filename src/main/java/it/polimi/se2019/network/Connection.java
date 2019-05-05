package it.polimi.se2019.network;

import java.net.InetAddress;

public interface Connection {
    void submit(String data);
    String retrieve();
    InetAddress getRemoteEnd();
}
