package it.polimi.se2019.model;

import java.util.Set;

public class GraphNode<T> {
    private Set<GraphNode<T>> children;
    private Set<Effect> effects;

    public void addChild(GraphNode<T> child){}
    public Set<GraphNode<T>> getChildren() {
        return children;
    }

    public Set<Effect> getEffects() {
        return effects;
    }

    public void setEffects(Set<Effect> effects) {
        this.effects = effects;
    }
}