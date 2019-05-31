package it.polimi.se2019.utility;

import it.polimi.se2019.model.GraphNode;

import java.util.Set;
import java.util.regex.Pattern;

public class GraphSearch <T> {
    public GraphNode<T> found;
    public String toLog="";

    private void search(Set<T> tSet, GraphNode<T> tGraphNode){
        if (!found.isEmpty())
            return;
        if (tGraphNode.getNode().equals(tSet)) {
            found=tGraphNode;
            search(tSet,tGraphNode);
        }
        tGraphNode.visited= true;
        for (GraphNode<T> graphNode : tGraphNode.getChildren()) {
            if (!graphNode.visited && found.isEmpty()) {
                search(tSet,graphNode);
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

    public void dfs(Set<T> tSet,GraphNode<T> tGraphNode){
        found= new GraphNode<>();
        search(tSet,tGraphNode);
        unSetVisit(tGraphNode);
    }

    private void distendedGraph(GraphNode<T> node){
        node.visited= true;
        toLog=toLog.concat(node.toString());
        toLog=toLog.concat(System.lineSeparator());
        for (GraphNode<T> graphNode : node.getChildren()) {
            if (!graphNode.visited) {
                distendedGraph(graphNode);
            }
        }
    }

    public void print(GraphNode<T> node){
        toLog="";
        distendedGraph(node);
        unSetVisit(node);
        Log.fine(toLog);
    }



}
