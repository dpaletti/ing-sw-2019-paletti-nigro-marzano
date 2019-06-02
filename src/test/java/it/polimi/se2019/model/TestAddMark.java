package it.polimi.se2019.model;

import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@Ignore
public class TestAddMark {
    @Test
    public void testAddMark (){
        Player player= new Player(new Figure(FigureColour.GREEN), new Game ());
        Set<Tear> mark = new HashSet<>();
        Tear tear= new Tear(FigureColour.MAGENTA);
        mark.add(tear);

        player.setMarks(mark);
        player.addMark(FigureColour.MAGENTA);
        assertEquals(player.getMarks(), mark);
    }
}
