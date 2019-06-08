package it.polimi.se2019.model;

public class WeaponSpot {
    private Weapon firstWeapon;
    private Weapon secondWeapon;
    private Weapon thirdWeapon;

    public WeaponSpot (WeaponSpot weaponSpot) {
        this.firstWeapon= new Weapon(weaponSpot.firstWeapon);
        this.secondWeapon= new Weapon(weaponSpot.secondWeapon);
        this.thirdWeapon= new Weapon(weaponSpot.thirdWeapon);
    }

    public Weapon getFirstWeapon() {
        return firstWeapon;
    }

    public void setFirstWeapon(Weapon firstWeapon) {
        this.firstWeapon = firstWeapon;
    }

    public Weapon getSecondWeapon() {
        return secondWeapon;
    }

    public void setSecondWeapon(Weapon secondWeapon) {
        this.secondWeapon = secondWeapon;
    }

    public Weapon getThirdWeapon() {
        return thirdWeapon;
    }

    public void setThirdWeapon(Weapon thirdWeapon) {
        this.thirdWeapon = thirdWeapon;
    }

}
