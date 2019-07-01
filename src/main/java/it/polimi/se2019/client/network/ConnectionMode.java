package it.polimi.se2019.client.network;

public enum ConnectionMode {
    RMI{
        @Override
        public NetworkHandler createNetworkHandler(Client client, String ip, int port) {
            return new NetworkHandlerRMI(client);
        }

    },
    SOCKET{
        @Override
        public NetworkHandler createNetworkHandler(Client client, String ip, int port) {
            return new NetworkHandlerSocket(client, ip, port);
        }

    };
    private static final String ERROR_MESSAGE = "Could not create handler, wrong parameters";
    public abstract NetworkHandler createNetworkHandler(Client client, String ip, int port);
    public static ConnectionMode parseConnectionMode(String toParse){
        if(toParse.equals("RMI"))
            return ConnectionMode.RMI;
        else
            return ConnectionMode.SOCKET;
    }
}
