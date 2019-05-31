package it.polimi.se2019.model;
import it.polimi.se2019.utility.GraphSearch;
import it.polimi.se2019.utility.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class GraphNode<T> {
    private Set<GraphNode<T>> children= new HashSet<>();
    private Set<T> node= new HashSet<>();
    private Set<GraphNode<T>> parents= new HashSet<>();
    public boolean visited=false;
    private GraphSearch<T> search=new GraphSearch<>();

    public void addChild(GraphNode<T> child){
        children.add(child);
        child.getParents().add(this);
    }

    public void addParent(GraphNode<T> parent){
        parents.add(parent);
        parent.getChildren().add(this);
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
        search.dfs(tSet,this);
        return !search.found.isEmpty();

    }

    //Returns the graphNode with the input set as a node if that is in the subgraph of the graphnode, returns null otherwise
    public GraphNode<T> getGraphNode(Set<T> tSet) {
        GraphSearch<T> search=new GraphSearch<>();
        search.dfs(tSet, this);
        System.out.println(search.found);
        return search.found;
    }

    //This method takes in a Set<T> and creates a child of the node with that as node and returns it
    public GraphNode<T> insert(Set<T> tSet){
        GraphNode<T> child= new GraphNode<>();
        child.setNode(tSet);
        this.addChild(child);
        return child;
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

    public boolean isEmpty(){
        return node.isEmpty();
    }

    //This method returns a set of graphnodes where all of the graphnodes contain the node of the input
    public Set<GraphNode<T>> getSimilarNodes(GraphNode<T> graphNode){
        Set<GraphNode<T>> set= new HashSet<>();
        for(GraphNode<T> child: getChildren()){
            if(child.getNode().containsAll(graphNode.getNode())){
                set.add(child);
            }
            set.addAll(child.getSimilarNodes(graphNode));
        }
        return set;
    }

    @Override
    public String toString() {
        String nodeString="";
        for (T t: node){
            nodeString=nodeString.concat(t.toString());
        }
        return nodeString;
    }

}