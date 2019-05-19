package it.polimi.se2019.model;
import it.polimi.se2019.utility.Factory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class TestCardHelper {
    Set<Weapon> weaponSet= new HashSet<>();
    Set<PowerUp> powerUpSet= new HashSet<>();
    Weapon weapon;
    PowerUp powerUp;


    @Before
    public void setup(){
        weapon=Factory.createWeapon(Weapon.class.getClassLoader().getResource("weapons/Furnace.json").getPath());
        weaponSet.add(weapon);
        powerUp=Factory.createPowerUp(PowerUp.class.getClassLoader().getResource("powerUps/NewtonBlue.json").getPath());
        powerUpSet.add(powerUp);
    }

    @Test
    public void testFindWeaponByName(){
        assertEquals(weapon,CardHelper.findWeaponByName("Furnace", weaponSet));
    }

    @Test(expected = NullPointerException.class)
    public void testFindWeaponByNameException(){
        Weapon secondWeapon= CardHelper.findWeaponByName("Cyberblade",weaponSet);
    }

    @Test
    public void testFindPowerUpByName(){
        assertEquals(powerUp,CardHelper.findPowerUpByName("Newton",powerUpSet,AmmoColour.BLUE));
    }

    @Test(expected = NullPointerException.class)
    public void testFindPowerUpByNameException(){
        PowerUp secondPowerUp= CardHelper.findPowerUpByName("Teleport",powerUpSet,AmmoColour.BLUE);
    }
}
