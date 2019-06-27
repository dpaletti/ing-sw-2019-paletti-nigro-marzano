package it.polimi.se2019.model.mv_events;

import it.polimi.se2019.utility.MVEventDispatcher;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.MVEvent;

import java.util.HashMap;
import java.util.Map;

//first player receives drawn pUps, must discard one of the two and spawn on the colour of the discarded pUp

public class StartFirstTurnEvent extends MVEvent {
    private String firstPowerUpName;
    private String secondPowerUpName;
    private boolean isFirstPlayer;
    private HashMap<Point, String> spawnPoints; //keeps relationship between SpawnPoint and color
    public StartFirstTurnEvent(String destination,
                               String firstPowerUpName,
                               String secondPowerUpName,
                               boolean isFirstPlayer,
                               Map<Point, String> spawnPoints){
        super(destination);
        this.firstPowerUpName=firstPowerUpName;
        this.secondPowerUpName=secondPowerUpName;
        this.isFirstPlayer=isFirstPlayer;
        this.spawnPoints = new HashMap<>(spawnPoints);
    }

    @Override
    public void handle(MVEventDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    public String getFirstPowerUpName() {
        return firstPowerUpName;
    }

    public String getSecondPowerUpName() {
        return secondPowerUpName;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public Map<Point, String> getSpawnPoints() {
        return new HashMap<>(spawnPoints);
    }
}
