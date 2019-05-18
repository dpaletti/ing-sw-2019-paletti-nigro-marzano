package it.polimi.se2019.model;

import it.polimi.se2019.utility.Factory;
import org.junit.Test;
import static junit.framework.TestCase.*;
import java.util.HashSet;
import java.util.Set;

public class TestCard {
    private Weapon cyberblade= Factory.createWeapon(Weapon.class.getClassLoader().getResource("weapons/Cyberblade.json").getPath());

    @Test
    public void testGenerateGraph(){
        cyberblade.generateGraph();
        Set<GraphNode<Effect>> children=cyberblade.getStaticDefinition().getChildren();
        Set<String> stringSet= new HashSet<>();
        stringSet.add("B1");
        Set<String> effectNames= new HashSet<>();
        for(GraphNode<Effect> node: children){
            assertEquals(1, node.getNode().size());
            effectNames.add(node.getNode().iterator().next().getName());
        }
        assertEquals(stringSet,effectNames);
    }

   @Test(expected = UnsupportedOperationException.class)
    public void testGenerateGraphException(){
       cyberblade.generateGraph();
       cyberblade.generateGraph();
   }

}
