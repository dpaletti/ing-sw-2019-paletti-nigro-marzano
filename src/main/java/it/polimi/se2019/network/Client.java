package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.ViewCLI;
import it.polimi.se2019.view.ViewGUI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;


public class Client {
    private NetworkHandler networkHandler;
    private View view;
    private String username = null;
    private static Path TOKEN_FILE_PATH;
    private static Path USERNAME_FILE_PATH;

    public String getUsername() {
        if(username == (null)){
            try {
                if(!Files.readAllLines(USERNAME_FILE_PATH).isEmpty())
                    username = Files.readAllLines(USERNAME_FILE_PATH).get(0);
            }catch (IOException e){
                Log.severe("Could not read username");
            }
        }
        return username;
    }

    private Scanner in = new Scanner(System.in);

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public void openSession(String token, List<String> usernames){
        if(!networkHandler.isReconnection()) {
            networkHandler.setToken(token);
            usernameSelection(usernames);
        }
        else
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

        try {
            Files.write(USERNAME_FILE_PATH, username.getBytes(StandardCharsets.UTF_8));
        }catch (IOException e){
            Log.severe("could not write token to file");
        }
        networkHandler.chooseUsername(username);
    }

    private String readToken(){
        try {
            if(!Files.readAllLines(TOKEN_FILE_PATH).isEmpty())
                return Files.readAllLines(TOKEN_FILE_PATH).get(0);
        }catch (IOException e){
            Log.severe("Could not read token");
        }
        return null;
    }

    public void writeToken(String token){
        try {
            Files.write(TOKEN_FILE_PATH, token.getBytes(StandardCharsets.UTF_8));
        }catch (IOException e){
            Log.severe("could not write token to file");
        }
   }

    private boolean isReconnection() {
        if (TOKEN_FILE_PATH.toFile().length() != 0) {
            Log.input("Do you want to reconnect to the previous ongoing session?" +
                        " (yes/no): [default = no]");
            return in.nextLine().equals("yes");
            }
        return false;
    }

    public static void main(String[] args) {

        Client client = new Client();
        TOKEN_FILE_PATH = Paths.get(client.getClass().getClassLoader().getResource("config/token").getPath());
        USERNAME_FILE_PATH = Paths.get(client.getClass().getClassLoader().getResource("config/username").getPath());

        Log.input("Preferred view mode (GUI/CLI): [default = GUI] ");

        String viewMode = client.in.nextLine();

        Log.input("Preferred network communication mode (RMI/Socket): [default = Socket] ");

        String connectionType = client.in.nextLine();

        boolean reconnecting = client.isReconnection();
        String token = null;
        if(reconnecting)
            token = client.readToken();
        Log.fine("Read token: " + token);
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
