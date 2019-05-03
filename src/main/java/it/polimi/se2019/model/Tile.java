package it.polimi.se2019.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Tile {
    protected RoomColour colour;
    protected Map<Direction, Boolean> doors;
    protected Set<Figure> figures;
    protected WeaponSpot weaponSpot;
    protected LootCard loot;
    protected Point position;
    protected List<Tear> hp;

    public Tile (RoomColour colour, Map<Direction, Boolean> doors, Set<Figure> figures, WeaponSpot weaponSpot, LootCard loot, Point position, List<Tear> hp){
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
    public Integer findDistance (){
        return null;
    }

    public Set<Tile> visibleTiles() {
        Set<Tile> visibleTiles=null;
        Point point= null;
        Tile pointToTile=null;
        for (Tile tileCounter: GameMap.getTiles()){
            if(tileCounter.colour.equals(colour)){
                visibleTiles.add(tileCounter);
            }
        }
        if (doors.get(Direction.NORTH)){
            point.setX(position.getX());
            point.setY(position.getY()+1);
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.position.equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.colour.equals(pointToTile.colour)){
                    visibleTiles.add(tileCounter);
                }
            }
        }
        if (doors.get(Direction.SOUTH)){
            point.setX(position.getX());
            point.setY(position.getY()-1);
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.position.equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.colour.equals(pointToTile.colour)){
                    visibleTiles.add(tileCounter);
                }
            }
        }
        if (doors.get(Direction.EAST)){
            point.setX(position.getX()+1);
            point.setY(position.getY());
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.position.equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.colour.equals(pointToTile.colour)){
                    visibleTiles.add(tileCounter);
                }
            }
        }
        if (doors.get(Direction.WEST)){
            point.setX(position.getX()-1);
            point.setY(position.getY());
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.position.equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.colour.equals(pointToTile.colour)){
                    visibleTiles.add(tileCounter);
                }
            }
        }
        return visibleTiles;
    }

    public Set<Tile> visibleTiles(Tile tile) {
        Set<Tile> visibleTiles=null;
        Point point= null;
        Tile pointToTile=null;
        for (Tile tileCounter: GameMap.getTiles()){
            if(tileCounter.colour.equals(tile.colour)){
                visibleTiles.add(tileCounter);
            }
        }
        if (tile.doors.get(Direction.NORTH)){
            point.setX(tile.position.getX());
            point.setY(tile.position.getY()+1);
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.position.equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.colour.equals(pointToTile.colour)){
                    visibleTiles.add(tileCounter);
                }
            }
        }
        if (tile.doors.get(Direction.SOUTH)){
            point.setX(tile.position.getX());
            point.setY(tile.position.getY()-1);
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.position.equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.colour.equals(pointToTile.colour)){
                    visibleTiles.add(tileCounter);
                }
            }
        }
        if (tile.doors.get(Direction.EAST)){
            point.setX(tile.position.getX()+1);
            point.setY(tile.position.getY());
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.position.equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.colour.equals(pointToTile.colour)){
                    visibleTiles.add(tileCounter);
                }
            }
        }
        if (tile.doors.get(Direction.WEST)){
            point.setX(tile.position.getX()-1);
            point.setY(tile.position.getY());
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.position.equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.colour.equals(pointToTile.colour)){
                    visibleTiles.add(tileCounter);
                }
            }
        }
        return visibleTiles;
    }

}
