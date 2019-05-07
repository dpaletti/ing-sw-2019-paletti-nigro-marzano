package it.polimi.se2019.network;


import it.polimi.se2019.utility.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionSocket implements Connection{
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private String username = null;
    private String password = null;

    public ConnectionSocket(Socket socket){
        try{
            this.socket=socket;
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream());
        }catch (IOException e){
            Log.severe("Cannot establish connection with" + socket.getInetAddress());
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getId() {
        if(username == (null))
            return socket.getRemoteSocketAddress().toString();
        return username;
    }

    @Override
    public void setPassword(String password) {
        if(this.password == (null))
            this.password = password;
        else
            throw new UnsupportedOperationException("Password in Socket Connection already set");
    }


    public void setId(String username){
        if(this.username == (null))
            this.username = username;
        else
            throw new UnsupportedOperationException("Username in Socket Connection already set");
    }

    @Override
    public void submit(String data) {
        out.println(data);
    }

    @Override
    public String retrieve(){
        return in.nextLine();
    }

}
