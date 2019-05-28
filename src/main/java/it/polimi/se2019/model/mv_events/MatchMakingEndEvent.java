package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.model.FigureColour;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchMakingEndEvent extends MVEvent {
    private int boardConfiguration;
    private HashMap<String, FigureColour> userToColour;
    private ArrayList<String> weaponSpots;
    private ArrayList<String> lootCards;
    private int numberOfSkulls;

    public MatchMakingEndEvent (String destination,
                                int boardConfiguration,
                                Map<String, FigureColour> userToColour,
                                List<String> weaponSpots,
                                List<String> lootCards,
                                int numberOfSkulls){
        super(destination);
        this.boardConfiguration=boardConfiguration;
        this.userToColour=new HashMap<>(userToColour);
        this.weaponSpots= new ArrayList<>(weaponSpots);
        this.lootCards= new ArrayList<>(lootCards);
        this.numberOfSkulls= numberOfSkulls;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.update(this);
    }

    public Map<String, FigureColour> getUserToColour() {
        return new HashMap<>(userToColour);
    }

    public int getBoardConfiguration() {
        return boardConfiguration;
    }

    public List<String> getWeaponSpots() {
        return new ArrayList<>(weaponSpots);
    }

    public ArrayList<String> getLootCards() {
        return new ArrayList<>(lootCards);
    }

    public int getNumberOfSkulls() {
        return numberOfSkulls;
    }
}
