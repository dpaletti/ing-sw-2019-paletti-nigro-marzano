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
    private int layersVisitedPartial = 0;
    private int layersVisited = 0; //TODO where to iterate on this

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
        List<GraphWeaponEffect> list = new ArrayList<>();
        layersVisited = layersVisited + 1;
        for (GraphNode<GraphWeaponEffect> g: model.nameToWeapon(message.getWeapon()).getDefinition().getListLayer(layersVisited))
            list.add(g.getKey());
        model.sendPossibleEffects(message.getSource(), message.getWeapon(), list);
    }

    @Override
    public void dispatch(ChosenEffectEvent message) {
        GraphWeaponEffect weaponEffect = null;

        for (GraphNode<GraphWeaponEffect> w: model.nameToWeapon(message.getWeapon()).getDefinition()) {
            if(w.getKey().getName().equals(message.getEffectName())) {
                weaponEffect = w.getKey();
                break;
            }
        }
        if(weaponEffect == null)
            throw new NullPointerException("Could not find " + message.getEffectName() + " in " + message.getWeapon());

        layersVisitedPartial = layersVisitedPartial + 1;
        for(GraphNode<PartialWeaponEffect> p: weaponEffect.getEffectGraph().getListLayer(layersVisitedPartial)) {
            model.addToSelection(message.getSource(), p.getKey().getActions(), generateTargetSet(p.getKey(), model.userToPlayer(message.getSource())));
        }
        model.sendPossibleTargets();
    }


    private List<Targetable> generateTargetSet (PartialWeaponEffect effect, Player player){
        TargetSpecification targetSpecification= effect.getTargetSpecification();
        List<Player> targetSet= model.getPlayers();
        List<Tile> tileSet= new ArrayList<>(model.getGameMap().getTiles());

    }

    private Set<Targetable> getVisible(Targetable t){
        Set<Targetable> visibleTarget= new HashSet<>();
        while(t.getAll().iterator().hasNext()) {
            if (visibleRooms(t.getPosition()).contains(model.getGameMap().getMap().get(t.getPosition()).getColour()))
                visibleTarget.add( t.getAll().iterator().next());
        }
        visibleTarget.remove(t);
        return visibleTarget;

    }

    private Set<Targetable> handleVisibile(List<Targetable> targetSet, int visible, Targetable source){
        if (visible==0) {
            targetSet.removeAll(getVisible(source));
            return new HashSet<>(targetSet);
        }

        else if (visible==1){
            List<Targetable> temp= new ArrayList<>(targetSet);
            temp.removeAll(getVisible(source));
            targetSet.removeAll(temp);
            return new HashSet<>(targetSet);
        }

        else if (visible==2)
            return getVisible(source.getHitTargets(model.getTurnMemory()).get(model.getTurnMemory().getLastEffectUsed()).get(0));
        return new HashSet<>(targetSet);

    }

    private Set<RoomColour> visibleRooms (Point point){
        Tile tile = model.getGameMap().getMap().get(point);
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

    private Set<Targetable> areaSelection(Targetable target, int innerRadius, int outerRadius){
        Set<Targetable> targetables = new HashSet<>();
        if(innerRadius==-2 && outerRadius == -2) //redundant because of tile switch in weapon declaration
            return getVisible(target);
        if(innerRadius == -3 && outerRadius == -3) {
            for(Tile t: model.getGameMap().getTiles()) {
                if (model.getGameMap().getMap().get(target.getPosition()).getColour().equals(t.getColour()))
                    targetables.add(t);
            }
            return targetables;
        }
        targetables = new HashSet<>(getTileCircle(outerRadius, target.getPosition()));
        targetables.removeAll(getTileCircle(innerRadius, target.getPosition()));
        return targetables;

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

    private Set<Targetable> handleDifferent(Set<Targetable> targetSet, boolean different, List<String> effects) {
        if (different)
            targetSet.removeAll(model.getTurnMemory().getByEffect(effects, targetSet.iterator().next()));
        return targetSet;

    }

    private Set<Targetable> handlePrevious(Set<Targetable> targetables, boolean previous, List<String> effects){
        if (previous)
            return ballet(targetables, new HashSet<>(model.getTurnMemory().getByEffect(effects, targetables.iterator().next())));
        else
            return targetables;
    }



    private Set<Tile> handleRadiusBetweenTiles (int innerRadius, int outerRadius, Player player){
        return areaSelectionTile (player.getFigure().getTile(), innerRadius, outerRadius);
    }

    private Set<Player> handleRadiusBetweenPlayers (int innerRadius, int outerRadius, Player player){
        return areaSelectionPlayer(player, innerRadius, outerRadius);
    }

    private Set <Tile> handleEnlargeTile (int enlarge, Set<Tile> centre){
        Set<Tile> tiles= new HashSet<>();
        for (Tile t: centre){
            tiles.addAll(getTileCircle(enlarge, t.getPosition()));
        }
        return tiles;
    }




    private Set<Targetable> handleTarget(List<Targetable> targetSet,Targetable target, TargetSpecification targetSpecification){

        Set<Tile> visibleTiles = ballet(new HashSet<>(targetSet), handleVisible (targetSet, targetSpecification.getVisible(), target));
    }


    private Set<Tile> handleTile (List<Tile> tileSet, Player player, TargetSpecification targetSpecification){

        Set<Tile> visibleTiles = ballet(new HashSet<>(tileSet), handleVisibleTiles (tileSet, targetSpecification.getVisible(), player));

        Set<Tile> differentTiles = ballet(visibleTiles, handleDifferentTiles
                (new HashSet<>(tileSet),
                        targetSpecification.getDifferent().getFirst(),
                        targetSpecification.getDifferent().getSecond(), player));

        Set<Tile> previousTiles = ballet(differentTiles,
                handlePreviousTiles (new HashSet<>(tileSet),
                        targetSpecification.getDifferent().getFirst(),
                        targetSpecification.getDifferent().getSecond(), player));

        Set<Tile> radiusBetweenTiles = ballet(previousTiles,
                handleRadiusBetweenTiles (targetSpecification.getRadiusBetween().getFirst(),
                        targetSpecification.getRadiusBetween().getSecond(), player));

        return ballet(radiusBetweenTiles, handleEnlargeTile(targetSpecification.getEnlarge(), radiusBetweenTiles));


    }

    private Set<Player> handlePlayer (List<Player> targetSet, Player player, TargetSpecification targetSpecification){
        {

            Set<Player> visiblePlayers = ballet(new HashSet<>(targetSet), handleVisibilePlayers (targetSet, targetSpecification.getVisible(), player));

            Set<Player> differentPlayers = ballet(visiblePlayers, handleDifferentPlayers
                    (new HashSet<>(targetSet),
                            targetSpecification.getDifferent().getFirst(),
                            targetSpecification.getDifferent().getSecond(), player));

            Set<Player> previousPlayers = ballet(differentPlayers,
                    handlePreviousPlayers (new HashSet<>(targetSet),
                            targetSpecification.getDifferent().getFirst(),
                            targetSpecification.getDifferent().getSecond(), player));

            return ballet(previousPlayers,
                    handleRadiusBetweenPlayers (targetSpecification.getRadiusBetween().getFirst(),
                            targetSpecification.getRadiusBetween().getSecond(), player));


        }
    }

    private Set<Targetable> ballet (Set<Targetable> completeSet, Set<Targetable> inOut){
        Set<Targetable> temp= new HashSet<>(completeSet);
        temp.removeAll(inOut);
        completeSet.removeAll(temp);
        return completeSet;
    }
}
