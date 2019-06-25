package it.polimi.se2019.controller;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.AvailableWeaponsEvent;
import it.polimi.se2019.model.mv_events.MVWeaponEndEvent;
import it.polimi.se2019.model.mv_events.UsablePowerUpEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeaponController extends CardController {
    private int layersVisitedPartial = 0;
    private int layersVisited = 0;
    protected List<GraphNode<PartialWeaponEffect>> currentLayer= new ArrayList<>();
    private Weapon currentWeapon;
    private Player currentPlayer;

    /* when should this be sent?
                send(new PossibleEffectsEvent(username,
                weaponName,
                model.nameToWeapon(weaponName).getCardColour().getColour().toString(),
                ));*/

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
        model.send(new AvailableWeaponsEvent(message.getSource(),
                Card.cardStringify(Card.cardToCard(model.userToPlayer(message.getSource()).getWeapons()))));
    }

    @Override
    public void dispatch(ChosenWeaponEvent message) {
        currentWeapon= model.nameToWeapon(message.getWeapon());
        nextWeaponEffect();
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
        currentLayer= weaponEffect.getEffectGraph().getListLayer(layersVisitedPartial);
        for(GraphNode<PartialWeaponEffect> p: currentLayer)
            model.addToSelection(message.getSource(),
                    p.getKey().getActions(),
                    generateTargetSet(p.getKey(),
                            model.userToPlayer(message.getSource())));

        model.sendPossibleTargets();
    }

    @Override
    public void dispatch(VCSelectionEvent message) {
        model.usablePowerUps("onAttack", true, currentPlayer);
        for (String s : message.getSelectedPlayers()){
           // currentLayer.get(layersVisited).getKey().getActions().get(layersVisitedPartial).getActionType().apply();
        }
        nextWeaponEffect();
    }

    @Override
    public void dispatch(VCWeaponEndEvent message) {
        layersVisited = 0;
        layersVisitedPartial = 0;
        currentLayer = null;
        currentPlayer = null;
        currentWeapon = null;
    }

    protected List<Targetable> generateTargetSet (PartialWeaponEffect effect, Player player){
        Targetable targetable= null;
        Set<Targetable> targetSet= new HashSet<>();
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
                                        effect.getTargetSpecification().getRadiusBetween().getSecond(), targetable))));
        return new ArrayList<>(intersect(handleEnlarge(effect.getTargetSpecification().getEnlarge(), targetSet), targetSet));

    }

    private Set<Targetable> getVisible(Targetable t){
        Set<Targetable> visibleTarget= new HashSet<>();
       for (Targetable tCounter: t.getAll()) {
            if (visibleRooms(t.getPosition()).contains(model.getGameMap().getTile(tCounter.getPosition()).getColour()))
                visibleTarget.add(tCounter);
        }
        visibleTarget.remove(t);
        return visibleTarget;
    }

    private Set<Targetable> handleVisible(int visible, Targetable source){
        List<Targetable> targetables= null;
        if (visible==0) {
            targetables= source.getAll();
            targetables.removeAll(getVisible(source));
            return new HashSet<>(targetables);
        }

        else if (visible==1)
            return new HashSet<>(getVisible(source));

        else if (visible==2)
            return getVisible(source.getHitTargets(model.getTurnMemory()).get(model.getTurnMemory().getLastEffectUsed()).get(0));
        return new HashSet<>(source.getAll());

    }

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

    private Set<Targetable> areaSelection(Targetable source, int innerRadius, int outerRadius){
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
        targetables = new HashSet<>(getTileCircle(outerRadius, source.getPosition()));
        targetables.removeAll(getTileCircle(innerRadius, source.getPosition()));
        return targetables;

    }

    private Set<Tile> getTileCircle (int distance, Point centre) {
        if (distance==-1)
            return (model.getGameMap().getTiles());
        Set<Tile> tiles = new HashSet<>();
        for (Point p : model.getGameMap().getPoints()) {
            if (p.getDistance(centre) <= distance)
                tiles.add(model.getTile(p));
        }
        return tiles;
    }

    private Set<Targetable> handleDifferent(Targetable source, boolean different, List<String> effects) {
        if (different) {
            Set<Targetable> targetables = new HashSet<>(source.getAll());
            targetables.removeAll(model.getTurnMemory().getByEffect(effects, source));
            return targetables;
        }
        return new HashSet<>(source.getAll());

    }

    private Set<Targetable> handlePrevious(Targetable source, boolean previous, List<String> effects){
        if (previous)
           return new HashSet<>(model.getTurnMemory().getByEffect(effects, source));
        else
            return new HashSet<>(source.getAll());
    }

    private Set<Targetable> handleRadiusBetween (int innerRadius, int outerRadius, Targetable targetable){
        return areaSelection(targetable, innerRadius, outerRadius);
    }

    private Set<Targetable> handleEnlarge (int enlarge, Set<Targetable> centre){
        Set<Targetable> targetables= new HashSet<>();
        if (enlarge==-2){
            Targetable targetable= centre.iterator().next();
            for (Targetable t: targetable.getAll()){
                if (t.getPosition().getX()==targetable.getPosition().getX() || t.getPosition().getY()== targetable.getPosition().getY())
                    targetables.add(t);
            }
            return targetables;
        }
        else if (enlarge==-1){
            for (Targetable t: centre){
                targetables.addAll(model.getGameMap().getRoom(model.getGameMap().getTile(t.getPosition()).getColour()));
            }
            return targetables;
        }
        else if (enlarge==0){
            return new HashSet<>(centre.iterator().next().getAll());
        }
        for (Targetable t: centre){
            targetables.addAll(getTileCircle(enlarge, t.getPosition()));
        }
        return targetables;
     }

    private Set<Targetable> intersect (Set<Targetable> first, Set<Targetable> second){
        Set<Targetable> finalSet= new HashSet<>();
        for (Targetable t: first){
            if (second.contains(t)){
                finalSet.add(t);
            }
        }
        return finalSet;
    }

    protected void nextWeaponEffect (){
        List<GraphWeaponEffect> list = new ArrayList<>();
        layersVisited = layersVisited + 1;
        for (GraphNode<GraphWeaponEffect> g: currentWeapon.getDefinition().getListLayer(layersVisited))
            list.add(g.getKey());
        if (!list.isEmpty()) {
            PossibleEffectsEvent event = new PossibleEffectsEvent(model.playerToUser(currentPlayer), currentWeapon.getName());
            for (GraphWeaponEffect w: list)
                event.addEffect(w.getName(), w.getEffectType());
            model.send(event);
        }
        else
            model.send(new MVWeaponEndEvent(model.playerToUser(currentPlayer)));
    }

    private void handlePartial (PartialWeaponEffect partial){

    }
}
