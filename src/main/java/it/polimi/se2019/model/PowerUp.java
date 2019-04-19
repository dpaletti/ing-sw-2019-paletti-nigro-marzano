package it.polimi.se2019.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class PowerUp {
    private AmmoColour colour;
    private GraphNode<Effect> staticDefinition;

    public AmmoColour getColour() {
        return colour;
    }

    public GraphNode<Effect> getStaticDefinition() { return staticDefinition; }

    public void setColour(AmmoColour colour) {
        this.colour = colour;
    }

    public void setStaticDefinition(GraphNode<Effect> staticDefinition) { this.staticDefinition = staticDefinition; }
}
