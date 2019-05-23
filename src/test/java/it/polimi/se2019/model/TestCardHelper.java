package it.polimi.se2019.model;
import it.polimi.se2019.utility.Factory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCardHelper {
    Weapon weapon;
    PowerUp powerUp;

    @Before
    public void setup(){
        weapon=Factory.createWeapon(Weapon.class.getClassLoader().getResource("weapons/Cyberblade.json").getPath());
        powerUp=Factory.createPowerUp(PowerUp.class.getClassLoader().getResource("powerUps/NewtonBlue.json").getPath());
    }

    @Test
    public void testFindWeaponByName(){
        assertEquals(weapon.getName(),CardHelper.getInstance().findWeaponByName("Cyberblade").getName());
    }

   @Test(expected = NullPointerException.class)
   public void testFindWeaponByNameException(){
       Weapon secondWeapon= CardHelper.getInstance().findWeaponByName("Lulic");
   }

    @Test
    public void testFindPowerUpByName(){
        assertEquals(powerUp.getName(),CardHelper.getInstance().findPowerUpByName("Newton",AmmoColour.BLUE).getName());
    }

    @Test(expected = NullPointerException.class)
    public void testFindPowerUpByNameException(){
        PowerUp secondPowerUp= CardHelper.getInstance().findPowerUpByName("Lulic",AmmoColour.BLUE);
    }
}
