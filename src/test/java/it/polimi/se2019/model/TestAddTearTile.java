package it.polimi.se2019.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestAddTearTile {
    @Test
    public void testAddTear (){
        List<Tear> finalHP = new ArrayList<>();
        Tear tear= new Tear(FigureColour.MAGENTA);
        finalHP.add(tear);
        SpawnTile tile= new SpawnTile(RoomColour.WHITE, Collections.emptyMap(), Collections.emptySet(), null, null, null, finalHP);

        tile.addTear(FigureColour.MAGENTA);
        assertEquals(tile.getHp(), finalHP);
    }
}
