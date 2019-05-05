package it.polimi.se2019.model;

import java.util.Set;
import java.util.TreeSet;

public class GraphNode<T> {
    private Set<GraphNode<T>> children;
    private Set<T> node;
    private Set<GraphNode<T>> parents;

    public void addChild(GraphNode<T> child){
        children.add(child);
        child.addParent(this);
    }

    public void addParent(GraphNode<T> parent){
        parents.add(parent);
    }

    public Set<T> getNode() {
        return node;
    }

    public void setNode(Set<T> t) {
        this.node = t;
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

    public Integer getHeight(){
        TreeSet<Integer> parentsHeight= new TreeSet<>();
        if(!parents.isEmpty()){
            while(parents.iterator().hasNext()){
                parentsHeight.add(parents.iterator().next().getHeight());
            }
            return 1+ parentsHeight.last();
        }else{
            return 0;
        }
    }

    //This can be used only with direct acyclic graphs, the method returns the "root" of the graph
    public GraphNode<T> getRoot(){
        if(parents.isEmpty()){
            return this;
        }else{
            return parents.iterator().next().getRoot();
        }
    }

    //This method takes in input a generic object T and returns 1 if the object is in a subgraph of the graphnode
    public boolean isIn(Set<T> tSet){
        if(node.equals(tSet)){
            return true;
        }else{
            while(children.iterator().hasNext()){
                return children.iterator().next().isIn(tSet);
            }
        }
        return false;
    }

    //This method takes in a Set<T> and creates a child of the node with that as node and returns it
    public GraphNode<T> insert(Set<T> tSet){
        GraphNode<T> child= new GraphNode<>();
        child.setNode(tSet);
        this.addChild(child);
        return child;
    }
}