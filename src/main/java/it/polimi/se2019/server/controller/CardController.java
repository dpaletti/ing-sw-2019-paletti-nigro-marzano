package it.polimi.se2019.server.controller;

import it.polimi.se2019.commons.mv_events.MVCardEndEvent;
import it.polimi.se2019.commons.mv_events.PartialSelectionEvent;
import it.polimi.se2019.commons.mv_events.PossibleEffectsEvent;
import it.polimi.se2019.server.model.*;
import it.polimi.se2019.server.network.Server;
import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.client.view.VCEvent;

import java.util.*;

public class CardController extends Controller {
    protected Card current;
    protected int layersVisited = 0;
    protected Player currentPlayer;
    protected int layersVisitedPartial = 0;
    protected List<GraphNode<PartialWeaponEffect>> currentLayer= new ArrayList<>();
    protected int partialGraphLayer = -1;
    protected GraphWeaponEffect weaponEffect = null;
    protected List<String> previousTargets = new ArrayList<>();


    public CardController(Game model, Server server, int roomNumber) {
        super(model, server, roomNumber);
    }

    public CardController (Game model){
        this.model=model;
    }

    @Override
    public void update(VCEvent message) {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("CardController ignored " + JsonHandler.serialize(message));
        }
    }

    protected Set<Targetable> intersect (Set<Targetable> first, Set<Targetable> second){
        Set<Targetable> finalSet= new HashSet<>();
        for (Targetable t: first){
            if (second.contains(t))
                finalSet.add(t);
        }
        return finalSet;
    }

    protected void endUsage(boolean isWeapon){
        model.send(new MVCardEndEvent(model.playerToUser(currentPlayer), isWeapon));
        layersVisited = 0;
        layersVisitedPartial = 0;
        currentPlayer = null;
        current = null;
        currentLayer = null;
        partialGraphLayer = -1;
        weaponEffect = null;
        previousTargets.clear();
    }

    protected List<Targetable> generateTargetSet (PartialWeaponEffect effect, Player player){
        Targetable targetable;
        Set<Targetable> targetSet;
        if(effect.getTargetSpecification().getTile())
            targetable= player.getFigure().getTile();
        else
            targetable= player;

        targetSet= intersect(handleVisible(effect.getTargetSpecification().getVisible(), targetable),
                intersect(handleDifferent(targetable,
                        effect.getTargetSpecification().getDifferent().getFirst(),
                        effect.getTargetSpecification().getDifferent().getSecond()),
                        intersect(handlePrevious(targetable,
                                effect.getTargetSpecification().getPrevious().getFirst(),
                                effect.getTargetSpecification().getPrevious().getSecond()),
                                handleRadiusBetween(effect.getTargetSpecification().getRadiusBetween().getFirst(),
                                        effect.getTargetSpecification().getRadiusBetween().getSecond(), targetable, effect.getTargetSpecification().getTile()))));
        return new ArrayList<>(intersect(handleEnlarge(effect.getTargetSpecification().getEnlarge(), targetSet, effect.getTargetSpecification().getTile()), targetSet));
    }


    private Set<Targetable> getVisible(Targetable t){   //tested
        Set<Targetable> visibleTarget= new HashSet<>();
        for (Targetable tCounter: t.getAll()) {
            if (visibleRooms(t.getPosition()).contains(model.getGameMap().getTile(tCounter.getPosition()).getColour())) {
                visibleTarget.add(tCounter);
            }
        }
        visibleTarget.remove(t);
        return visibleTarget;
    }

    private Set<Targetable> handleVisible(int visible, Targetable source){  //tested
        List<Targetable> targetables;
        if (visible==0) {
            targetables= source.getAll();
            targetables.removeAll(getVisible(source));
            targetables.remove(source);
            return new HashSet<>(targetables);
        }

        else if (visible==1)
            return new HashSet<>(getVisible(source));

        else if (visible==2)
            return getVisible(source.getHitTargets(model.getTurnMemory()).get(model.getTurnMemory().getLastEffectUsed()).get(0));
        return new HashSet<>(source.getAll());

    }

    private Set<RoomColour> visibleRooms (Point point){     //tested
        Tile tile = model.getGameMap().getTile(point);
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

    private Set<Targetable> areaSelection (Targetable source, int innerRadius, int outerRadius, boolean isTile){       //tested
        Set<Targetable> targetables = new HashSet<>();
        if(innerRadius==-2 && outerRadius == -2) //redundant because of tile switch in weapon declaration
            return getVisible(source);
        if(innerRadius == -3 && outerRadius == -3) {
            for(Tile t: model.getGameMap().getTiles()) {
                if (!model.getGameMap().getTile(source.getPosition()).getColour().equals(t.getColour()))
                    targetables.add(t);
            }
            return targetables;
        }
        if (innerRadius == outerRadius){
            for(Point p:model.getGameMap().getAllowedMovements(model.getTile(source.getPosition()), innerRadius)){
                targetables.add(model.getTile(p));
            }
            return targetables;
        }
        targetables = new HashSet<>(getTileCircle(outerRadius, source.getPosition(), isTile));
        targetables.removeAll(getTileCircle(innerRadius, source.getPosition(), isTile));
        return targetables;

    }

    //distance -1 as radiusBetween includes both bounds
    private Set<Targetable> getTileCircle (int distance, Point centre, boolean isTile) {
        Set<Targetable> tiles = new HashSet<>();
        if (distance==-1)
            tiles.addAll(model.getGameMap().getTiles());
        for (Tile t : model.getGameMap().getTiles()) {
            if (model.getGameMap().getAllowedMovements(model.getTile(centre), distance-1).contains(t.getPosition()))
                tiles.add(t);
        }
        return handleTargetableTiles(isTile, tiles);
    }

    private Set<Targetable> handleDifferent(Targetable source, boolean different, List<String> effects) {   //tested
        if (different) {
            Set<Targetable> targetables = new HashSet<>(source.getAll());
            targetables.removeAll(model.getTurnMemory().getByEffect(effects, source));
            return targetables;
        }
        return new HashSet<>(source.getAll());
    }

    private Set<Targetable> handlePrevious(Targetable source, boolean previous, List<String> effects){      //tested
        if (previous)
            return new HashSet<>(model.getTurnMemory().getByEffect(effects, source));
        else
            return new HashSet<>(source.getAll());
    }

    private Set<Targetable> handleRadiusBetween (int innerRadius, int outerRadius, Targetable targetable, boolean isTile){      //tested
        return areaSelection(targetable, innerRadius, outerRadius, isTile);
    }

    private Set<Targetable> handleEnlarge (int enlarge, Set<Targetable> centre, boolean isTile){   //tested
        Set<Targetable> targetables= new HashSet<>();
        if(centre.isEmpty())
            return new HashSet<>();
        Targetable targetable= new ArrayList<>(centre).get(0);
        if (enlarge==-2){
            for (Targetable t: targetable.getAll()){
                if (t.getPosition().getX()==targetable.getPosition().getX() || t.getPosition().getY()== targetable.getPosition().getY())
                    targetables.add(t);
            }
            return targetables;
        }
        else if (enlarge==-1){
            for (Targetable t: centre)
                targetables.addAll(model.getGameMap().getRoom(model.getGameMap().getTile(t.getPosition()).getColour()));
            return targetables;
        }
        else if (enlarge==0)
            return new HashSet<>(targetable.getAll());
        for (Targetable t: centre)
            targetables.addAll(getTileCircle(enlarge, t.getPosition(), isTile));
        return targetables;
    }

    private Set<Targetable> handleTargetableTiles (boolean isTile, Set<Targetable> targetables){
        Set<Targetable> finalSet = new HashSet<>();
        if (isTile)
            return targetables;
        else {
            for (Targetable t : targetables)
                finalSet.addAll(t.getPlayers());
            return finalSet;
        }
    }

    // enoughPowerUps() uses missingAmmos() to calculate whether any ammos are missing.
    // If ammos are missing, it calculates if those can be replaced by power ups and,
    // in case player does not own enough power ups to pay, it returns false,

    protected boolean enoughPowerUps (GraphWeaponEffect effect){
        List<PowerUp> ownedPowerUps = new ArrayList<>(currentPlayer.getPowerUps());
        List<Ammo> missingAmmos = missingAmmos(effect);
        boolean flag = false;

        for (Ammo a : missingAmmos){
            for (PowerUp p : ownedPowerUps) {
                if (a.getColour().name().equalsIgnoreCase(p.getColour())) {
                    ownedPowerUps.remove(p);
                    flag = true;
                }
            }
            if (!flag)
                return false;
            flag = false;
        }
        return true;
    }

    // missingAmmos returns missing ammos to pay a price, therefore,
    // when price can be fully payed with ammos, it returns an empty list.

    protected List<Ammo> missingAmmos(GraphWeaponEffect effect){
        List<Ammo> ownedAmmos = new ArrayList<>(currentPlayer.getAmmo());
        List<Ammo> toReturn = new ArrayList<>();
        for (Ammo a : effect.getPrice()){
            if (!ownedAmmos.remove(a))
                toReturn.add(a);
        }
        return toReturn;
    }


    protected void handlePartial (PartialWeaponEffect partial){
        List<Targetable> targets = generateTargetSet(partial, currentPlayer);
        if (partial.getTargetSpecification().getTile())
            model.send(new PartialSelectionEvent(targetableToTile(targets), model.playerToUser(currentPlayer), partial.isEndable()));
        else
            model.send(new PartialSelectionEvent(model.playerToUser(currentPlayer), targetableToPlayer(targets), partial.isEndable()));
    }


    private List<Point> targetableToTile (List<Targetable> targetables){
        List<Point> points = new ArrayList<>();
        for (Targetable t : targetables)
            points.add(t.getPosition());
        return points;
    }

    private List<String> targetableToPlayer (List<Targetable> targetables){
        List<String> players = new ArrayList<>();
        for (Targetable t : targetables){
            players.add(model.playerToUser((Player)t));
        }
        return players;
    }

    protected void handleEffect (){
        currentPlayer.useAmmos(weaponEffect.getPrice());
        layersVisitedPartial = layersVisitedPartial + 1;
        currentLayer= weaponEffect.getEffectGraph().getListLayer(layersVisitedPartial);
        partialGraphLayer++;
        handlePartial(currentLayer.get(partialGraphLayer).getKey());
    }

    protected void nextWeaponEffect (boolean isWeapon){
        List<GraphWeaponEffect> list = new ArrayList<>();
        layersVisited = layersVisited + 1;
        for (GraphNode<GraphWeaponEffect> g: current.getDefinition().getListLayer(layersVisited)) {
            if (missingAmmos(g.getKey()).isEmpty() || enoughPowerUps(g.getKey()))
                list.add(g.getKey());
        }
        if (!list.isEmpty()) {
            PossibleEffectsEvent event = new PossibleEffectsEvent(model.playerToUser(currentPlayer), current.getName(), isWeapon);
            for (GraphWeaponEffect w: list)
                event.addEffect(w.getName(), w.getEffectType());
            model.send(event);
        }
        else
            model.send(new MVCardEndEvent(model.playerToUser(currentPlayer), isWeapon));
    }

}
