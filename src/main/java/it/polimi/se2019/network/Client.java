package it.polimi.se2019.network;

import it.polimi.se2019.utility.Log;

import java.util.Scanner;


public class Client {
    private NetworkHandler networkHandler;

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public static void main(String[] args) {
        Client client = new Client();

        Scanner in = new Scanner(System.in);

        Log.input("Preferred network communication mode (RMI/Socket): ");
        String connectionType = in.nextLine();

        if (!connectionType.equals("RMI")) {
            Log.input("input Server IP (with no spaces) then press Enter: ");
            String ip = in.nextLine();

            Log.input("input Server port then press Enter: ");
            int port = in.nextInt();
            in.nextLine();

            client.networkHandler = new NetworkHandlerSocket(ip, port);
        }
        else
            client.networkHandler = new NetworkHandlerRMI();
    }
}
