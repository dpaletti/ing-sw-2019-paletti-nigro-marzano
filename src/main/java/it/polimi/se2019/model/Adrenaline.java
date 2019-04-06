package it.polimi.se2019.model;

import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;

public class Adrenaline extends Observable<Adrenaline> implements Observer<Action> {
    private GameHistory gameHistory;
    private GameMap gameMap;
    private KillshotTrack killshotTrack;
    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
    private static Adrenaline instance;

    public void startGame(){};
    public void endTurn(){};
    public void endGame(){};

    @Override
    public void update(Action message) {

    }

    public static Adrenaline getInstance() {
        return instance;
    }

    public Deck getAmmoDeck() {
        return ammoDeck;
    }

    public Deck getPowerUpDeck() {
        return powerUpDeck;
    }

    public Deck getWeaponDeck() {
        return weaponDeck;
    }

    public GameHistory getGameHistory() {
        return gameHistory;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public KillshotTrack getKillshotTrack() {
        return killshotTrack;
    }

    public void setAmmoDeck(Deck ammoDeck) {
        this.ammoDeck = ammoDeck;
    }

    public void setGameHistory(GameHistory gameHistory) {
        this.gameHistory = gameHistory;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void setKillshotTrack(KillshotTrack killshotTrack) {
        this.killshotTrack = killshotTrack;
    }

    public void setPowerUpDeck(Deck powerUpDeck) {
        this.powerUpDeck = powerUpDeck;
    }

    public void setWeaponDeck(Deck weaponDeck) {
        this.weaponDeck = weaponDeck;
    }

}
