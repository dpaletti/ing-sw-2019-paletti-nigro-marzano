package it.polimi.se2019.model;

import it.polimi.se2019.utility.Observable;

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

    public List<Tear> getHp() {}

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

    public void setHp(List<Tear> hp) { this.hp = hp; }

    public void setMarks(Set<Tear> marks) { this.marks = marks; }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setSecondPowerUp(PowerUp secondPowerUp) {
        this.secondPowerUp = secondPowerUp;
    }

    public void setThirdPowerUp(PowerUp thirdPowerUp) {
        this.thirdPowerUp = thirdPowerUp;
    }

    public void setUnusableAmmo(Set<Ammo> unusableAmmo) { this.unusableAmmo = unusableAmmo; }

    public void setUsableAmmo(Set<Ammo> usableAmmo) { this.usableAmmo = usableAmmo; }

    public GraphNode<Effect> showWeapon(Weapon weapon){return null;}

    public void useWeapon(Weapon weapon){}

    public void moveFigure(Direction direction){}

    public void grabStuff(){}

    public void endTurn(){}

    public void reload(Weapon weapon){}

    public void usePowerUp (PowerUp powerUp){}

    public void sellPowerUp (PowerUp powerUp){}

}
