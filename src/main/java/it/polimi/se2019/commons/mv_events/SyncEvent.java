package it.polimi.se2019.commons.mv_events;

import it.polimi.se2019.client.view.MVEvent;
import it.polimi.se2019.commons.utility.MVEventDispatcher;
import it.polimi.se2019.commons.utility.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This event allows users to obtain all the missing information whenever they reconnect.
 * See {@link it.polimi.se2019.client.view.MVEvent}.
 */

public class SyncEvent extends MVEvent {
    private HashMap<Point, String> pointColorSpawnMap;
    private HashMap<Point, ArrayList<String>> figurePosition;
    private HashMap<String, String> weaponSpots; //weapon to roomColour
    private HashMap<Point, String> lootSpots;
    private HashMap<String, ArrayList<String>> hp;
    private HashMap<String, ArrayList<String>> mark;
    private HashMap<String, ArrayList<String>> weapons;
    private HashMap<String, String> colours;
    private HashMap<String, ArrayList<String>> finance;
    private ArrayList<String> powerup;
    private HashMap<String, Integer> points;
    private int skulls; //+
    private ArrayList<String> paused;
    private ArrayList<String> usernames;
    private ArrayList<String> configs;
    private String leftConfig;
    private String rightConfig;
    private boolean isFrenzy;

    public SyncEvent(String destination,
                     Map<Point, String> pointColorSpawnMap,
                     Map<Point, ArrayList<String>> figurePosition,
                     Map<String, String> weaponSpots,
                     Map<Point, String> lootSpots,
                     Map<String, ArrayList<String>> hp,
                     Map<String, ArrayList<String>> mark,
                     Map<String, ArrayList<String>> weapons,
                     Map<String, String> colours, Map<String,
                     ArrayList<String>> finance, List<String> powerup,
                     Map<String, Integer> points, int skulls,
                     List<String> paused,
                     List<String> usernames,
                     List<String> configs,
                     String leftConfig,
                     String rightConfig,
                     boolean isFrenzy) {
        super(destination);
        this.pointColorSpawnMap = new HashMap<>(pointColorSpawnMap);
        this.figurePosition = new HashMap<>(figurePosition);
        this.weaponSpots = new HashMap<>(weaponSpots);
        this.lootSpots = new HashMap<>(lootSpots);
        this.hp = new HashMap<>(hp);
        this.mark = new HashMap<>(mark);
        this.weapons = new HashMap<>(weapons);
        this.colours = new HashMap<>(colours);
        this.finance = new HashMap<>(finance);
        this.powerup = new ArrayList<>(powerup);
        this.points = new HashMap<>(points);
        this.skulls = skulls;
        this.paused = new ArrayList<>(paused);
        this.usernames = new ArrayList<>(usernames);
        this.configs = new ArrayList<>(configs);
        this.leftConfig = leftConfig;
        this.rightConfig = rightConfig;
        this.isFrenzy = isFrenzy;
    }

    public HashMap<Point, String> getPointColorSpawnMap() {
        return pointColorSpawnMap;
    }

    public boolean isFrenzy() {
        return isFrenzy;
    }

    public String getLeftConfig() {
        return leftConfig;
    }

    public String getRightConfig() {
        return rightConfig;
    }

    public HashMap<Point, ArrayList<String>> getFigurePosition() {
        return figurePosition;
    }

    public HashMap<String, String> getWeaponSpots() {
        return weaponSpots;
    }

    public HashMap<Point, String> getLootSpots() {
        return lootSpots;
    }

    public HashMap<String, ArrayList<String>> getHp() {
        return hp;
    }

    public HashMap<String, ArrayList<String>> getMark() {
        return mark;
    }

    public HashMap<String, ArrayList<String>> getWeapons() {
        return weapons;
    }

    public HashMap<String, String> getColours() {
        return colours;
    }

    public HashMap<String, ArrayList<String>> getFinance() {
        return finance;
    }

    public ArrayList<String> getPowerup() {
        return powerup;
    }

    public HashMap<String, Integer> getPoints() {
        return points;
    }

    public int getSkulls() {
        return skulls;
    }

    public ArrayList<String> getPaused() {
        return paused;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public ArrayList<String> getConfigs() {
        return configs;
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
