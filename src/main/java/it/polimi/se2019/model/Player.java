package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.MVSelectionEvent;
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
    private Weapon firstWeapon;
    private Weapon secondWeapon;
    private Weapon thirdWeapon;
    private PowerUp firstPowerUp;
    private PowerUp secondPowerUp;
    private PowerUp thirdPowerUp;
    private PowerUp temporaryPowerUp= null;
    private Integer points= 0;
    private Set<Ammo> usableAmmo= new HashSet<>();
    private Set<Ammo> unusableAmmo= new HashSet<>();
    private List <Integer> pointsToAssign= new ArrayList<>(Arrays.asList(8, 6, 4, 2, 1, 1, 1, 1));
    private List <Integer> frenzyPointsToAssign= new ArrayList<>(Arrays.asList(2, 1, 1, 1, 1));
    private Game game;

    public Player (Figure figure, Game game){
        this.figure= figure;
        this.game= game;
        for (AmmoColour ammoColour: AmmoColour.values()){
            for (int i=0; i<3; i++){
                usableAmmo.add(new Ammo(ammoColour));
            }
        }
        this.figure.setPlayer(this);
    }
    @Override
    public void hit(String partialWeaponEffect, List<Targetable> hit, TurnMemory turnMemory) {
        List<Player> list = new ArrayList<>();
        for (Targetable t: hit
             )
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
        game.unpausedPlayer(this);
    }

    public void pause(){
        if (isPaused){
            throw new UnsupportedOperationException("This player is already paused");
        }
        isPaused= true;
        game.pausedPlayer(this);
    }


    public Integer getPoints() { return points; }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setUsableAmmo(Set<Ammo> usableAmmo) {
        this.usableAmmo = usableAmmo;
    }

    public Weapon getThirdWeapon() {
        return thirdWeapon;
    }

    public Weapon getSecondWeapon() {
        return secondWeapon;
    }

    public Weapon getFirstWeapon() {
        return firstWeapon;
    }

    public Figure getFigure() {
        return figure;
    }

    public PlayerDamage getHealthState() {
        return healthState;
    }

    public PowerUp getFirstPowerUp() {
        return firstPowerUp;
    }

    public PowerUp getSecondPowerUp() {
        return secondPowerUp;
    }

    public PowerUp getThirdPowerUp() {
        return thirdPowerUp;
    }

    public List<Tear> getHp() {return hp;}

    public Set<Tear> getMarks() {
        return marks;
    }

    public Set<Ammo> getUnusableAmmo() {
        return unusableAmmo;
    }

    public Set<Ammo> getUsableAmmo() {
        return usableAmmo;
    }

    public List<Integer> getPointsToAssign() {
        return pointsToAssign;
    }

    public PlayerValue getPlayerValue() {
        return playerValue;
    }

    public void setThirdWeapon(Weapon thirdWeapon) {
        this.thirdWeapon = thirdWeapon;
    }

    public void setSecondWeapon(Weapon secondWeapon) {
        this.secondWeapon = secondWeapon;
    }

    public void setFirstWeapon(Weapon firstWeapon) {
        this.firstWeapon = firstWeapon;
    }

    public void setFirstPowerUp(PowerUp firstPowerUp) {
        this.firstPowerUp = firstPowerUp;
    }

    public void setHp(List<Tear> hp) {
        this.hp = hp;
    }

    public void setMarks(Set<Tear> marks) {
        this.marks = marks;
    }

    public void setSecondPowerUp(PowerUp secondPowerUp) {
        this.secondPowerUp = secondPowerUp;
    }

    public void setThirdPowerUp(PowerUp thirdPowerUp) {
        this.thirdPowerUp = thirdPowerUp;
    }


    public Set<Weapon> showWeapons (){
        Set<Weapon> weapons= new HashSet<>();
        weapons.add(firstWeapon);
        weapons.add(secondWeapon);
        weapons.add(thirdWeapon);
        return weapons;
    }



    public void moveFigure (Direction direction){
        figure.move(direction);
    } //used by weapons

    public void moveFigure (Direction direction, Figure target){
        figure.move(target, direction);
    } //used by weapons

    public void teleport (Point position){
        figure.teleport(position);
        game.playerMovement(this, position);
    }

    public void run (Point destination) {
        figure.run(destination);
        game.playerMovement(this, destination);
    }

    public void grabStuff(Grabbable grabbed){
        figure.grab(grabbed);
    }


    public void reload(Weapon weapon){
        figure.reload(weapon);
    }

    public void usePowerUp (PowerUp powerUp){
        //TODO: use power up
        deletePowerUp(powerUp);
    }

    //MASSI: finally changed the powerUp structure, method getPowerUpName->getName, method getColour->getCardColour.getColour
    public void sellPowerUp (PowerUp powerUp){
        usableAmmo.add(new Ammo(powerUp.getCardColour().getColour()));
        deletePowerUp(powerUp);
    }

    public void deletePowerUp (PowerUp powerUp){
        if (powerUp.equals(firstPowerUp)){
            firstPowerUp=null;
        }
        else if (powerUp.equals(secondPowerUp)){
            secondPowerUp=null;
        }
        else if (powerUp.equals(thirdPowerUp)){
            thirdPowerUp=null;
        }
        game.discardedPowerUp(this, null, powerUp);
    }
    void addTear (FigureColour figureColour){
        Tear tear= new Tear(figureColour);
        hp.add(tear);
        game.attackOnPlayer(this, game.colourToPlayer(figureColour));
    }

    void addMark (FigureColour figureColour){
        Tear tear= new Tear(figureColour);
        marks.add(tear);
        game.markOnPlayer(this, game.colourToPlayer(figureColour));
    }

    void updatePlayerDamage (){
        if(healthState.getMaximumHits()==hp.size()){
            healthState= healthState.findNextHealthState();
        }
        if (hp.size()>=10){
                game.deathHandler(this);
        }
    }


    public void updatePointsToAssign (){
        playerValue= playerValue.getNextPlayerValue();
    }

    public void drawPowerUp (PowerUp powerUp){
        if (temporaryPowerUp!=null){
            throw new IllegalStateException("Discard a powerUp before drawing");
        }
        if (firstPowerUp==null) firstPowerUp= powerUp;
        else if (secondPowerUp==null) secondPowerUp= powerUp;
        else if (thirdPowerUp==null) thirdPowerUp= powerUp;
        else {
            temporaryPowerUp= powerUp;
            List<PowerUp> powerUps= new ArrayList<>();
            powerUps.add(firstPowerUp);
            powerUps.add(secondPowerUp);
            powerUps.add(thirdPowerUp);
            powerUps.add(powerUp);
            game.chosePowerUpToDiscard(this, powerUps);
        }
    }

    //TODO check consistency, don't instantiate new powerups, temporary powerups may be null on first deferentiation
    /*public void discardPowerUp (PowerUp powerUp){
        PowerUp powerUpToAdd= new PowerUp(temporaryPowerUp.getName(), temporaryPowerUp.getCardColour().getColour());
        temporaryPowerUp=null;
        PowerUp discardedPowerUp= new PowerUp(powerUpToAdd.getName(), powerUpToAdd.getCardColour().getColour());

        if (powerUp.equals(firstPowerUp)){
            discardedPowerUp= new PowerUp(firstPowerUp.getName(), firstPowerUp.getCardColour().getColour());
            firstPowerUp=powerUpToAdd;
        }
        else if (powerUp.equals(secondPowerUp)){
            discardedPowerUp= new PowerUp(secondPowerUp.getName(), secondPowerUp.getCardColour().getColour());
            secondPowerUp=powerUpToAdd;
        }
        else if (powerUp.equals(thirdPowerUp)){
            discardedPowerUp= new PowerUp(thirdPowerUp.getName(), thirdPowerUp.getCardColour().getColour());
            thirdPowerUp=powerUpToAdd;
        }
        else if (!powerUp.equals(powerUpToAdd)){
            throw new NullPointerException("The powerUp with this name cannot be discarded");
        }
        game.discardedPowerUp(this, powerUpToAdd, discardedPowerUp);
    }*/

    public void useAmmo (Set<Ammo> usedAmmo){
        usableAmmo.removeAll(usedAmmo);
        unusableAmmo.addAll(usedAmmo);
    }
}
