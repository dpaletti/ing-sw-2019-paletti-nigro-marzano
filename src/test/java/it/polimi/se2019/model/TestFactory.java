package it.polimi.se2019.model;
import it.polimi.se2019.utility.Factory;
import static org.junit.Assert.*;

import it.polimi.se2019.utility.Log;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class TestFactory {

    private Set<Set<String>> invalidCombinations= new HashSet<>();
    private Set<String> firstCombination= new HashSet<>();
    private Set<String> secondCombination= new HashSet<>();
    private Ammo cardColour= new Ammo(AmmoColour.RED);

    @Before
    public void setup(){
        firstCombination.add("B2");
        secondCombination.add("E1");
        invalidCombinations.add(firstCombination);
        invalidCombinations.add(secondCombination);
    }

    @Test
    public void TestCreateWeapon(){
        Weapon machineGun= Factory.createWeapon(Paths.get("files/weapons/MachineGun.json").toString());
        for (WeaponEffect weaponEffect: machineGun.getWeaponEffects()){
            if (weaponEffect.name.equals("withTurretTripod")){
                for (PartialWeaponEffect partialWeaponEffect: weaponEffect.getEffects())
                    System.out.print(partialWeaponEffect);
            }
        }
        assertEquals("MachineGun" , machineGun.getName());
    }

   @Test
   public void TestCreatePowerUp(){
        PowerUp teleportRed= Factory.createPowerUp(Paths.get("files/powerUps/TeleportRed.json").toString());
        assertEquals("TeleportRed",teleportRed.getName());
        assertEquals(cardColour.getColour(), teleportRed.getCardColour().getColour());
   }


}


