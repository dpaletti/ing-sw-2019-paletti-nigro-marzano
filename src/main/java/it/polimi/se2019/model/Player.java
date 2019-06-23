package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.utility.Action;
import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Point;

import java.util.*;

public class Player extends Observable<Action> implements Targetable{
    private Figure figure;
    private boolean isPaused= false;
    private List<Tear> hp= new ArrayList<>();
    private PlayerDamage healthState= new Healthy();
    private PlayerValue playerValue= new NoDeaths();
    private Set<Tear> marks= new HashSet<>();
    private List<Weapon> weapons=new ArrayList<>();
    private List<PowerUp> powerUps= new ArrayList<>();
    private PowerUp temporaryPowerUp= null;
    private Integer points= 0;
    private Set<Ammo> ammo = new HashSet<>();
    private Game game;

    public Player (Figure figure, Game game){
        this.figure= figure;
        this.game= game;
        for (AmmoColour ammoColour: AmmoColour.values()){
            for (int i=0; i<3; i++){
                ammo.add(new Ammo(ammoColour));
            }
        }
        this.figure.setPlayer(this);
    }

    @Override
    public void hit(String partialWeaponEffect, List<Targetable> hit, TurnMemory turnMemory) {
        List<Player> list = new ArrayList<>();
        for (Targetable t: hit)
           list.add((Player) t);
        turnMemory.putPlayers(partialWeaponEffect, list);
        turnMemory.setLastEffectUsed(partialWeaponEffect);
    }

    @Override
     public List<Targetable> getByEffect(List<String> effects, TurnMemory turnMemory) {
        List<Targetable> hit= new ArrayList<>();
        for (String s: effects){
            hit.addAll(turnMemory.getHitTargets().get(s));
        }
        return hit;
    }

    @Override
    public List<Targetable> getAll() {
        return new ArrayList<>(game.getPlayers());
    }

    @Override
    public Point getPosition() {
        return figure.getPosition();
    }

    @Override
    public Map<String, List<Targetable>> getHitTargets(TurnMemory turnMemory) {
        List<Targetable> list;
        Map<String, List<Targetable>> map = new HashMap<>();
        for (String s: turnMemory.getHitTargets().keySet()) {
            list = new ArrayList<>(turnMemory.getHitTargets().get(s));
            map.put(s, list);
        }
        return map;
    }

    @Override
    public void addToSelectionEvent(MVSelectionEvent event, List<Targetable> targets, List<Action> actions) {
        List<Player> players = new ArrayList<>(toPlayerList(targets));
        List<String> usernames = new ArrayList<>();
        for(Player p: players){
            usernames.add(game.playerToUser(p));
        }
        event.addActionOnPlayer(actions, usernames);
    }

    private List<Player> toPlayerList(List<Targetable> list){
        List<Player> players = new ArrayList<>();
        for (Targetable t: list) {
            players.add((Player) t);
        }
        return players;
    }

    public GameMap getGameMap (){
        return game.getGameMap();
    }

    public void unpause (){
        if (!isPaused){
            throw new UnsupportedOperationException("This player is already unpaused");
        }
        isPaused= false;
        game.send(new UnpausedPlayerEvent("*", game.colourToUser(figure.getColour())));
    }

    public void pause(){
        if (isPaused){
            throw new UnsupportedOperationException("This player is already paused");
        }
        isPaused= true;
        game.send(new PausedPlayerEvent("*", game.colourToUser(figure.getColour())));
    }


    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setAmmo(Set<Ammo> ammo) {
        this.ammo = ammo;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public Figure getFigure() {
        return figure;
    }

    public PlayerDamage getHealthState() {
        return healthState;
    }

    public List<PowerUp> getPowerUps() {
        return new ArrayList<>(powerUps);
    }

    public List<Tear> getHp() {
        return hp;
    }

    public Set<Tear> getMarks() {
        return marks;
    }

    public Set<Ammo> getAmmo() {
        return ammo;
    }

    public PlayerValue getPlayerValue() {
        return playerValue;
    }

    public void setMarks(Set<Tear> marks) {
        this.marks = marks;
    }

    public void setHp(List<Tear> hp) {
        this.hp = hp;
    }

    public void run (Point destination, int distance) {
        figure.run(destination, distance);
        game.send(new MVMoveEvent("*", game.colourToUser(figure.getColour()), destination));
    }

    public void grabStuff(String grabbed){
        figure.grab(grabbed);
    }


    public void reload(Weapon weapon){
        figure.reload(weapon);
    }

    public void usePowerUp (String powerUp){
        //TODO: use power up
        deletePowerUp(powerUp);
    }

    public void sellPowerUp (String powerUp){
        if (powerUpIsOwned(powerUp))
            throw new UnsupportedOperationException("Could not sell " + powerUp + "as player doesn't own it");
        ammo.add(new Ammo(game.nameToPowerUp(powerUp).getCardColour().getColour()));
        deletePowerUp(powerUp);
    }

    public void deletePowerUp (String powerUp){
        if (powerUpIsOwned(powerUp))
            throw new UnsupportedOperationException("Could not delete" + powerUp + "as player doesn't own it");
        powerUps.remove(game.nameToPowerUp(powerUp));
        game.discardedPowerUp(this, "none", powerUp);
    }

    private boolean powerUpIsOwned (String powerUp){
        for (PowerUp p: powerUps){
            if (p.name.equalsIgnoreCase(powerUp))
                return true;
        }
        return false;
    }

    void addTear (FigureColour figureColour){
        Tear tear= new Tear(figureColour);
        hp.add(tear);
        game.send(new UpdateHpEvent("*", game.colourToUser(figure.getColour()), game.colourToUser(figureColour)));
    }

    void addMark (FigureColour figureColour){
        Tear tear= new Tear(figureColour);
        marks.add(tear);
        game.send(new UpdateMarkEvent("*", game.colourToUser(figure.getColour()), game.colourToUser(figureColour)));
    }

    void updatePlayerDamage (){
        if(healthState.getMaximumHits()==hp.size()){
            healthState= healthState.findNextHealthState();
        }
        if (hp.size()>=10)
                game.deathHandler(this);
    }

    void updatePlayerDamage (PlayerDamage playerDamage){
        healthState= playerDamage;
    }


    public void updatePointsToAssign (){
        playerValue= playerValue.getNextPlayerValue();
    }

    public void drawPowerUp (String drawnPowerUp){
        if (powerUps.size()==4)
            throw new UnsupportedOperationException("Discard a powerup before drawing one");
        powerUps.add(game.nameToPowerUp(drawnPowerUp));
        if (powerUps.size()==4) {
            game.chosePowerUpToDiscard(this, powerUps);
        }
        else if (powerUps.size()<4)
            powerUps.add(game.nameToPowerUp(drawnPowerUp));
    }

    public void discardPowerUp (String discardedPowerUp){
        if (!powerUpIsOwned(discardedPowerUp))
            throw new UnsupportedOperationException("Could not discard" + discardedPowerUp + "as player doesn't own it");
        game.discardedPowerUp(this, powerUps.get(4).name, discardedPowerUp);
        powerUps.remove(game.nameToPowerUp(discardedPowerUp));
    }

    public void useAmmos(Set<Ammo> usedAmmo){
       for (Ammo a : usedAmmo)
           useAmmo(a);
    }

    public void useAmmo(Ammo usedAmmo){
        ammo.remove(usedAmmo);
    }

    public void addAmmo (Ammo ammo){
        this.ammo.add(ammo);
    }

    public void drawPowerUp (){
        drawPowerUp(game.getPowerUpDeck().draw().getName());
    }

    public boolean pay (Set<Ammo> ammoToPay){
        Set<Ammo> ammosOwned = new HashSet<>(ammoToPay);
        for  (Ammo a : ammoToPay){
            if (ammosOwned.contains(a))
                ammosOwned.remove(a);
            else
                return false;
        }
        useAmmos(ammoToPay);
        return true;
    }

    public void addWeapon (Weapon weapon){
        if (weapons.size() == 3) {
           // game.send(/*new LeaveOneGrabbableEvent ()*/);
            return;
        }
        weapons.add(weapon);
    }
}
