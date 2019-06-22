package it.polimi.se2019.utility;

import it.polimi.se2019.model.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;

import static junit.framework.TestCase.assertEquals;

//import sun.jvm.hotspot.oops.Mark;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class TestStateEncoder {
    Player player=new Player(new Figure(FigureColour.MAGENTA),new Game());
    String user;
    String file;
    WeaponHelper weaponHelper= new WeaponHelper();
    PowerUpHelper powerUpHelper= new PowerUpHelper();

    @Before
    public void setup(){
        player.setFirstWeapon((Weapon)weaponHelper.findByName("Cyberblade"));
        player.setSecondWeapon((Weapon)weaponHelper.findByName("Shotgun"));
        player.setThirdWeapon((Weapon)weaponHelper.findByName("Furnace"));
        player.setHp(new ArrayList<>());
        player.setMarks(new HashSet<>());
        /*player.setFirstPowerUp((PowerUp)powerUpHelper.findByName("TeleportRed"));
        player.setSecondPowerUp((PowerUp)powerUpHelper.findByName("TeleportBlue"));
        player.setThirdPowerUp((PowerUp)powerUpHelper.findByName("TeleportYellow"));*/
        player.setPoints(3);
        player.setUsableAmmo(new HashSet<>());
        user= "Lulic71";
        file= StateEncoder.generateEncodedGame();
    }

    @Test
    public void testGenerateEncodedGame(){
        assertEquals("<Players><&>"+System.lineSeparator()+"<Board><£>"+System.lineSeparator()+"<Last><@>"+System.lineSeparator(),
                StateEncoder.generateEncodedGame());
    }

    @Ignore
    @Test
    public void testGetEncodedUser(){
        assertEquals("Lulic71:M;;;8;Cyberblade;Shotgun;Furnace;TelR;TelB;TelY;3;R0,B0,Y0;0,0"+System.lineSeparator(),StateEncoder.getEncodedUser(player,user));
    }

    @Test
    public void testAddPlayer(){
        file=StateEncoder.addPlayer(player,user,file);
        System.out.println(file);
    }

    @Test
    public void testAddLastUser(){
        file=StateEncoder.addLastUser(user,file);
        System.out.println(file);
    }


}
