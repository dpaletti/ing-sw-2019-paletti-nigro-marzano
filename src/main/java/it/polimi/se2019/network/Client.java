package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.ViewCLI;
import it.polimi.se2019.view.ViewGUI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;


public class Client {
    private NetworkHandler networkHandler;
    private View view;
    private Properties properties = new Properties();
    private Scanner in = new Scanner(System.in);

    private Properties getProperties() {
        return new Properties(properties);
    }

    public String getRemoteServerName(){
        return properties.getProperty("REMOTE_SERVER_NAME");
    }

    public String getUsername(){
        return properties.getProperty("username");
    }

    public String getToken(){
        return properties.getProperty("token");
    }

    public void writeProperty(String property, String value){
        properties.setProperty(property, value);
        try {
            properties.store(new FileOutputStream(Paths.get("files/server.properties").toFile()), "updating properties");
        }catch (Exception e){
            Log.severe("Could not write property file");
        }
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
            writeProperty("token", "");
            networkHandler.stopListening();
            main(new String[]{"a", "b", "c"});
        }
    }

    private void usernameSelection(List<String> usernames){
        in = new Scanner(System.in);

        Log.input("Insert username");

        String username  = in.nextLine();

        while (usernames.contains(username)) {
            Log.input("Choose another username please, '" + properties.getProperty("username") + "' already in use");
            username = in.nextLine();
        }

        writeProperty(username, username);
        networkHandler.chooseUsername(username);

        usernames.add(username);
        usernames.remove("*");

        view.matchMaking(usernames);
    }

    private boolean isReconnection() {
        if (getToken().length() == 0) {
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
        if(!viewMode.equals("CLI")) {
            ViewGUI.create(this);
            view = ViewGUI.getInstance();
        }
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

    public void fillProperties() {
        try {
            properties.load(new FileInputStream(Paths.get("files/client.properties").toFile()));
        } catch (IOException e) {
            Log.severe("Could not load properties file");
        }
    }
    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
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
            token = client.getToken();

        client.viewInitialization(viewMode);
        //view needs to be initialized before network
        //nullPointer exception is thrown otherwise
        client.networkInitialization(connectionType, token, reconnecting);
        client.view.register(client.networkHandler);
    }
}
