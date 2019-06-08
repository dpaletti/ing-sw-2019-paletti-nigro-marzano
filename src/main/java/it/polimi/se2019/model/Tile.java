package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.MVSelectionEvent;
import it.polimi.se2019.utility.Action;
import it.polimi.se2019.utility.Point;

import java.util.*;

public abstract class Tile implements Targetable{
    protected GameMap gameMap;
    protected RoomColour colour;
    protected Map<Direction, Boolean> doors;
    protected Set<Figure> figures;
    protected Point position;
    protected List<Grabbable> grabbables;

    public Tile (GameMap gameMap, RoomColour colour, Map<Direction, Boolean> doors, Point position, List<Grabbable> grabbables){
        this.gameMap = gameMap;
        this.colour= colour;
        this.doors= doors;
        this.position=position;
        this.grabbables= new ArrayList<>(grabbables);
    }

    public abstract List<Grabbable> grab ();

    public List<Grabbable> getGrabbables() {
        return grabbables;
    }

    public void setGrabbables(List<Grabbable> grabbables) {
        this.grabbables = grabbables;
    }

    public void setColour(RoomColour colour) {
        this.colour = colour;
    }

    public void setDoors(Map<Direction, Boolean> doors) {
        this.doors = doors;
    }

    public void setFigures(Set<Figure> figures) {
        this.figures = figures;
    }

    public void setPosition(Point position) {
        this.position = position;
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

    public LootCard getLootCard(){
        return null;
    }

    public Weapon getWeapon(Weapon weapon){
        return null;
    }

    public TileType getTileType(){
        return null;
    }

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

    private List<Tile> toTileList(List<Targetable> list){
        List<Tile> tiles = new ArrayList<>();
        for (Targetable t: list) {
            tiles.add((Tile) t);
        }
        return tiles;
    }
}
