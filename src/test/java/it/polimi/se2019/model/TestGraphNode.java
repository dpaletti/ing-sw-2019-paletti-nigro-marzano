package it.polimi.se2019.model;

import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.*;


public class TestGraphNode {
        private GraphNode<String> child= new GraphNode<>("Acerbi",1);
        private GraphNode<String> parent= new GraphNode<>( "Strakosha",0);


        @Test
        public void testAddChild(){
            parent.addChild(child);
            assertEquals(child,parent.getChildren().iterator().next());
            assertEquals(parent,child.getParents().iterator().next());
        }

        @Test
        public void testAddParent(){
            child.addParent(parent);
            assertEquals(parent,child.getParents().iterator().next());
            assertEquals(child,parent.getChildren().iterator().next());
        }

        @Test
        public void testisIn(){
            parent.addChild(child);
            assertTrue(parent.isIn("Acerbi"));
        }

       @Test
       public void testGetGraphNode() {

           try {
               child.addParent(parent);
               assertEquals(child, parent.getGraphNode("Acerbi"));
           } catch (NullPointerException c) {
               fail();
           }
       }

      @Test (expected = NullPointerException.class)
       public void testGetGraphNodeException(){
            parent.getGraphNode("71");
      }

       @Test
        public void testRemoveChild(){
            parent.addChild(child);
            parent.removeChild(child);
            assertTrue(parent.getChildren().isEmpty());
        }

     @Test
     public void testRemoveParent(){
            child.addParent(parent);
            child.removeParent(parent);
            assertTrue(child.getParents().isEmpty());
     }


     @Test
     public void testRemove(){
            parent.addChild(child);
            try {
                GraphNode<String> obj= parent.getGraphNode("Acerbi");
                parent.remove(obj);
                assertTrue(parent.getChildren().isEmpty());
            }catch (NullPointerException c){
                fail();
            }

     }

     @Test
     public void testSetChildren(){
            Set<GraphNode<String>> children= new HashSet<>();
            children.add(child);
            parent.setChildren(children);
            assertEquals(children,parent.getChildren());
     }

     @Test
     public void testRemoveAll(){
         parent.addChild(child);
         Set<GraphNode<String>> set= new HashSet<>();
         set.add(child);
         parent.removeAll(set);
         assertTrue(!parent.isIn("Lulic"));
     }

     @Test
     public void testGetLayer(){
            parent.addChild(child);
            child.addChild(new GraphNode<>("71",2));
            assertEquals("71",parent.getListLayer(2).iterator().next().getKey());
     }

    @Test
    public void testToString(){
        parent.addChild(child);
        GraphNode<String> lucasLeiva= new GraphNode<>("Leiva",2);
        child.addChild(lucasLeiva);
        lucasLeiva.addChild(new GraphNode<>("Immobile",3));
        String lineUp="\t\troot"+System.lineSeparator()+"Acerbi<Strakosha,>\t"+System.lineSeparator()+
        "Leiva<Acerbi,>\t"+System.lineSeparator()+
        "Immobile<Leiva,>\t"+System.lineSeparator();
        assertEquals(lineUp,parent.toString());
    }

    //Todo: try to do it with assert
    @Test
    public void testGetSibling(){
            GraphNode<String> father = new GraphNode<>("Peppe", 0);
            father.addChild(new GraphNode<>("Luca", 1));
            father.addChild(new GraphNode<>("Marco", 1));
            father.addChild(new GraphNode<>("Gianni", 1));
            System.out.println(father.getSibling(father.getChildren(), "Luca"));
    }

}

