package it.polimi.se2019.network;

import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.vc_events.JoinEvent;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetworkHandlerSocket extends NetworkHandler {
    private transient Socket socket;
    private transient Scanner in;
    private transient PrintWriter out;
    private transient Dispatcher dispatcher = new Dispatcher();

    public NetworkHandlerSocket(String ip, int port){
        super();
        try {
            socket = new Socket(ip, port);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);


            listenToEvent();
        }catch (IOException e){
            Log.severe("Could not establish connection" + e.getMessage());
        }
    }


    public NetworkHandlerSocket(String token,  String ip, int port){
        super(token);
        try {
            socket = new Socket(ip, port);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            listenToEvent();
        }
        catch(IOException e){
            Log.severe("Could not establish connection " + e.getMessage());
       }
    }

    @Override
    public void setToken(String token){
        if (this.token == (null))
            this.token = token;
        else
            throw new UnsupportedOperationException("Session token is already set");

    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            Log.severe(e.getMessage());
        }
    }

    private class Dispatcher extends VCEventDispatcher {

        @Override
        public void update(JoinEvent message){
            submit(message);
        }

    }

    @Override
    public void submit(VCEvent vcEvent) {
        out.println(JsonHandler.serialize(vcEvent));
    }

    @Override
    public void retrieve() throws ClassNotFoundException{
        notify ((MVEvent)JsonHandler.deserialize(in.nextLine()));
    }

    @Override
    protected void listenToEvent(){
        new Thread(() -> {
                while(true){
                    try {
                        retrieve();
                    }catch (NoSuchElementException e){
                        Log.info("Server disconnected");
                        break;
                    }catch (ClassNotFoundException e){
                        Log.severe("Error during deserialization: " + e.getMessage());
                    }
                }
        }).start();
    }
}

