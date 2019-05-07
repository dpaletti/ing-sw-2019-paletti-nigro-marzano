package it.polimi.se2019.network;


public interface Connection {
    void submit(String data);
    String retrieve();
    String getPassword();
    String getId();
    void setPassword(String password);
    void setId(String username);
}
