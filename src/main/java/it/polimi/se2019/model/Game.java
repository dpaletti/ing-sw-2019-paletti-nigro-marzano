package it.polimi.se2019.model;

import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Observer;

import java.util.List;

public class Game extends Observable<Game> implements Observer<Action> {
    private GameMap gameMap;
    private KillshotTrack killshotTrack;
    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
    private static Game instance;
    private List<Player> players;
    private Turn turn;

    @Override
    public void update(Action message) {}

    public static Game getInstance() {
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

    public GameMap getGameMap() {
        return gameMap;
    }

    public KillshotTrack getKillshotTrack() {
        return killshotTrack;
    }

    public List<Player> getPlayers() { return players; }

    public Turn getTurn() { return turn; }

    public void setAmmoDeck(Deck ammoDeck) {
        this.ammoDeck = ammoDeck;
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

    public static void setInstance(Game instance) { Game.instance = instance; }

    public void setPlayers(List<Player> players) { this.players = players; }

    public void setTurn(Turn turn) { this.turn = turn; }
}
