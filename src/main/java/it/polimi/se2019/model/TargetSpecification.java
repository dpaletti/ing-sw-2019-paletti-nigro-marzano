package it.polimi.se2019.model;

import java.io.Serializable;
import java.util.List;

public class TargetSpecification implements Serializable {
    private Boolean tile;
    private Pair<Integer, List<Effect>> different;
    private Pair<Integer, Integer> radiusBetween;
    private Integer visible;
    private Integer enlarge;
    private Boolean area;

    public Boolean getTile() { return tile; }

    public Boolean getArea() { return area; }

    public Integer getEnlarge() { return enlarge; }

    public Integer getVisible() { return visible; }

    public Pair<Integer, Integer> getRadiusBetween() { return radiusBetween; }

    public Pair<Integer, List<Effect>> getDifferent() { return different; }

    public void setDifferent(Pair<Integer, List<Effect>> different) { this.different = different; }

    public void setArea(Boolean area) { this.area = area; }

    public void setEnlarge(Integer enlarge) { this.enlarge = enlarge; }

    public void setRadiusBetween(Pair<Integer, Integer> radiusBetween) { this.radiusBetween = radiusBetween; }

    public void setTile(Boolean tile) { this.tile = tile; }

    public void setVisible(Integer visible) { this.visible = visible; }
}

