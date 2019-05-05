package it.polimi.se2019.network;


import it.polimi.se2019.utility.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionSocket implements Connection{
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

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
    public void submit(String data) {
        out.println(data);
    }

    @Override
    public String retrieve(){
        return in.nextLine();
    }

    @Override
    public InetAddress getRemoteEnd() {
        return socket.getInetAddress();
    }
}
