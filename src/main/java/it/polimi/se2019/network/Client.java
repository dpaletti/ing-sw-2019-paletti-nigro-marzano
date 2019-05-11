package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.ViewCLI;
import it.polimi.se2019.view.ViewGUI;

import java.util.List;
import java.util.Scanner;


public class Client {
    private NetworkHandler networkHandler;
    private View view;
    private String username;

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public void setSessionToken(String token){
        networkHandler.setToken(token);
    }

    public void usernameSelection(List<String> usernames){
        Scanner in = new Scanner(System.in);

        Log.input("Insert username");

        username = in.nextLine();

        while(usernames.contains(username)){
            Log.input("Choose another username please, '" + username + "' already in use");
            username = in.nextLine();
        }

        networkHandler.chooseUsername(username);

    }

    public static void main(String[] args) {
        //TODO manage reconnection by logging connection token on a file
        Client client = new Client();

        Scanner in = new Scanner(System.in);

        Log.input("Preferred view mode (GUI/CLI): [default = GUI] ");

        String viewMode = in.nextLine();

        Log.input("Preferred network communication mode (RMI/Socket): [default = Socket] ");

        String connectionType = in.nextLine();

        if(!viewMode.equals("CLI"))
            client.view = new ViewGUI(client);
        else
            client.view = new ViewCLI(client);

        if (!connectionType.equals("RMI")) {
            Log.input("input Server IP (with no spaces) then press Enter: ");
            String ip = in.nextLine();

            Log.input("input Server port then press Enter: ");
            int port = in.nextInt();
            in.nextLine();
                client.networkHandler = new NetworkHandlerSocket(client.username, ip, port);
        }
        else {
                client.networkHandler = new NetworkHandlerRMI();
            }
        client.view.register(client.networkHandler);
        client.networkHandler.register(client.view);
    }
}
