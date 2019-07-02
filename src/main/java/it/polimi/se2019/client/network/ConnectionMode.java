package it.polimi.se2019.client.network;

import it.polimi.se2019.client.view.View;

public enum ConnectionMode {
    RMI{
        @Override
        public NetworkHandler createNetworkHandler(Client client, String ip, int port, View view) {
            return new NetworkHandlerRMI(client, view);
        }

    },
    SOCKET{
        @Override
        public NetworkHandler createNetworkHandler(Client client, String ip, int port, View view) {
            return new NetworkHandlerSocket(client, ip, port, view);
        }

    };
    private static final String ERROR_MESSAGE = "Could not create handler, wrong parameters";
    public abstract NetworkHandler createNetworkHandler(Client client, String ip, int port, View view);
    public static ConnectionMode parseConnectionMode(String toParse){
        if(toParse.equals("RMI"))
            return ConnectionMode.RMI;
        else
            return ConnectionMode.SOCKET;
    }
}
