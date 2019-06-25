package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.UIMode;
import it.polimi.se2019.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;


public class Client {
    private NetworkHandler networkHandler;
    private View view;
    private Properties properties = new Properties();
    private Properties hidden = new Properties();
    private Scanner in = new Scanner(System.in);
    private List<String> mapConfigs = new ArrayList<>();



    public Client(){
        initializePropertiesAndPreferences();
        viewInitialization();
        networkInitialization();
        view.register(networkHandler);
        networkHandler.register(view);

    }

    private String getServerIP(){
        return properties.getProperty("SERVER_IP");
    }

    private  int getTestUsernameBound(){
        return Integer.parseInt(properties.getProperty("TEST_USERNAME_BOUND"));
    }

    private int getServerPort(){
        return Integer.parseInt(properties.getProperty("SERVER_PORT"));
    }

    private ConnectionMode getConnectionMode(){
        return ConnectionMode.parseConnectionMode(properties.getProperty("CONNECTION_MODE"));
    }

    private UIMode getUiMode(){
        return UIMode.parseUIMode((properties.getProperty("UI_MODE")));
    }

    private boolean isTesting(){
        return Boolean.parseBoolean(properties.getProperty("TESTING"));
    }

    public String getRemoteServerName(){
        return properties.getProperty("SERVER_NAME");
    }

    public String getUsername(){
        return hidden.getProperty("username");
    }

    public String getToken(){
        return hidden.getProperty("token");
    }


    public void writePreference(String property, String value){
        hidden.put(property, value);
        try{
            FileOutputStream f =new FileOutputStream(Client.class.getClassLoader().getResource("hidden.properties").getFile());
            hidden.store(f, "updating");
        }catch (FileNotFoundException e){
            Log.severe("Could not find local properties file");
        }catch (IOException e){
            Log.severe("Could not store data in this hidden property file");
        }
    }

    public void openSession(String token, List<String> roomUsernames, List<String> allUsernames, List<String> configs){
        mapConfigs = configs;
        if(!networkHandler.isReconnection()) {
            networkHandler.setToken(token);
            view.matchMaking(usernameSelection(allUsernames, roomUsernames), configs);
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
            writePreference("token", "");
            networkHandler.stopListening();
            main(new String[]{"a", "b", "c"});
        }
    }

    private List<String> usernameSelection(List<String> allUsernames, List<String> roomUsernames){
        String username;
        if(!isTesting()) {
            in = new Scanner(System.in);

            Log.input("Insert username");

            username = in.nextLine();

            while (allUsernames.contains(username)) {
                Log.input("Choose another username please, '" + getUsername() + "' already in use");
                username = in.nextLine();
            }
        }
        else {
            Random r = new SecureRandom();
            username = ((Integer) r.nextInt(getTestUsernameBound())).toString();
            while(allUsernames.contains(username)){
                username = ((Integer) r.nextInt(getTestUsernameBound())).toString();
            }
        }

        writePreference("username", username);
        networkHandler.chooseUsername(username);

        roomUsernames.add(username);
        roomUsernames.remove("*");
        return roomUsernames;

    }

    private boolean isReconnection() {
        return !isTesting() && (getToken().length()!=0);
    }

    public void viewInitialization(){
        view = getUiMode().createView(this);
    }

    public void networkInitialization(){
        //Ip and port are always given for network handling, they are ignored if connection mode is RMI
        if(!isReconnection())
            networkHandler = getConnectionMode().createNetworkHandler(this, getServerIP(), getServerPort());
        else
            networkHandler = getConnectionMode().createNetworkHandler(this, getServerIP(), getServerPort(), getToken());
    }

    public void initializePropertiesAndPreferences() {
        try {
            properties.load(new FileInputStream(Paths.get("files/client.properties").toFile()));
            properties.load(Client.class.getClassLoader().getResourceAsStream("hidden.properties"));
        } catch (IOException e) {
            Log.severe("Could not load properties file");
        }
    }

    public void setNetworkHandler(NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public static void main(String[] args) {
        new Client();
    }
}
