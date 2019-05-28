package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.MoveEvent;
import it.polimi.se2019.utility.Observable;

import java.util.*;

public class Player extends Observable<Action> {
    private Figure figure;
    private List<Tear> hp= new ArrayList<>();
    private PlayerDamage healthState= new Healthy();
    private Set<Tear> marks= new HashSet<>();
    private Weapon firstWeapon;
    private Weapon secondWeapon;
    private Weapon thirdWeapon;
    private PowerUp firstPowerUp;
    private PowerUp secondPowerUp;
    private PowerUp thirdPowerUp;
    private Integer points= 0;
    private Set<Ammo> usableAmmo= new HashSet<>();
    private Set<Ammo> unusableAmmo= new HashSet<>();
    private TurnMemory turnMemory;
    private List <Integer> pointsToAssign= new ArrayList<>(Arrays.asList(8, 6, 4, 2, 1, 1));

    public Player (Figure figure){
        this.figure=figure;
        for (AmmoColour ammoColour: AmmoColour.values()){
            for (int i=0; i<3; i++){
                usableAmmo.add(new Ammo(ammoColour));
            }
        }
    }

    public void unpause(){
        //TODO implement
        //a paused player can be resurrect if it timed out
        //probably mv_events adhoc
    }

    public void pause(){
        //TODO implement
        //players that disconnect should be considered paused
        //some players may simply time out
        //probably mv_events adhoc
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

    public Integer getPoints() {
        return points;
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

    public TurnMemory getTurnMemory() {
        return turnMemory;
    }

    public List<Integer> getPointsToAssign() {
        return pointsToAssign;
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

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    public void setFirstPowerUp(PowerUp firstPowerUp) {
        this.firstPowerUp = firstPowerUp;
    }

    public void setHealthState(PlayerDamage healthState) {
        this.healthState = healthState;
    }

    public void setHp(List<Tear> hp) {
        this.hp = hp;
    }

    public void setMarks(Set<Tear> marks) {
        this.marks = marks;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setSecondPowerUp(PowerUp secondPowerUp) {
        this.secondPowerUp = secondPowerUp;
    }

    public void setThirdPowerUp(PowerUp thirdPowerUp) {
        this.thirdPowerUp = thirdPowerUp;
    }

    public void setUnusableAmmo(Set<Ammo> unusableAmmo) {
        this.unusableAmmo = unusableAmmo;
    }

    public void setUsableAmmo(Set<Ammo> usableAmmo) {
        this.usableAmmo = usableAmmo;
    }

    public void setTurnMemory(TurnMemory turnMemory) {
        this.turnMemory = turnMemory;
    }

    public GraphNode<Effect> showWeapon(Weapon weapon){
        return(weapon.getStaticDefinition());
        }

    public void useWeapon(Weapon weapon, Player target, String effectName){
        Effect effect= Game.getInstance().getEffectMap().get(effectName);
        target.getFigure().shoot(effect, getFigure().getColour());
        weapon.setLoaded(false);
    }

    public void moveFigure(Direction direction){
        figure.move(direction);
    }

    public void moveFigure(Direction direction, Figure target){
        figure.move(target, direction);
    }

    public void teleport (Point position){
        figure.teleport(position);
        String user= Game.getInstance().colourToUser(figure.getColour());
        Game.getInstance().sendMessage(new MoveEvent(user, position));
    }

    public void run (Point destination) {
        figure.run(destination);
        String user= Game.getInstance().colourToUser(figure.getColour());
        Game.getInstance().sendMessage(new MoveEvent(user, destination));
    }

    public void grabStuff(){
        figure.grab();
    }

    public void endTurn(){

        updateTurn();
    } //TODO: endTurn sends an event to the virtual view, modifies turns in Game

    public void reload(Weapon weapon){
        figure.reload(weapon);
    }

    public void usePowerUp (PowerUp powerUp){
        //TODO: implement
        // powerUp is discarded after usage
        if (powerUp.equals(firstPowerUp)){
            firstPowerUp=null;
        }
        else if (powerUp.equals(secondPowerUp)){
            secondPowerUp=null;
        }
        else if (powerUp.equals(thirdPowerUp)){
            thirdPowerUp=null;
        }
    }

    //MASSI: finally changed the powerUp structure, method getPowerUpName->getName, method getColour->getCardColour.getColour
    void sellPowerUp (PowerUp powerUp){
        Ammo powerUpToSell= new Ammo(powerUp.getCardColour().getColour());
        usableAmmo.add(powerUpToSell);
        if (powerUp.getName().equals(firstPowerUp.getName())){
            firstPowerUp=null;
        }
        else if (powerUp.getName().equals(secondPowerUp.getName())){
            secondPowerUp=null;
        }
        else if (powerUp.getName().equals(thirdPowerUp.getName())){
            thirdPowerUp=null;
        }
    }

    void addTear (FigureColour figureColour){
        Tear tear= new Tear(figureColour);
        hp.add(tear);
    }

    void addMark (FigureColour figureColour){
        Tear tear= new Tear(figureColour);
        marks.add(tear);
    }

    void updatePlayerDamage (){
        if(healthState.getMaximumHits()==hp.size()){
            healthState= healthState.findNextHealthState();
        }
        if (hp.size()>=10){
            if (Game.getInstance().getKillshotTrack().getNumberOfSkulls().equals(Game.getInstance().getKillshotTrack().getKillshot().size())){
                Game.getInstance().finalFrenzyDeathHandler(this);
            }
            else {
                Game.getInstance().deathHandler(this);
            }
        }
    }

    public void updateTurn (){ //only called from player and accessed from Figure
        Game.getInstance().updateTurns(this, turnMemory);
        turnMemory.clear();
    }


    public void calculatePoints(Player deadPlayer){
        FigureColour figureColour= deadPlayer.hp.get(0).getColour();
        Player firstShooter= Game.getInstance().colourToPlayer(figureColour);
        firstShooter.points++;

        Map<FigureColour, Integer> tags= new HashMap<>();
        for (Player player: Game.getInstance().getPlayers()){
            tags.put(player.getFigure().getColour(), 0);
        }
        for(Tear tear: deadPlayer.getHp()){
            tags.put(tear.getColour(), tags.get(tear.getColour())+1);
        }
        for (Map.Entry entry: tags.entrySet()){
            if (entry.getValue().equals(0)){
                tags.remove(entry.getKey());
            }
        }

        List <FigureColour> localBestShooters= new ArrayList<>();
        int maximumHits=0;
        for (int counter=0; counter<pointsToAssign.size(); counter++){
            for (Map.Entry<FigureColour, Integer> entry: tags.entrySet()){
                if (maximumHits==0 || entry.getValue()>maximumHits){
                    maximumHits=entry.getValue();
                    localBestShooters.clear();
                    localBestShooters.add(entry.getKey());
                }
                if (entry.getValue()==maximumHits){
                    localBestShooters.add(entry.getKey());
                }
                Player localBestPlayer= Game.getInstance().colourToPlayer(localBestShooters.get(0));
                localBestPlayer.points=localBestPlayer.points+pointsToAssign.get(counter);
                localBestShooters.remove(0);
                    for (FigureColour localBestShooter : localBestShooters) {
                        localBestPlayer = Game.getInstance().colourToPlayer(localBestShooter);
                        localBestPlayer.points = localBestPlayer.points + pointsToAssign.get(counter+1);
                        counter++;
                    }
            }
        }
        updatePointsToAssign();
    }

    public void finalFrenzyCalculatePoints (Player deadPlayer){
        List<Integer> finalFrenzyPoints= new ArrayList<>(Arrays.asList(2, 1, 1, 1));
        Map<FigureColour, Integer> tags= new HashMap<>();
        for (Player player: Game.getInstance().getPlayers()){
            tags.put(player.getFigure().getColour(), 0);
        }
        for(Tear tear: deadPlayer.getHp()){
            tags.put(tear.getColour(), tags.get(tear.getColour())+1);
        }
        for (Map.Entry entry: tags.entrySet()){
            if (entry.getValue().equals(0)){
                tags.remove(entry.getKey());
            }
        }
        List <FigureColour> localBestFrenzyShooters= new ArrayList<>();
        int maximumHits=0;
        for (int counter=0; counter<finalFrenzyPoints.size(); counter++){
            for (Map.Entry<FigureColour, Integer> entry: tags.entrySet()){
                if (maximumHits==0 || entry.getValue()>maximumHits){
                    maximumHits=entry.getValue();
                    localBestFrenzyShooters.clear();
                    localBestFrenzyShooters.add(entry.getKey());
                }
                if (entry.getValue()==maximumHits){
                    localBestFrenzyShooters.add(entry.getKey());
                }
                Player localBestFrenzyPlayer= Game.getInstance().colourToPlayer(localBestFrenzyShooters.get(0));
                localBestFrenzyPlayer.points= localBestFrenzyPlayer.points+finalFrenzyPoints.get(counter);
                for (FigureColour localBestShooter: localBestFrenzyShooters){
                    localBestFrenzyPlayer= Game.getInstance().colourToPlayer(localBestShooter);
                    localBestFrenzyPlayer.points=localBestFrenzyPlayer.points+finalFrenzyPoints.get(counter+1);
                    counter++;
                }
            }
        }
    }

    public void updatePointsToAssign (){
        pointsToAssign.remove(0);
    }
}
