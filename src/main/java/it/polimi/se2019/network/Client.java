package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;
import it.polimi.se2019.view.ViewCLI;
import it.polimi.se2019.view.ViewGUI;

import java.util.Scanner;


public class Client {
    private NetworkHandler networkHandler;
    //TODO encrypt in a file the password
    //TODO read it like a password

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public static void main(String[] args) {
        Client client = new Client();

        Scanner in = new Scanner(System.in);

        Log.input("Preferred view mode (GUI/CLI): ");

        String viewMode = in.nextLine();

        Log.input("Insert username");

        String username = in.nextLine();

        Log.input("Insert password");

        String password = in.nextLine();

        Log.input("Preferred network communication mode (RMI/Socket): ");
        String connectionType = in.nextLine();

        if (!connectionType.equals("RMI")) {
            Log.input("input Server IP (with no spaces) then press Enter: ");
            String ip = in.nextLine();

            Log.input("input Server port then press Enter: ");
            int port = in.nextInt();
            in.nextLine();
            if(viewMode.equals("CLI"))
                client.networkHandler = new NetworkHandlerSocket(username, password, ip, port, new ViewCLI());
            else

                client.networkHandler = new NetworkHandlerSocket(username, password, ip, port, new ViewGUI());
        }
        else {
            if(viewMode.equals("CLI"))
                client.networkHandler = new NetworkHandlerRMI(username, password, new ViewCLI());
            else
                client.networkHandler = new NetworkHandlerRMI(username, password, new ViewGUI());
        }
    }
}
