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
    protected WeaponSpot weaponSpot;
    protected LootCard loot;
    protected Point position;
    protected List<Tear> hp;

    public Tile (GameMap gameMap, RoomColour colour, Map<Direction, Boolean> doors, Set<Figure> figures, WeaponSpot weaponSpot, LootCard loot, Point position, List<Tear> hp){
        this.gameMap = gameMap;
        this.colour= colour;
        this.doors= doors;
        this.figures=figures;
        this.weaponSpot=weaponSpot;
        this.loot=loot;
        this.position=position;
        this.hp=hp;
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

    public void setWeaponSpot(WeaponSpot weaponSpot) {
        this.weaponSpot = weaponSpot;
    }

    public void setLoot(LootCard loot) { this.loot = loot; }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setHp(List<Tear> hp) {
        this.hp = hp;
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

    public WeaponSpot getWeaponSpot() {
        return weaponSpot;
    }

    public LootCard getLoot() {
        return loot;
    }

    @Override
    public Point getPosition() {
        return position;
    }

    public List<Tear> getHp() {
        return hp;
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

    public void addTear(FigureColour figureColour){
        Tear tearToAdd= new Tear(figureColour);
        hp.add(tearToAdd);
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
