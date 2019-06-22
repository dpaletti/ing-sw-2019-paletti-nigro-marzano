package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.model.FigureColour;
import it.polimi.se2019.model.RoomColour;
import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.MVEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//sends everyone a map between usernames and colours, the weapons on each weaponSpot and the position of each loot card

public class SetUpEvent extends MVEvent {
    private HashMap<String, FigureColour> userToColour;
    private HashMap<String, RoomColour> weaponSpots;
    private HashMap<Point, String> lootCards;

    public SetUpEvent(String destination,
                      Map<String, FigureColour> userToColour,
                      Map<String, RoomColour> weaponSpots,
                      Map<Point, String> lootCards){
        super(destination);
        this.userToColour=new HashMap<>(userToColour);
        this.weaponSpots= new HashMap<>(weaponSpots);
        this.lootCards= new HashMap<>(lootCards);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public Map<String, FigureColour> getUserToColour() {
        return new HashMap<>(userToColour);
    }

    public Map<String, RoomColour> getWeaponSpots() {
        return new HashMap<>(weaponSpots);
    }

    public Map<Point, String> getLootCards() {
        return new HashMap<>(lootCards);
    }

}
