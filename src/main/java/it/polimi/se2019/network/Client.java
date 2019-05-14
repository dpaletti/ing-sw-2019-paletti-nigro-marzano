package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.ViewCLI;
import it.polimi.se2019.view.ViewGUI;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;


public class Client {
    private NetworkHandler networkHandler;
    private View view;
    private String username;
    private static final Path FILE_PATH= Paths.get("src/main/resources/config/token");

    private Scanner in = new Scanner(System.in);

    private boolean triedReconnecting = false;

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public void openSession(String token, List<String> usernames){
        if(!networkHandler.isReconnection()) {
            networkHandler.setToken(token);
            usernameSelection(usernames);
            return;
        }
        networkHandler.reconnect(token);
    }

    public void connectionRefused(String cause){
        Log.input("Connection refused: " + cause
                   + ". Do you want to connect to a new session? (yes/no): [default = yes]");
        if(in.nextLine().equals("no")){
            Log.info("Goodbye!");
            System.exit(0);
        }
        else {
                writeToken("");
                networkHandler.stopListening();
                main(new String[]{"a", "b", "c"});
        }
    }

    private void usernameSelection(List<String> usernames){
            in = new Scanner(System.in);

            Log.input("Insert username");

            username = in.nextLine();

            while (usernames.contains(username)) {
                Log.input("Choose another username please, '" + username + "' already in use");
                username = in.nextLine();
            }

            networkHandler.chooseUsername(username);
    }

    private String readToken(){
        try {
            if(!Files.readAllLines(FILE_PATH).isEmpty())
                return Files.readAllLines(FILE_PATH).get(0);
            else
                throw new InvalidStateException("Expected token but no token found to read");
        }catch (IOException e){
            Log.severe("Could not read token");
        }
        return null;
    }

    public void writeToken(String token){
        try {
            Files.write(FILE_PATH, token.getBytes(StandardCharsets.UTF_8));
        }catch (IOException e){
            Log.severe("could not write token to file");
        }
   }

    private boolean isReconnection() {
        if (FILE_PATH.toFile().length() != 0) {
            Log.input("Do you want to reconnect to the previous ongoing session?" +
                        " (yes/no): [default = no]");
            return in.nextLine().equals("yes");
            }
        return false;
    }

    public static void main(String[] args) {

        Client client = new Client();


        Log.input("Preferred view mode (GUI/CLI): [default = GUI] ");

        String viewMode = client.in.nextLine();

        Log.input("Preferred network communication mode (RMI/Socket): [default = Socket] ");

        String connectionType = client.in.nextLine();

        boolean reconnecting = client.isReconnection();
        String token = null;
        if(reconnecting)
            token = client.readToken();
        if(!viewMode.equals("CLI"))
            client.view = new ViewGUI(client);
        else
            client.view = new ViewCLI(client);

        if (!connectionType.equals("RMI")) {
            Log.input("input Server IP (with no spaces) then press Enter: ");
            String ip = client.in.nextLine();

            Log.input("input Server port then press Enter: ");
            int port = client.in.nextInt();
            client.in.nextLine();
            if(!reconnecting)
                client.networkHandler = new NetworkHandlerSocket(client, ip, port);
            else
                client.networkHandler = new NetworkHandlerSocket(token, client, ip, port);

        }
        else {
            if(!reconnecting)
                client.networkHandler = new NetworkHandlerRMI(client);
            else
                client.networkHandler = new NetworkHandlerRMI(token, client);
            }
        client.view.register(client.networkHandler);
        client.networkHandler.register(client.view);
    }
}
