package it.polimi.se2019.server.network;


import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.client.view.VCEvent;

/**
 * Connection generalization over the possible connection types
 */
public interface Connection {
    /**
     * Submits a message to the other end of the connection
     * @param mvEvent message to submit
     */
    void submit(MVEvent mvEvent);

    /**
     * retrieves a message from the connection
     * @return message retrieved
     */
    VCEvent retrieve();
    String getToken();

    /**
     * disables commmunication along this connection
     */
    void disconnect();

    /**
     * reconnects the connection to a running game through a sync event
     * @param reconnectionEvent
     * @param roomNumber
     */
    void reconnect(MVEvent reconnectionEvent, int roomNumber);
}
