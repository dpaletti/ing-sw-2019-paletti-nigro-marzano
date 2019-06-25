package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.MVEvent;

import java.util.HashMap;
import java.util.Map;

//sends everyone a map between usernames and colours, the weapons on each weaponSpot and the position of each loot card

public class SetUpEvent extends MVEvent {
    private HashMap<String, String> userToColour; //<user, figure colour>
    private HashMap<String, String> weaponSpots;    //<weapon, room colour>
    private HashMap<Point, String> lootCards;
    private String leftConfig;
    private String rightConfig;
    private int skulls;
    private boolean frenzy;

    public SetUpEvent(String destination,
                      Map<String, String> userToColour,
                      Map<String, String> weaponSpots,
                      Map<Point, String> lootCards,
                      int skulls,
                      String leftConfig,
                      String rightConfig,
                      boolean frenzy){
        super(destination);
        this.userToColour=new HashMap<>(userToColour);
        this.weaponSpots= new HashMap<>(weaponSpots);
        this.lootCards= new HashMap<>(lootCards);
        this.skulls = skulls;
        this.leftConfig = leftConfig;
        this.rightConfig = rightConfig;
        this.frenzy = frenzy;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public Map<String, String> getUserToColour() {
        return new HashMap<>(userToColour);
    }

    public Map<String, String> getWeaponSpots() {
        return new HashMap<>(weaponSpots);
    }

    public Map<Point, String> getLootCards() {
        return new HashMap<>(lootCards);
    }

    public String getLeftConfig() {
        return leftConfig;
    }

    public String getRightConfig() {
        return rightConfig;
    }

    public int getSkulls() {
        return skulls;
    }

    public boolean isFrenzy() {
        return frenzy;
    }

}
