package it.polimi.se2019.model;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.*;

public class TestGraphNode {
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

        @Test
        public void testGetRoot(){
            child.addParent(parent);
            assertEquals(parent,parent.getRoot());
            assertEquals(parent,child.getRoot());
        }

        @Test
        public void testisIn(){
            stringSet.add("Lulic");
            child.getNode().add("Lulic");
            child.addParent(parent);
            assertEquals(true,parent.isIn(stringSet));
            stringSet.clear();
            stringSet.add("71");
            assertEquals(false,parent.isIn(stringSet));
        }


       @Test
       public void testGetGraphNode(){

            try {
                stringSet.add("Lulic");
                child.getNode().add("Lulic");
                child.addParent(parent);
                assertEquals(child, parent.getGraphNode(stringSet));
            }catch (ClassNotFoundException c){
                fail();
            }

            try {
                stringSet.clear();
                stringSet.add("71");
                child= parent.getGraphNode(stringSet);
            }catch (ClassNotFoundException c){
                assertTrue(true);
            }

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
            assertEquals(true,parent.getChildren().isEmpty());
        }

     @Test
     public void testRemoveParent(){
            child.addParent(parent);
            child.removeParent(parent);
            assertEquals(true,child.getParents().isEmpty());
     }

     @Test
     public void testRemove(){
            stringSet.add("Lulic");
            child=parent.insert(stringSet);
            try {
                parent.remove(stringSet);
                assertEquals(true,parent.getChildren().isEmpty());
            }catch (ClassNotFoundException c){
                fail();
            }

            try {
                parent.remove(stringSet);
            }catch (ClassNotFoundException c){
                assertTrue(true);
            }


     }



}

