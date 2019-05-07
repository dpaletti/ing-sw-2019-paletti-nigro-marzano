package it.polimi.se2019.model;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

    @Ignore
public class TestGraphNode {
        private GraphNode<Effect> graphNode;
        private Effect effect;

        @Before
        public void beforeTest(){
            effect= new Effect();
            effect.setName("B");
            graphNode= new GraphNode<>();
        }

        @Test
        public void testNodeInsertion(){
            Set<Effect> hash= new HashSet<>();
            hash.add(effect);
            graphNode.insert(hash);
            String s1= graphNode.getChildren().iterator().next().getNode().iterator().next().getName();
            assertEquals("B",s1);
        }


    }

