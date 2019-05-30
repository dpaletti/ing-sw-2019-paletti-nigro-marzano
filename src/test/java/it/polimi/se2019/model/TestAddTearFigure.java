package it.polimi.se2019.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TestAddTearFigure {
    @Test
    public void testAddTear (){
        Player player= new Player(new Figure(FigureColour.BLUE), new Game());
        List<Tear> finalHP = new ArrayList<>();
        Tear tear= new Tear(FigureColour.MAGENTA);
        finalHP.add(tear);

        player.setHp(finalHP);
        player.addTear(FigureColour.MAGENTA);
        assertEquals(player.getHp(), finalHP);
    }
}
