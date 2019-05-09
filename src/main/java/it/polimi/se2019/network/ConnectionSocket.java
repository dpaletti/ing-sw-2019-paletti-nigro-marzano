package it.polimi.se2019.network;


import it.polimi.se2019.utility.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionSocket implements Connection{
    private Scanner in;
    private PrintWriter out;

    private String token = null;

    public ConnectionSocket(Socket socket){
        try{
            token = socket.getRemoteSocketAddress().toString();
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream());
        }catch (IOException e){
            Log.severe("Cannot establish connection with" + socket.getInetAddress());
        }
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
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
