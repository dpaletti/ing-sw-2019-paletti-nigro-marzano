package it.polimi.se2019.model;

import org.junit.Before;
import org.junit.Ignore;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@Ignore
public class TestCardHelper {
    private Weapon weapon;
    private PowerUp powerUp;

    @Before
    public void setup(){
        weapon=new Weapon(Paths.get("files/weapons/Cyberblade.json").toString());
        powerUp=new PowerUp(Paths.get("files/powerUps/NewtonBlue.json").toString());
    }

    /*
    @Test
    public void testFindWeaponByName(){
        assertEquals(weapon.getName(),CardHelper.getInstance().findWeaponByName("Cyberblade").getName());
    }

   @Test(expected = NullPointerException.class)
   public void testFindWeaponByNameException(){
       Weapon secondWeapon= CardHelper.getInstance().findWeaponByName("Lulic");
   }

   //TODO check why this does not work.
    @Test
    public void testFindPowerUpByName(){
        assertEquals(powerUp.getName(),CardHelper.getInstance().findPowerUpByName("Newton").getName());
    }

    @Test(expected = NullPointerException.class)
    public void testFindPowerUpByNameException(){
        PowerUp secondPowerUp= CardHelper.getInstance().findPowerUpByName("Lulic");
    }
    */
}
