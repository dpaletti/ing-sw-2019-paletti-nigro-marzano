package it.polimi.se2019.model;

import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.*;

@Ignore
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

        @Ignore
        @Test
        public void testisIn(){
            parent.addChild(child);
            assertTrue(parent.isIn("Lulic"));
        }



        @Ignore
       @Test
       public void testGetGraphNode() {

           try {
               child.addParent(parent);
               assertEquals(child, parent.getGraphNode("Lulic"));
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


     @Ignore
     @Test
     public void testRemove(){
            parent.addChild(child);
            try {
                GraphNode<String> obj= parent.getGraphNode("Lulic");
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
        parent.addChild(new GraphNode<>("Luiz Felipe",1));
        parent.addChild(new GraphNode<>("Radu",1));
        child.addChild(new GraphNode<>("Romulo",2));
        GraphNode<String> lucasLeiva= new GraphNode<>("Leiva",2);
        child.addChild(lucasLeiva);
        child.addChild(new GraphNode<>("Luis Alberto",2));
        child.addChild(new GraphNode<>("Milinkovic-Savic",2));
        child.addChild(new GraphNode<>("Lulic",2));
        lucasLeiva.addChild(new GraphNode<>("Correa",3));
        lucasLeiva.addChild(new GraphNode<>("Immobile",3));
        System.out.print(parent.toString());
    }

}

