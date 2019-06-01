package it.polimi.se2019.model;

import org.junit.Ignore;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.*;


public class TestGraphNode {
    /*
        private GraphNode<String> child= new GraphNode<>();
        private GraphNode<String> parent= new GraphNode<>();
        private Set<String> stringSet= new HashSet<>();


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
/*
        @Test
        public void testisIn(){
            stringSet.add("Lulic");
            child.getNode().add("Lulic");
            child.addParent(parent);
            assertTrue(parent.isIn(stringSet));
            stringSet.clear();
            stringSet.add("71");
            assertFalse(parent.isIn(stringSet));
        }



       @Test
       public void testGetGraphNode() {

           try {
               stringSet.add("Lulic");
               child.getKey().add("Lulic");
               child.addParent(parent);
               assertEquals(child, parent.getGraphNode(stringSet));
           } catch (NullPointerException c) {
               fail();
           }
       }

      @Test (expected = NullPointerException.class)
       public void testGetGraphNodeException(){
            stringSet.add("71");
            child.getNode().add("Lulic");
            child.addParent(parent);
            parent.getGraphNode(stringSet);
      }

       @Test
        public void testInsert(){
            stringSet.add("Lulic");
            child= parent.insert(stringSet);
            assertEquals("Lulic",child.getNode().iterator().next());
            assertEquals(child,parent.getChildren().iterator().next());
       }

       @Test
        public void testIsEmpty(){
            assertEquals(true,parent.isEmpty());
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
            stringSet.add("Lulic");
            child=parent.insert(stringSet);
            try {
                GraphNode<String> obj= parent.getGraphNode(stringSet);
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
         stringSet.add("Lulic");
         child=parent.insert(stringSet);
         Set<GraphNode<String>> set= new HashSet<>();
         set.add(child);
         parent.removeAll(set);
         assertTrue(!parent.isIn(stringSet));
     }
*/

}

