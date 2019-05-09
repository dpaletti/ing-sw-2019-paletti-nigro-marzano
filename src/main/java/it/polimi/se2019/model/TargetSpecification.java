package it.polimi.se2019.model;

import it.polimi.se2019.utility.Pair;

import java.util.List;

public class TargetSpecification  {
    private Boolean tile;
    private Pair<Boolean, List<String>> different;
    private Pair<Boolean, List<String>> previous;
    private Pair<Integer, Integer> radiusBetween;
    private Integer visible;
    private Integer enlarge;
    private Boolean area;

    public Boolean getTile() { return tile; }

    public Boolean getArea() { return area; }

    public Integer getEnlarge() { return enlarge; }

    public Integer getVisible() { return visible; }

    public Pair<Integer, Integer> getRadiusBetween() { return radiusBetween; }

    public void setArea(Boolean area) { this.area = area; }

    public void setEnlarge(Integer enlarge) { this.enlarge = enlarge; }

    public void setRadiusBetween(Pair<Integer, Integer> radiusBetween) { this.radiusBetween = radiusBetween; }

    public void setTile(Boolean tile) { this.tile = tile; }

    public void setVisible(Integer visible) { this.visible = visible; }

    public void setDifferent(Pair<Boolean, List<String>> different) {
        this.different = different;
    }

    public Pair<Boolean, List<String>> getDifferent() {
        return different;
    }

    public void setPrevious(Pair<Boolean, List<String>> previous) {
        this.previous = previous;
    }

    public Pair<Boolean, List<String>> getPrevious() {
        return previous;
    }

}

