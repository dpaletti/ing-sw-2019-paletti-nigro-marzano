package it.polimi.se2019.model;
import it.polimi.se2019.utility.Factory;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
        Weapon Cyberblade= Factory.createWeapon(Weapon.class.getClassLoader().getResource("weapons/Cyberblade.json").getPath());
        assertEquals("Cyberblade" , Cyberblade.getName());
        assertEquals(invalidCombinations, Cyberblade.getInvalidCombinations());
    }

   @Test
   public void TestCreatePowerUp(){
        PowerUp teleportRed= Factory.createPowerUp(PowerUp.class.getClassLoader().getResource("powerUps/TeleportRed.json").getPath());
        assertEquals("Teleport",teleportRed.getName());
        assertEquals(cardColour.getColour(), teleportRed.getCardColour().getColour());
   }


}


