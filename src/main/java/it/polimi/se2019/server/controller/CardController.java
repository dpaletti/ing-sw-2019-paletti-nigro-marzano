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

/**
 * This class handles the generic interaction with a Card that can both be a Weapon or a Power Up.
 * It implements the methods that calculate all the possible targets of a certain action which are used by its subclasses
 * {@link it.polimi.se2019.server.controller.PowerUpController} and {@link it.polimi.se2019.server.controller.WeaponController}.
 */

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

    /**
     * This method ignores the events that are not dispatched in this controller.
     * @param message Any message arriving from the view.
     */
    @Override
    public void update(VCEvent message) {
        if(disabled)
            return;
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //ignore events that this controller does not support
            Log.fine("CardController ignored " + JsonHandler.serialize(message));
        }
    }

    /**
     * Intersects the first and the second given sets.
     * @param first first set to intersect.
     * @param second second set to intersect.
     * @return returns the common values between first and second.
     */
    protected Set<Targetable> intersect (Set<Targetable> first, Set<Targetable> second){
        Set<Targetable> finalSet= new HashSet<>();
        for (Targetable t: first){
            if (second.contains(t))
                finalSet.add(t);
        }
        return finalSet;
    }

    /**
     * This method sets to their initial value all of the indexes used.
     * @param isWeapon a boolean that defines whether a card is a weapon or a power up.
     */
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

    /**
     * Generates the potential targets of a weapon or power up given the desired partial weapon effect.
     * @param effect the chosen effect to apply.
     * @param player the player applying the effect.
     * @return A list of potential targets.
     */
    protected List<Targetable> generateTargetSet (PartialWeaponEffect effect, Player player){
        Targetable targetable;
        Set<Targetable> targetSet;
        Set<Targetable> lastFilter;
        if(effect.getTargetSpecification().getTile()) {
            targetable = player.getFigure().getTile();
            lastFilter=new HashSet<>(targetable.getAll());
        }
        else {
            targetable = player;
            lastFilter=new HashSet<>(targetable.getAll());
            lastFilter.remove(targetable);
        }

        targetSet= intersect(handleVisible(effect.getTargetSpecification().getVisible(), targetable),
                intersect(handleDifferent(targetable,
                        effect.getTargetSpecification().getDifferent().getFirst(),
                        effect.getTargetSpecification().getDifferent().getSecond()),
                        intersect(handlePrevious(targetable,
                                effect.getTargetSpecification().getPrevious().getFirst(),
                                effect.getTargetSpecification().getPrevious().getSecond()),
                                handleRadiusBetween(effect.getTargetSpecification().getRadiusBetween().getFirst(),
                                        effect.getTargetSpecification().getRadiusBetween().getSecond(), targetable, effect.getTargetSpecification().getTile()))));
        if(effect.getTargetSpecification().getEnlarge()==0)
            targetSet=intersect(handleEnlarge(effect.getTargetSpecification().getEnlarge(), targetSet, effect.getTargetSpecification().getTile()), targetSet);
        else if (effect.getTargetSpecification().getEnlarge()==-3){
            List<Targetable> targets=new ArrayList<>();
            targets.add(player);
            targetSet=new HashSet<>(targets);
        }else
            targetSet=handleEnlarge(effect.getTargetSpecification().getEnlarge(), targetSet, effect.getTargetSpecification().getTile());
        if(!(effect.getActions().size()==1 && effect.getActions().get(0).getActionType().equals(ActionType.MOVE)))
            targetSet=intersect(targetSet,lastFilter);
        return new ArrayList<>(targetSet);
    }

    /**
     * A method that calculates which targets can be seen by the player or from its position.
      * @param t the player playing or the tile it is standing on.
     * @return A Set of targets that are visible. The targets will be players if t is a player, tiles if t is a tile.
     */

    private Set<Targetable> getVisible(Targetable t){
        try {
            Set<Targetable> visibleTarget = new HashSet<>();
            Set<RoomColour> visibleRooms = visibleRooms(t.getPosition());
            for (Targetable tCounter : t.getAll()) {
                if (visibleRooms
                        .contains(model.getGameMap()
                                .getTile(tCounter
                                        .getPosition()).getColour())) {
                    visibleTarget.add(tCounter);
                }
            }
            return visibleTarget;
        }catch (NullPointerException e){
            Log.severe("NullPointer getVisible: "+t.toString());
            return Collections.emptySet();
        }

    }

    /**
     * A method that, reading the visible value, generates the target set with the correct visibility.
     * @param visible When visible is -1, the visibility property will be deactivated, when it is 0 the required targets are not visibile, when 1 they are visible and when 2 they are visible by a previous targe.
     * @param source t the player playing or the tile it is standing on.
     * @return the targets with the required visibility properties.
     */

    private Set<Targetable> handleVisible(int visible, Targetable source){
        List<Targetable> targetables;
        if (visible==0) {
            targetables= source.getAll();
            targetables.removeAll(getVisible(source));
            return new HashSet<>(targetables);
        }

        else if (visible==1)
            return new HashSet<>(getVisible(source));

        else if (visible==2) {
            Targetable hit=source.getHitTargets(model.getTurnMemory()).get(model.getTurnMemory().getLastEffectUsed()).get(0);
            Set<Targetable> targetableSet=getVisible(hit);
            targetableSet.remove(hit);
        }
        return new HashSet<>(source.getAll());

    }

    /**
     * A method that calculates the rooms that are visible to the source.
     * @param point The tile the player is standing on.
     * @return A set of room colours that are visible to the source.
     */

    private Set<RoomColour> visibleRooms (Point point){
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

    /**
     * Calculates the area at required distance from the source.
     * @param source The point from which the distance is calculated.
     * @param innerRadius The minimum distance.
     * @param outerRadius The maximum distance.
     * @param isTile Whether the effect is applied to tiles or to players.
     * @return A set of targets at the correct distance from the source.
     */

    private Set<Targetable> areaSelection (Targetable source, int innerRadius, int outerRadius, boolean isTile){       //tested
        Set<Targetable> targetables = new HashSet<>();
        if(innerRadius==-2 && outerRadius == -2) //redundant because of tile switch in weapon declaration
            return getVisible(source);
        if(innerRadius == -3 && outerRadius == -3) {
            for(Tile t: model.getGameMap().getTiles()) {
                if (!model.getGameMap().getTile(source.getPosition()).getColour().equals(t.getColour()))
                    targetables.add(t);
            }
            return handleTargetableTiles(isTile,targetables);
        }
        if (innerRadius == outerRadius){
            if (innerRadius==-1){
                return new HashSet<>(source.getAll());
            }
            for(Point p:model.getGameMap().getAllowedMovements(model.getTile(source.getPosition()), innerRadius)){
                targetables.add(model.getTile(p));
            }
            return handleTargetableTiles(isTile,targetables);
        }
        if(outerRadius == -1){
            Set<Targetable> allTile = new HashSet<>(source.getAll());
            Set<Targetable> circle = getTileCircle(innerRadius, currentPlayer.getPosition(), true);
            allTile.removeAll(circle);
            return allTile;
        }
        targetables = getTileCircle(outerRadius, source.getPosition(), isTile);
        targetables.removeAll(getTileCircle(innerRadius, source.getPosition(), isTile));
        return targetables;

    }

    /**
     * This method calculates the tiles at a defined distance or less.
     * @param distance the maximum distance.
     * @param centre the centre from which the distance is calculated.
     * @param isTile whether the targets are players or tiles.
     * @return A set of tiles within a maximum distance.
     */
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

    /**
     * Calculates the targetset based on the different property.
     * @param source t the player playing or the tile it is standing on.
     * @param different the different property: 0 when targets accepted are not different from certain effects, 1 when they need to be different from those effects.
     * @param effects the effects whose targets ought to be different from the current ones.
     * @return targets that fit the different properties.
     */

    private Set<Targetable> handleDifferent(Targetable source, boolean different, List<String> effects) {   //tested
        if (different) {
            Set<Targetable> targetables = new HashSet<>(source.getAll());
            targetables.removeAll(model.getTurnMemory().getByEffect(effects, source));
            return targetables;
        }
        return new HashSet<>(source.getAll());
    }

    /**
     * Calculates the targetset based on the previous property.
     * @param source t the player playing or the tile it is standing on.
     * @param previous the previous property: 0 when targets accepted are not the same as those of certain effects, 1 when they need to be the same as those effects.
     * @param effects the effects whose targets ought to be different from the current ones.
     * @return targets that fit the previous properties.
     */

    private Set<Targetable> handlePrevious(Targetable source, boolean previous, List<String> effects){      //tested
        if (previous)
            return new HashSet<>(model.getTurnMemory().getByEffect(effects, source));
        else
            return new HashSet<>(source.getAll());
    }

    /**
     * This method calculates which targets comply to the given specifications.
     * @param innerRadius the minimum distance of the targets.
     * @param outerRadius the maximum distance of the targets.
     * @param targetable the source from which the distance is calculated.
     * @param isTile whether the hit targets are players or tiles.
     * @return a set of targets that comply to the given specifications.
     */
    private Set<Targetable> handleRadiusBetween (int innerRadius, int outerRadius, Targetable targetable, boolean isTile){      //tested
        return areaSelection(targetable, innerRadius, outerRadius, isTile);
    }

    /**
     * This method calculates which targets comply to the given specifications.
     * @param enlarge When enlarge is -1, it selects the whole room, when -1 it selects a specific direction and when it is any other value it defines the distance the tiles to add need to be from the source.
     * @param centre the source of the enlarge.
     * @param isTile whether the hit targets are players or tiles.
     * @return
     */

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
        else if(enlarge==-3)
            return centre;
        for (Targetable t: centre)
            targetables.addAll(getTileCircle(enlarge, t.getPosition(), isTile));
        return targetables;
    }

    /**
     * This method transforms targetables to tiles
     * @param isTile
     * @param targetables the targetables to transform
     * @return the transformed tiles.
     */
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

    /**
     * enoughPowerUps() uses missingAmmos() to calculate whether any ammos are missing.
     * If ammos are missing, it calculates if those can be replaced by power ups.
     * @param effect the chosen effect whose price has to be paid.
     * @return In case player does not own enough power ups to pay, it returns false.
     */

    protected boolean enoughPowerUps (GraphWeaponEffect effect){
        List<PowerUp> ownedPowerUps = new ArrayList<>(currentPlayer.getPowerUps());
        List<Ammo> missingAmmos = missingAmmos(effect);
        boolean flag = false;
        List<PowerUp> powerUps=new ArrayList<>(ownedPowerUps);
        for (Ammo a : missingAmmos){
            for (PowerUp p : powerUps) {
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

    /**
     * missingAmmos returns missing ammos to pay a price.
     * @param effect the effect that has to be paid.
     * @return  when price can be fully payed with ammos, it returns an empty list, else the missing ammos.
     */

    protected List<Ammo> missingAmmos(GraphWeaponEffect effect){
        List<Ammo> ownedAmmos = new ArrayList<>(currentPlayer.getAmmo());
        List<Ammo> toReturn = new ArrayList<>();
        for (Ammo a : effect.getPrice()){
            if (!ownedAmmos.remove(a))
                toReturn.add(a);
        }
        return toReturn;
    }

    /**
     * This method asks the user whether they would like to use a specific effect.
     * @param partial the proposed partial effect.
     * @param isWeapon whether the method handles a weapon or a power up.
     */

    protected void handlePartial (PartialWeaponEffect partial,boolean isWeapon){
        List<Targetable> targets = generateTargetSet(partial, currentPlayer);
        if (partial.getTargetSpecification().getTile())
            model.send(new PartialSelectionEvent(targetableToTile(targets), model.playerToUser(currentPlayer), partial.isEndable(),isWeapon));
        else
            model.send(new PartialSelectionEvent(model.playerToUser(currentPlayer), targetableToPlayer(targets), partial.isEndable(),isWeapon));
    }

    /**
     * Transforms targetables to tiles.
     * @param targetables the targetables to transform.
     * @return the points of the tiles transformed.
     */
    private List<Point> targetableToTile (List<Targetable> targetables){
        List<Point> points = new ArrayList<>();
        for (Targetable t : targetables)
            points.add(t.getPosition());
        return points;
    }

    /**
     * Transforms targetables to players.
     * @param targetables the targetables to transform.
     * @return the users of the players transformed.
     */
    private List<String> targetableToPlayer (List<Targetable> targetables){
        List<String> players = new ArrayList<>();
        for (Targetable t : targetables){
            players.add(model.playerToUser((Player)t));
        }
        return players;
    }

    /**
     * It iterates on the graph of effects.
     * @param isWeapon defines whether the current card is a weapon or a power up.
     */

    protected void handleEffect (boolean isWeapon){
        currentPlayer.useAmmos(weaponEffect.getPrice());
        layersVisitedPartial = layersVisitedPartial + 1;
        currentLayer= weaponEffect.getEffectGraph().getListLayer(layersVisitedPartial);
        partialGraphLayer++;
        handlePartial(currentLayer.get(partialGraphLayer).getKey(),isWeapon);
    }

    /**
     * Calculates the following effect in the graph.
     * @param isWeapon defines whether the current card is a weapon or a power up.
     */

    protected void nextWeaponEffect (boolean isWeapon){
        partialGraphLayer=-1;
        layersVisitedPartial=0;
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
        else{
            endUsage(isWeapon);
        }
    }

}
