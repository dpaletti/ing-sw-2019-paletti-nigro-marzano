package it.polimi.se2019.network;


public interface Connection {
    void submit(String data);
    String retrieve();
    String getPassword();
    String getUsername();
    void setPassword(String password);
    void setUsername(String username);
    String getBootstrapId();
}
