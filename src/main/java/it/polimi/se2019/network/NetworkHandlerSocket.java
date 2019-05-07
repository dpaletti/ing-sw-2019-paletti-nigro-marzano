package it.polimi.se2019.network;

import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.VCEventDispatcher;
import it.polimi.se2019.view.VCEvents.JoinEvent;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetworkHandlerSocket extends NetworkHandler {
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Dispatcher dispatcher = new Dispatcher();

    public NetworkHandlerSocket(String username, String password, String ip, int port, View view){
        super(username, password, view);
        try {
            socket = new Socket(ip, port);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            listenToEvent();
            enterMatchMaking();
        }
        catch(IOException e){
            Log.severe("Could not establish connection" + e.getMessage());
       }
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(dispatcher);
        }catch (UnsupportedOperationException e){
            Log.severe(e.getMessage());
        }
    }

    private class Dispatcher extends VCEventDispatcher{

        @Override
        public void update(JoinEvent message){
            submit(JsonHandler.serialize(message, message.getClass().toString().replace("class ", "")));
        }

    }

    @Override
    public void submit(String toVirtualView) {
        Log.fine("submitted JSON: " + toVirtualView);
        out.println(toVirtualView);
    }

    @Override
    public void retrieve() throws ClassNotFoundException{
        notify ((MVEvent)JsonHandler.deserialize(in.nextLine()));
    }


    @Override
    public void enterMatchMaking(){
        Log.info("Entering match making");
        update(new JoinEvent(socket.getLocalSocketAddress().toString(), password, username ));
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

