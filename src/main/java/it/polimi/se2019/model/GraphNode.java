package it.polimi.se2019.model;
import java.util.HashSet;
import java.util.Set;

public class GraphNode<T> {
    private Set<GraphNode<T>> children= new HashSet<>();
    private Set<T> node= new HashSet<>();
    private Set<GraphNode<T>> parents= new HashSet<>();

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
        if(node.equals(tSet)){
            return true;
        }else{
            while(children.iterator().hasNext()){
                return children.iterator().next().isIn(tSet);
            }
        }
        return false;
    }

    //Returns the graphNode with the input set as a node if that is in the subgraph of the graphnode, returns null otherwise
    public GraphNode<T> getGraphNode(Set<T> tSet){
        if(node.equals(tSet)){
            return this;
        }else if(!children.isEmpty()) {
            for (GraphNode<T> graphNode : children) {
                try {
                    return graphNode.getGraphNode(tSet);
                }catch (NullPointerException e){
                    /*Cathing this exception to let the search of the node continue, if there isn't such node, at last will be thrown the exception anyway*/
                }
            }
        }
        throw new NullPointerException();

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
        graphNode.getParents().forEach( parent -> parent.getChildren().removeIf(child -> (child.equals(graphNode))));
        graphNode.getChildren().forEach(child -> child.getParents().removeIf(parent -> (parent.equals(graphNode))));
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

}