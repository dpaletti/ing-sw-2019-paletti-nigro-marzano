package it.polimi.se2019.client.view;

import it.polimi.se2019.client.network.Client;
import it.polimi.se2019.commons.mv_events.*;
import it.polimi.se2019.commons.utility.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User interface manager, is to be extended for adopting different UIs
 */
public abstract class View extends Observable<Event> implements Observer<MVEvent>, MVEventDispatcher {
    //View is observable of VCEvent and GuiEvents to facilitate gui management
    //TODO see whether CliEvents make sense
    protected Client client;

    public View(){

    }

    public View(Client client){
        this.client = client;
        observers = new ArrayList<>();

    }

    /**
     * View initialization method that informs the user about the start of the matchmaking
     * @param usernames usernames of all players in matchmaking
     * @param configs maps to show the user
     */
    public abstract void matchMaking(List<String> usernames, List<String> configs);


    /**
     * Method for adding a player to matchMaking
     * @param username username of the player to be added
     * @param missingPlayers players missing to begin the closing phase for matchMaking (e.g. timer)
     */
    public abstract void addPlayer(String username, int missingPlayers);

    /**
     * MatchMaking official end and SetUp phase start (players choosing match configuration)(
     * @param message serves to signal matchMaking wrap up
     */
    @Override
    public void dispatch(SetUpEvent message) {
        Log.fine("MatchMaking is over");
    }

    /**
     * End of handshaking phase, the client owns its token now
     * @param message message containing usernames, maps and token
     */
    @Override
    public void dispatch(HandshakeEndEvent message) {
        client.openSession(message.getDestination(), message.getRoomUsernames(), message.getAllUsernames(), message.getConfigs());
    }

    /**
     * Notification for new player join
     * @param message contains username and number of missing players
     */

    @Override
    public void dispatch(MvJoinEvent message) {
        addPlayer(message.getUsername(), message.getMissingPlayers());
    }
}
