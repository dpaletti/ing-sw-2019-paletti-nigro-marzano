package it.polimi.se2019.model;

import it.polimi.se2019.utility.Observable;
import it.polimi.se2019.model.mv_events.EffectToApplyEvent;
import it.polimi.se2019.model.mv_events.FigureToAttackEvent;
import it.polimi.se2019.utility.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player extends Observable<Action> {
    private Figure figure;
    private List<Tear> hp;
    private PlayerDamage healthState;
    private PlayerValue deathState;
    private Set<Tear> marks;
    private Weapon firstWeapon;
    private Weapon secondWeapon;
    private Weapon thirdWeapon;
    private PowerUp firstPowerUp;
    private PowerUp secondPowerUp;
    private PowerUp thirdPowerUp;
    private Integer points;
    private Set<Ammo> usableAmmo;
    private Set<Ammo> unusableAmmo;

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

    public PlayerValue getDeathState() {
        return deathState;
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

    public Set<Ammo> getUnusableAmmo() { return unusableAmmo; }

    public Set<Ammo> getUsableAmmo() { return usableAmmo; }

    public void setThirdWeapon(Weapon thirdWeapon) {
        this.thirdWeapon = thirdWeapon;
    }

    public void setSecondWeapon(Weapon secondWeapon) {
        this.secondWeapon = secondWeapon;
    }

    public void setFirstWeapon(Weapon firstWeapon) {
        this.firstWeapon = firstWeapon;
    }

    public void setDeathState(PlayerValue deathState) {
        this.deathState = deathState;
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


    public GraphNode<Effect> showWeapon(Weapon weapon){
        return(weapon.getStaticDefinition());
        }

    public void useWeapon(Weapon weapon){
        FigureToAttackEvent figureToAttackEvent =new FigureToAttackEvent();
        EffectToApplyEvent effectToApplyEvent=new EffectToApplyEvent();
        Set<String> applicableEffects=new HashSet<>();
        Set<FigureColour> attackableFigureColours=new HashSet<>();
        Effect chosenEffect=new Effect();
        Figure chosenTarget=figure; //temporary, will be updated after vc events are implemented
        String target=null;
        Set<Pair<Effect, Set<Figure>>> targetSet= figure.generateTargetSet(weapon.getStaticDefinition());

        for (Pair<Effect, Set<Figure>> counter: targetSet){
            applicableEffects.add(counter.getFirst().getName());
        }
        effectToApplyEvent.setApplicableEffects(applicableEffects);
        Game.getInstance().sendMessage(effectToApplyEvent);
        //TODO: vc_events returns chosen effect

        for (Pair<Effect, Set<Figure>> counter: targetSet){
            if(counter.getFirst().equals(chosenEffect)) {
                for (Figure figureCounter : counter.getSecond()) {
                    attackableFigureColours.add(figureCounter.getColour());
                }
            }
        }
        figureToAttackEvent.setPlayersToAttack(attackableFigureColours);
        Game.getInstance().sendMessage(figureToAttackEvent);
        //TODO: vc_events returns chosen target
        figure.generateWeaponEffect(weapon.getStaticDefinition(), chosenTarget);
        weapon.setLoaded(false);
    }

    public void moveFigure(Direction direction){
        figure.move(direction);
    }

    public void grabStuff(){
        figure.grab();
    }

    public void endTurn(){

    } //TODO: endTurn sends an event to the virtual view, modifies turns in Game

    public void reload(Weapon weapon){
        figure.reload(weapon);
    }

    public void usePowerUp (PowerUp powerUp){
        //TODO: implement
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
            Game.getInstance().deathHandler();
        }
    }

    public void updateTurn (){
        //List<Turn> turns= Game.getInstance().getTurns();
    }

}
