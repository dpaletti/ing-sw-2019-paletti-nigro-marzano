package it.polimi.se2019.model;

import java.util.List;

public class WeaponSpot {
    private Weapon firstWeapon;
    private Weapon secondWeapon;
    private Weapon thirdWeapon;

    public WeaponSpot (WeaponSpot weaponSpot) {
        this.firstWeapon= new Weapon(weaponSpot.firstWeapon);
        this.secondWeapon= new Weapon(weaponSpot.secondWeapon);
        this.thirdWeapon= new Weapon(weaponSpot.thirdWeapon);
    }

    public WeaponSpot (List<Weapon> cards){
        if (cards.size()>3)
            throw new UnsupportedOperationException("The number of cards exceeds the number of possible spots");
        firstWeapon= new Weapon(cards.get(0));
        secondWeapon= new Weapon(cards.get(1));
        thirdWeapon= new Weapon(cards.get(2));
    }

    public Weapon getFirstWeapon() {
        return new Weapon(firstWeapon);
    }

    public void setFirstWeapon(Weapon firstWeapon) {
        this.firstWeapon = firstWeapon;
    }

    public Weapon getSecondWeapon() {
        return new Weapon(secondWeapon);
    }

    public void setSecondWeapon(Weapon secondWeapon) {
        this.secondWeapon = secondWeapon;
    }

    public Weapon getThirdWeapon() {
        return new Weapon(thirdWeapon);
    }

    public void setThirdWeapon(Weapon thirdWeapon) {
        this.thirdWeapon = thirdWeapon;
    }


}
