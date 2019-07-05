package it.polimi.se2019.client.network;

import it.polimi.se2019.client.view.View;

/**
 * enum containing supported connection types
 */
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

    /**
     * Initializer for network handler, creates the right handler given the connection type.
     * @param client orchestrating class
     * @param ip Server ip (from .properties file)
     * @param port Server port (from .properties file)
     * @param view Listener for messages coming from the network
     * @return Newly created NetworkHandler
     */
    public abstract NetworkHandler createNetworkHandler(Client client, String ip, int port, View view);

    /** Parser for connection types
     *
     * @param toParse String to parse into a ConnectionMode
     * @return parsed ConnectionMode
     */
    public static ConnectionMode parseConnectionMode(String toParse){
        if(toParse.equals("RMI"))
            return ConnectionMode.RMI;
        else
            return ConnectionMode.SOCKET;
    }
}
