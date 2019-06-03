package it.polimi.se2019.controller;

import it.polimi.se2019.model.*;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.ChosenEffectEvent;
import it.polimi.se2019.view.vc_events.ChosenWeaponEvent;
import it.polimi.se2019.view.vc_events.ShootEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeaponController extends Controller {

    public WeaponController (Server server, int roomNumber, Game model){
        super(model, server, roomNumber);
    }
    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("WeaponController ignored " + JsonHandler.serialize(message));
        }
    }

    @Override
    public void dispatch(ShootEvent message) {
        model.sendAvailableWeapons(message.getSource());
    }

    @Override
    public void dispatch(ChosenWeaponEvent message) {
        model.sendPossibleEffects(message.getSource(), message.getWeapon());
    }

    @Override
    public void dispatch(ChosenEffectEvent message) {
        Weapon weapon= model.nameToWeapon(message.getWeapon());
    }


    private Set<Player> getVisiblePlayers(Player player){
        Set<Player> visiblePlayers= new HashSet<>();

        for (Player p: model.getPlayers()) {
            if (visibleRooms(player.getFigure().getTile()).contains(p.getFigure().getTile().getColour()))
                visiblePlayers.add(p);
        }
        visiblePlayers.remove(player);
        return visiblePlayers;
    }

    private Set<Tile> getVisibleTiles(Tile tile){
        Set<Tile> visibleTiles= new HashSet<>();

        for (Tile t: model.getGameMap().getTiles()) {
            if (visibleRooms(tile).contains(t.getColour()))
                visibleTiles.add(t);
        }
        visibleTiles.remove(tile);
        return visibleTiles;
    }

    private Set<Player> handleVisibilePlayers (List<Player> targetSet, int visible, Player player){

        if (visible==0) {
            targetSet.removeAll(getVisiblePlayers(player));
            return new HashSet<>(targetSet);
        }

        else if (visible==1){
            List<Player> temp= new ArrayList<>(targetSet);
            temp.removeAll(getVisiblePlayers(player));
            targetSet.removeAll(temp);
            return new HashSet<>(targetSet);
        }

        else if (visible==2)
            return getVisiblePlayers
                    (player.getTurnMemory().getHitTargets().
                            get(player.getTurnMemory().getLastEffectUsed()).get(0));
        return new HashSet<>();
    }

    private Set<Tile> handleVisibleTiles (List<Tile> tileSet, int visible, Player player){
        if (visible==0){
            tileSet.removeAll(getVisibleTiles(player.getFigure().getTile()));
            return new HashSet<>(tileSet);
        }

        else if (visible==1){
            List<Tile> temp= new ArrayList<>(tileSet);
            temp.removeAll(getVisibleTiles(player.getFigure().getTile()));
            tileSet.removeAll(temp);
            return new HashSet<>(tileSet);
        }

        else if (visible==2){
            return getVisibleTiles
                    (player.getTurnMemory().getHitTargets().
                            get(player.getTurnMemory().
                                    getLastEffectUsed()).get(0).getFigure().getTile());
        }
        return new HashSet<>();
    }


    private Set<RoomColour> visibleRooms (Tile tile){
        Set<RoomColour> visibleRooms= new HashSet<>();

        visibleRooms.add(tile.getColour());

        if (tile.getDoors().get(Direction.NORTH))
            visibleRooms.add(model.getTile
                    (new Point
                            (tile.getPosition().getX(),
                                    tile.getPosition().getY()+1)).getColour());

        if (tile.getDoors().get(Direction.SOUTH))
            visibleRooms.add(model.getTile
                    (new Point
                            (tile.getPosition().getX(),
                                    tile.getPosition().getY()-1)).getColour());

        if (tile.getDoors().get(Direction.EAST))
            visibleRooms.add(model.getTile
                    (new Point
                            (tile.getPosition().getX()+1,
                                    tile.getPosition().getY())).getColour());

        if (tile.getDoors().get(Direction.WEST))
            visibleRooms.add(model.getTile
                    (new Point
                            (tile.getPosition().getX()-1,
                                    tile.getPosition().getY())).getColour());
        return visibleRooms;
    }

    private Set<Player> areaSelectionPlayer (Player player, int innerRadius, int outerRadius){
        Set<Player> targets= new HashSet<>();
        for (Tile t: areaSelectionTile(player.getFigure().getTile(), innerRadius, outerRadius)){
            for (Figure f: t.getFigures())
                targets.add(f.getPlayer());
        }
        return targets;
    }

    private Set<Tile> areaSelectionTile (Tile tile, int innerRadius, int outerRadius){
        Set<Tile> tiles= new HashSet<>();
        if (innerRadius==-2 && outerRadius==-2) //redundant because of tile switch in weapon declaration
            return getVisibleTiles(tile);
        if (innerRadius==-3 && outerRadius==-3){
            for (Tile t: model.getGameMap().getTiles()){
                if (!t.getColour().equals(tile.getColour()))
                    tiles.add(t);
            }
            return tiles;
        }
        tiles = getTileCircle(outerRadius, tile.getPosition());
        tiles.removeAll(getTileCircle(innerRadius, tile.getPosition()));
        return tiles;
    }

    private Set<Tile> getTileCircle (int distance, Point centre) {
        if (distance==-1)
            return (model.getGameMap().getTiles());
        Set<Tile> tiles = new HashSet<>();
        for (Point p : model.getGameMap().getMap().keySet()) {
            if (p.getDistance(centre) <= distance)
                tiles.add(model.getTile(p));
        }
        return tiles;
    }



    private Set<Tile> handleDifferentTiles (Set<Tile> tileSet, boolean different, List<String> effects, Player player){
        if (different)
            tileSet.removeAll(player.getTurnMemory().getTilesByEffect(effects));
        return tileSet;
    }

    private Set<Player> handleDifferentPlayers (Set<Player> targetSet, boolean different, List<String> effects, Player player){
        if (different)
            targetSet.removeAll(player.getTurnMemory().getPlayersByEffect(effects));
        return targetSet;
    }

    private Set<Tile> handlePreviousTiles (Set<Tile> tileSet, boolean previous, List<String> effects, Player player){
        if (previous)
            return ballet(tileSet, new HashSet<>(player.getTurnMemory().getTilesByEffect(effects)));
        else
            return tileSet;
    }


    private void generateTargetSet (PartialWeaponEffect effect, Player player){
        TargetSpecification targetSpecification= effect.getTargetSpecification();
        List<Player> targetSet= model.getPlayers();
        List<Tile> tileSet= new ArrayList<>(model.getGameMap().getTiles());
        if (targetSpecification.getTile()) {
            tileSet= new ArrayList<>(ballet
                    (new HashSet<>(tileSet),
                            new HashSet<>(handleTile(tileSet, player, targetSpecification))));

        }
        else {
            targetSet= new ArrayList<>(ballet(
                    new HashSet<>(targetSet),
                    new HashSet<>(handlePlayer(targetSet, player, targetSpecification))));
        }
    }

    private Set<Tile> handleTile (List<Tile> tileSet, Player player, TargetSpecification targetSpecification){
        Set<Tile> visibleTiles = handleVisibleTiles(tileSet, targetSpecification.getVisible(), player);
        Set<Tile> differentTiles = handleDifferentTiles(new HashSet<>(tileSet), targetSpecification.getDifferent().getFirst(), targetSpecification.getDifferent().getSecond(), player);
        Set<Tile> previousTiles = handlePreviousTiles(new HashSet<>(tileSet), targetSpecification.getDifferent().getFirst(), targetSpecification.getDifferent().getSecond(), player);
        return null;
    }

    private Set<Player> handlePlayer (List<Player> targetSet, Player player, TargetSpecification targetSpecification){
        return null;
    }

    private <T> Set<T> ballet (Set<T> completeSet, Set<T> inOut){
        Set<T> temp= new HashSet<>(completeSet);
        temp.removeAll(inOut);
        completeSet.removeAll(temp);
        return completeSet;
    }
}
