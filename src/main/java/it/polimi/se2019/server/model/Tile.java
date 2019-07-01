package it.polimi.se2019.server.model;

import it.polimi.se2019.commons.mv_events.MVSelectionEvent;
import it.polimi.se2019.commons.utility.Point;

import java.util.*;

//TODO: change protected to private and override getters

public class Tile implements Targetable{
    protected GameMap gameMap;
    protected RoomColour colour;
    protected Map<Direction, Boolean> doors;
    protected Set<Figure> figures=new HashSet<>();
    protected Point position;
    protected List<Grabbable> grabbables=new ArrayList<>();

    protected Tile(){}

    protected Tile(Tile t) {
        this.gameMap=t.gameMap;
        this.colour=t.colour;
        this.doors=t.doors;
        this.figures=t.figures;
        this.grabbables=t.grabbables;
        this.position=t.position;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public List<Grabbable> grab (){throw new UnsupportedOperationException("Can't grab from generic tile");}

    public List<Grabbable> getGrabbables() {
        return new ArrayList<>(grabbables);
    }

    public void removeGrabbed(String grabbed){
        for (Grabbable g: getGrabbables()){
            if(g.getName().equalsIgnoreCase(grabbed)) {
                grabbables.remove(g);
                break;
            }
        }
    }

    public void setGrabbables(List<Grabbable> grabbables) {
        this.grabbables = grabbables;
    }

    public void setFigures(Set<Figure> figures) {
        this.figures = figures;
    }

    public RoomColour getColour() {
        return colour;
    }

    public Map<Direction, Boolean> getDoors() {
        return doors;
    }

    public Set<Figure> getFigures() {
        return figures;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    public void add(Grabbable grabbable){throw new UnsupportedOperationException("Can't add Tile");}

    public void addAll (List<Grabbable> grabbablesToAdd){throw new UnsupportedOperationException("Can't add Tiles");}

    @Override
    public void hit(String partialWeaponEffect, List<Targetable> hit, TurnMemory turnMemory) {
        List<Tile> list = new ArrayList<>();
        for(int i = 0; i < hit.size(); i++){
            list.add((Tile) hit.get(0));
        }
        turnMemory.putTiles(partialWeaponEffect, list);
        turnMemory.setLastEffectUsed(partialWeaponEffect);
    }

    @Override
    public List<Targetable> getByEffect(List<String> effects, TurnMemory turnMemory) {
        List<Tile> hit= new ArrayList<>();
        for (String s: effects){
            if (turnMemory.getHitTiles().get(s) != null)
                 hit.addAll(turnMemory.getHitTiles().get(s));
        }
        return new ArrayList<>(hit);
    }

    @Override
    public List<Targetable> getAll() {
        return new ArrayList<>(gameMap.getTiles());
    }

    @Override
    public Map<String, List<Targetable>> getHitTargets(TurnMemory turnMemory) {
        List<Targetable> list;
        Map<String, List<Targetable>> map = new HashMap<>();
        for (String s: turnMemory.getHitTargets().keySet()) {
            list = new ArrayList<>(turnMemory.getHitTargets().get(s));
            map.put(s, list);
        }
        return map;

    }

    @Override
    public void addToSelectionEvent(MVSelectionEvent event, List<Targetable> targets, List<Action> actions) {
        List<Tile> tiles = new ArrayList<>();
        List<Point> points = new ArrayList<>();
        for(Targetable t: targets)
            tiles.add((Tile) t);
        for(Tile t: tiles)
            points.add(t.getPosition());
        event.addActionOnTiles(actions, points);
    }

    @Override
    public List<Targetable> getPlayers () {
        List<Targetable> targetables = new ArrayList<>();
        for (Figure f : figures)
            targetables.add(f.getPlayer());
        return targetables;
    }

    private List<Tile> toTileList(List<Targetable> list){
        List<Tile> tiles = new ArrayList<>();
        for (Targetable t: list) {
            tiles.add((Tile) t);
        }
        return tiles;
    }

    public void addFigure (Figure figure){
        figures.add(figure);
    }

    public void removeFigure (Figure figure){
        figures.remove(figure);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return position.equals(tile.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "colour=" + colour +
                ", position=" + position +
                '}';
    }
}
