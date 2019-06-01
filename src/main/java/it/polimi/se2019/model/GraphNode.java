package it.polimi.se2019.model;

import java.util.*;

public class GraphNode<T> implements Iterable<GraphNode<T>> {
    private Set<GraphNode<T>> children= new HashSet<>();
    private T key;
    private Set<GraphNode<T>> parents= new HashSet<>();
    public boolean visited=false;


    public Iterator<GraphNode<T>> iterator(){
        return new MyIterator();
    }

    public class MyIterator implements Iterator<GraphNode<T>> {
        public List<GraphNode<T>> list;
        public Iterator<GraphNode<T>> iterator;
        public MyIterator() {
            list=dfsList(GraphNode.this);
            iterator=list.iterator();
        }

        public List<GraphNode<T>> dfsList(GraphNode<T> root){
            list= new ArrayList<>();
            pdfsList(root);
            unSetVisit(root);
            return new ArrayList<>(list);
        }
        private void pdfsList(GraphNode<T> root){
            if (!list.contains(root) && root.getKey()!=null)
                list.add(root);
            root.visited= true;
            for (GraphNode<T> graphNode : root.getChildren()) {
                if (!graphNode.visited) {
                    list.add(graphNode);
                    pdfsList(graphNode);
                }
            }

        }

        private void pbfsList(GraphNode<T> root){
            LinkedList<GraphNode<T>> queue=new LinkedList<>();
            root.visited= true;
            queue.add(root);
            while (!queue.isEmpty()){
                root=queue.poll();
                list.add(root);
                for(GraphNode<T> graphNode: root.getChildren()){
                    if (!graphNode.visited){
                        graphNode.visited=true;
                        queue.add(graphNode);
                    }
                }
            }
        }
        private void unSetVisit(GraphNode<T> tGraphNode){
            tGraphNode.visited= false;
            for (GraphNode<T> graphNode : tGraphNode.getChildren()) {
                if (graphNode.visited) {
                    unSetVisit(graphNode);
                }
            }
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public GraphNode<T> next() {
            return iterator.next();
        }
    }
    public GraphNode(T key) {
        this.key = key;
    }

    public void addChild(GraphNode<T> child){
        children.add(child);
        child.getParents().add(this);
    }

    public void addParent(GraphNode<T> parent){
        parents.add(parent);
        parent.getChildren().add(this);
    }

    public T getKey() {
        return key;
    }


    public Set<GraphNode<T>> getParents() {
        return parents;
    }

    public Set<GraphNode<T>> getChildren() {
        return children;
    }

    public void setChildren(Set<GraphNode<T>> children) {
        this.children = children;
    }

    //This method takes in input a generic object T and returns 1 if the object is in a subgraph of the graphnode
    public boolean isIn(T key){
        for (GraphNode<T> g: this){
            if (g.getKey().equals(key))
                return true;
        }
        return false;
    }


    //Returns the graphNode with the input set as a node if that is in the subgraph of the graphnode, returns null otherwise
    public GraphNode<T> getGraphNode(T key) {
        for (GraphNode<T> g: this){
            if (g.getKey().equals(key))
                return g;
        }
        throw new NullPointerException("GraphNode not found");
    }

    //Safely removed a node
    public void remove (GraphNode<T> graphNode){
        graphNode.getParents().forEach( parent -> parent.getChildren().remove(graphNode));
        graphNode.getChildren().forEach(child -> child.getParents().remove(graphNode));
    }

    public void removeAll(Set<GraphNode<T>> set){
        for (GraphNode<T> graphNode : set){
            this.remove(graphNode);
        }
    }

    public void removeChild(GraphNode<T> child){
        child.parents.remove(this);
        this.children.remove(child);
    }

    public void removeParent(GraphNode<T> parent){
        this.parents.remove(parent);
        parent.children.remove(this);
    }

    @Override
    public String toString() {
        if (key==null)
            return "root";
       return key.toString();
    }

}