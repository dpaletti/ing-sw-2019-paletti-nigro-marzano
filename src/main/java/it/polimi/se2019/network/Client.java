package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.ViewCLI;
import it.polimi.se2019.view.ViewGUI;

import java.util.Scanner;


public class Client {
    private NetworkHandler networkHandler;
    private View view;

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public static void main(String[] args) {
        //TODO manage reconnection by logging connection token on a file
        Client client = new Client();

        Scanner in = new Scanner(System.in);

        Log.input("Preferred view mode (GUI/CLI): ");

        String viewMode = in.nextLine();

        Log.input("Insert username");

        String username = in.nextLine();

        Log.input("Preferred network communication mode (RMI/Socket): ");
        String connectionType = in.nextLine();

        if(!viewMode.equals("CLI"))
            client.view = new ViewGUI();
        else
            client.view = new ViewCLI();

        if (!connectionType.equals("RMI")) {
            Log.input("input Server IP (with no spaces) then press Enter: ");
            String ip = in.nextLine();

            Log.input("input Server port then press Enter: ");
            int port = in.nextInt();
            in.nextLine();
                client.networkHandler = new NetworkHandlerSocket(username, ip, port);
        }
        else {
                client.networkHandler = new NetworkHandlerRMI(username);
            }
        client.view.register(client.networkHandler);
        client.networkHandler.register(client.view);
    }
}
