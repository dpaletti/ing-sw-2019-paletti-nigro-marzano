package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.ViewCLI;
import it.polimi.se2019.view.ViewGUI;

import java.io.IOException;
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
        usernames.add(username);
        usernames.remove("*");
        view.matchMaking(usernames);
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

    public void getViewRegistration(NetworkHandler networkHandler){
            Log.fine(view.toString());
            networkHandler.register(view);
    }

    public void viewInitialization(String viewMode){
        if(!viewMode.equals("CLI"))
            view = new ViewGUI(this);
        else
            view = new ViewCLI(this);


    }

    public void networkInitialization(String connectionType, String token, boolean reconnecting){
        if (!connectionType.equals("RMI")) {
            Log.input("input Server IP (with no spaces) then press Enter: ");
            String ip = in.nextLine();

            Log.input("input Server port then press Enter: ");
            int port = in.nextInt();
            in.nextLine();
            if(!reconnecting)
                networkHandler = new NetworkHandlerSocket(this, ip, port);
            else
                networkHandler = new NetworkHandlerSocket(token, this, ip, port);

        }
        else {
            if(!reconnecting)
                networkHandler = new NetworkHandlerRMI(this);
            else
                networkHandler = new NetworkHandlerRMI(token, this);
        }
    }

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public static void main(String[] args) {

        Client client = new Client();
        try {
            TOKEN_FILE_PATH = Paths.get(Client.class.getClassLoader().getResource("config/token").getPath());
            USERNAME_FILE_PATH = Paths.get(Client.class.getClassLoader().getResource("config/username").getPath());
        }catch (NullPointerException e){
            Log.severe("Could not find files for token and username");
        }

        Log.input("Preferred view mode (GUI/CLI): [default = GUI] ");

        String viewMode = client.in.nextLine();

        Log.input("Preferred network communication mode (RMI/Socket): [default = Socket] ");

        String connectionType = client.in.nextLine();

        boolean reconnecting = client.isReconnection();
        String token = null;
        if(reconnecting)
            token = client.readToken();

        client.viewInitialization(viewMode);
        //view needs to be initialized before network
        //nullPointer exception is thrown otherwise
        client.networkInitialization(connectionType, token, reconnecting);
        client.view.register(client.networkHandler);
    }
}
