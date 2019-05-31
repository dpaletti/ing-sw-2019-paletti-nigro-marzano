package it.polimi.se2019.model;

import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.utility.Point;

import java.util.*;

public class Player extends Observable<Action> {
    private Figure figure;
    private boolean isPaused= false;
    private List<Tear> hp= new ArrayList<>();
    private PlayerDamage healthState= new Healthy();
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
    private TurnMemory turnMemory;
    private List <Integer> pointsToAssign= new ArrayList<>(Arrays.asList(8, 6, 4, 2, 1, 1));
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

    public List<Player> getAllPlayers (){
      return game.getPlayers();
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

    public TurnMemory getTurnMemory() {
        return turnMemory;
    }

    public List<Integer> getPointsToAssign() {
        return pointsToAssign;
    }

    public PowerUp getTemporaryPowerUp() {
        return temporaryPowerUp;
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

    public void setSecondPowerUp(PowerUp secondPowerUp) {
        this.secondPowerUp = secondPowerUp;
    }

    public void setThirdPowerUp(PowerUp thirdPowerUp) {
        this.thirdPowerUp = thirdPowerUp;
    }

    public void setTurnMemory(TurnMemory turnMemory) {
        this.turnMemory = turnMemory;
    }

    public void setTemporaryPowerUp(PowerUp temporaryPowerUp) {
        this.temporaryPowerUp = temporaryPowerUp;
    }



    public Set<Weapon> showWeapons (){
        Set<Weapon> weapons= new HashSet<>();
        weapons.add(firstWeapon);
        weapons.add(secondWeapon);
        weapons.add(thirdWeapon);
        return weapons;
    }

    public GraphNode<Effect> showWeapon (Weapon weapon){
        return(weapon.getStaticDefinition());
        } //to be deleted

    public void useWeapon(Weapon weapon, Player target, String effectName){
        Effect effect= game.getEffectMap().get(effectName);
        target.getFigure().shoot(effect, getFigure().getColour());
        weapon.setLoaded(false);
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

    public void grabStuff(Card grabbed){
        figure.grab(grabbed);
    }

    public void endTurn (){
        //check whether anything else can be added to this method
        updateTurn();
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
            if (game.getKillshotTrack().getNumberOfSkulls().equals(game.getKillshotTrack().getKillshot().size())){
                game.finalFrenzyDeathHandler(this);
            }
            else {
                game.deathHandler(this);
            }
        }
    }

    public void updateTurn (){ //only called from player and accessed from Figure
        game.updateTurns(this, turnMemory);
        turnMemory.clear();
    }


    public void calculatePoints(Player deadPlayer){
        FigureColour figureColour= deadPlayer.hp.get(0).getColour();
        Player firstShooter= game.colourToPlayer(figureColour);
        firstShooter.points++;

        Map<FigureColour, Integer> tags= new HashMap<>();
        for (Player player: game.getPlayers()){
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
                Player localBestPlayer= game.colourToPlayer(localBestShooters.get(0));
                localBestPlayer.points=localBestPlayer.points+pointsToAssign.get(counter);
                localBestShooters.remove(0);
                    for (FigureColour localBestShooter : localBestShooters) {
                        localBestPlayer = game.colourToPlayer(localBestShooter);
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
        for (Player player: game.getPlayers()){
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
                Player localBestFrenzyPlayer= game.colourToPlayer(localBestFrenzyShooters.get(0));
                localBestFrenzyPlayer.points= localBestFrenzyPlayer.points+finalFrenzyPoints.get(counter);
                for (FigureColour localBestShooter: localBestFrenzyShooters){
                    localBestFrenzyPlayer= game.colourToPlayer(localBestShooter);
                    localBestFrenzyPlayer.points=localBestFrenzyPlayer.points+finalFrenzyPoints.get(counter+1);
                    counter++;
                }
            }
        }
        //game.deathHandler();
    }

    public void updatePointsToAssign (){
        pointsToAssign.remove(0);
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

    public void discardPowerUp (PowerUp powerUp){
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
    }

    public void useAmmo (Set<Ammo> usedAmmo){
        usableAmmo.removeAll(usedAmmo);
        unusableAmmo.addAll(usedAmmo);
    }
}
