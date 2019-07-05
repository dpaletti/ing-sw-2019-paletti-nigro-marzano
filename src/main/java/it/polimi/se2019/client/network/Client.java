package it.polimi.se2019.client.network;

import it.polimi.se2019.client.view.UIMode;
import it.polimi.se2019.client.view.View;
import it.polimi.se2019.commons.utility.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;

/**
 * Orchestrator class for client-side logic manages client startup and minor internal communications
 * among View and NetworkHandler.
 *
 */
public class Client {
    private NetworkHandler networkHandler;
    private View view;
    private Properties properties = new Properties();
    private Scanner in = new Scanner(System.in);
    private List<String> mapConfigs = new ArrayList<>();
    private String username;
    private String token;

    /**
     * Constructor
     */
    public Client() {
        initializePropertiesAndPreferences();
        viewInitialization();
        networkInitialization(view);

    }

    public String getServerIP() {
        return properties.getProperty("SERVER_IP");
    }

    private int getTestUsernameBound() {
        return Integer.parseInt(properties.getProperty("TEST_USERNAME_BOUND"));
    }

    private int getServerPort() {
        return Integer.parseInt(properties.getProperty("SERVER_PORT"));
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    private ConnectionMode getConnectionMode() {
        return ConnectionMode.parseConnectionMode(properties.getProperty("CONNECTION_MODE"));
    }

    private UIMode getUiMode() {
        return UIMode.parseUIMode((properties.getProperty("UI_MODE")));
    }

    private boolean isTesting() {
        return Boolean.parseBoolean(properties.getProperty("TESTING"));
    }

    public String getRemoteServerName() {
        return properties.getProperty("SERVER_NAME");
    }

    /**
     * This methods opens a new session by setting the connection token received from server and guarantees
     * that username do not repeat
     * @param token connection token received through HandshakeEndEvent
     * @param roomUsernames usernames of other players in the same room
     * @param allUsernames usernames from all rooms currently active on the server
     * @param configs Map configs for UI startup
     */
    public void openSession(String token, List<String> roomUsernames, List<String> allUsernames, List<String> configs) {
        mapConfigs = configs;
        networkHandler.setToken(token);
        view.matchMaking(usernameSelection(allUsernames, roomUsernames), configs);
    }


    /**
     * Username selection method that guarantees that username do not repeat
     * @param allUsernames usernames from all rooms currently active on the server
     * @param roomUsernames usernames of other players in the same room
     * @return all usernames, including the one just chosen, in the room
     */
    private List<String> usernameSelection(List<String> allUsernames, List<String> roomUsernames) {
        String localUsername;
        if (!isTesting()) {
            in = new Scanner(System.in);

            Log.input("Insert username");

            localUsername = in.nextLine();

            while (allUsernames.contains(localUsername)) {
                Log.input("Choose another username please, '" + localUsername + "' already in use");
                localUsername = in.nextLine();
            }
        } else {
            Random r = new SecureRandom();
            localUsername = ((Integer) r.nextInt(getTestUsernameBound())).toString();
            while (allUsernames.contains(localUsername)) {
                localUsername = ((Integer) r.nextInt(getTestUsernameBound())).toString();
            }
        }


        username = localUsername;

        networkHandler.chooseUsername(localUsername);

        roomUsernames.add(localUsername);
        roomUsernames.remove("*");
        return roomUsernames;

    }


    /**
     * UI initialization
     */
    public void viewInitialization() {
        view = getUiMode().createView(this);
    }


    /**
     * Network Handler initialization
     * @param view view that the network handler is going to be talking to
     */
    public void networkInitialization(View view) {
        //Ip and port are always given for it.polimi.se2019.client.network handling, they are ignored if connection mode is RMI
        networkHandler = getConnectionMode().createNetworkHandler(this, getServerIP(), getServerPort(), view);
    }

    /**
     * client.properties file loading
     */

    public void initializePropertiesAndPreferences() {
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
        new Client();
    }
}
