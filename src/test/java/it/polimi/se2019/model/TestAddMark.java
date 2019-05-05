package it.polimi.se2019.model;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class TestAddMark {
    @Test
    public void testAddMark (){
        Player player= new Player();
        Set<Tear> mark = new HashSet<>();
        Tear tear= new Tear(FigureColour.MAGENTA);
        mark.add(tear);

        player.setMarks(mark);
        player.addMark(FigureColour.MAGENTA);
        assertEquals(player.getMarks(), mark);
    }
}
