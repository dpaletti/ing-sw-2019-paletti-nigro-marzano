package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.JoinEvent;
import it.polimi.se2019.view.VCEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NetworkHandlerSocket extends NetworkHandler {
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    public NetworkHandlerSocket(Client client){
        super(client);
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
        String serialized = serialize(message, message.getClass().toString().replace("class ", ""));
        submit(serialized);
    }

    @Override
    public void submit(String toVirtualView) {
        Log.fine("submitted JSON: " + toVirtualView);
        out.println(toVirtualView);
    }

    @Override
    public String retrieve() {
        return in.nextLine();
    }

    @Override
    public void establishConnection() throws IOException {
        Log.info("Establishing new connection with " + client.getServerIp());
        try{
            socket = new Socket(client.getServerIp(), client.getServerPort());
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

        }catch(NoSuchElementException e){
            Log.severe(e.getMessage());
        }
        listenToEvent();
        enterMatchMaking();
    }

    @Override
    public void enterMatchMaking(){
        Log.info("Entering match making");
        update(new JoinEvent(ConnectionType.SOCKET, socket.getLocalAddress()));
    }

    public void listenToEvent(){
        new Thread(() -> {
                while(true){
                    try {
                        deserialize(retrieve());
                    }catch (NoSuchElementException e){
                        Log.info("Server disconnected");
                        break;
                    }
                }
        }).start();
    }
}

