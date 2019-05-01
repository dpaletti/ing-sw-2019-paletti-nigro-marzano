package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    private String serverIp;
    private int serverPort;
    private NetworkHandler networkHandler;

    public Client(String ip, int port){
        this.serverIp = ip;
        this.serverPort = port;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public static void main(String[] args){
        Log.input("input Server IP (with no spaces) then press Enter: ");
        Scanner in = new Scanner(System.in);
        String ip = in.nextLine();

        Log.input("input Server port then press Enter: ");
        int port = in.nextInt();
        in.nextLine();

        Client client = new Client(ip, port);

        Log.input("Preferred network communication mode (RMI/Socket): ");
        String connectionType = in.nextLine();

        if(connectionType.equals("Socket"))
            client.networkHandler = new NetworkHandlerSocket(client);
        //else if(connectionType.equals("RMI"))
          //  client.networkHandler = new NetworkHandlerRMI(client);
        else{
            Log.info( "'" + connectionType + "'" + " as a network communication mode is not valid, falling back to Socket ");
            client.networkHandler = new NetworkHandlerSocket(client);
        }

        try {
            client.networkHandler.establishConnection();
        }catch (IOException e){
            Log.severe(e.getMessage());
        }
    }

}
