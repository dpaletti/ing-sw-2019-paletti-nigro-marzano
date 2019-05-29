package it.polimi.se2019.network;

import it.polimi.se2019.utility.Event;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.VcJoinEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetworkHandlerSocket extends NetworkHandler {
    private transient Socket socket;
    private transient Scanner in;
    private transient PrintWriter out;

    public NetworkHandlerSocket(Client client, String ip, int port){
        super(client);
        try {
            socket = new Socket(ip, port);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);


            listenToEvent();
        }catch (IOException e){
            Log.severe("Could not establish connection" + e.getMessage());
        }
    }


    public NetworkHandlerSocket(String token, Client client , String ip, int port){
        super(token, client);
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


    public void update(Event message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.fine("Ignored " + message);
        }
    }

    @Override
    public void dispatch(VcJoinEvent message){
        submit(message);
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
        listener = new Thread(() -> {
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
        });
        listener.start();
    }
}

