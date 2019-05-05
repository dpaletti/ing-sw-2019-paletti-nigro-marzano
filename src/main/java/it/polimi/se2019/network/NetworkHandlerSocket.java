package it.polimi.se2019.network;

import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.JoinEvent;
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

    public NetworkHandlerSocket(String u, String p, String ip, int port, View view){
        super(u, p, view);
        try {
            establishConnection(ip, port);
        }
        catch(IOException e){
            Log.severe("Could not establish connection" + e.getMessage());
       }
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.severe(e.getMessage());
        }
    }

    public void update(JoinEvent message){
        submit(JsonHandler.serialize(message, message.getClass().toString().replace("class ", "")));
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

private void establishConnection(String serverIp, int serverPort) throws IOException{
        Log.info("Establishing new connection with " + serverIp);
        socket = new Socket(serverIp, serverPort);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        listenToEvent();
        enterMatchMaking();
    }

    @Override
    public void enterMatchMaking(){
        Log.info("Entering match making");
        update(new JoinEvent(username, password, socket.getLocalSocketAddress().toString()));
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

