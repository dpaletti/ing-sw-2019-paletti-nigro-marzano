package it.polimi.se2019.model;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;


@Ignore
public class TestAmmo {
    Ammo ammo= new Ammo(AmmoColour.BLUE);

    @Before
    public void setAmmo(){
        ammo.setColour(AmmoColour.RED);
    }

    @Test
    public void testAmmoGetters(){
        assertEquals(AmmoColour.RED,ammo.getColour());
    }
}