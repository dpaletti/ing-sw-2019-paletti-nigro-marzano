package it.polimi.se2019.controller;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.*;

import java.lang.annotation.Target;
import java.util.*;

public class WeaponController extends CardController {
    private int partialGraphLayer = -1;
    private GraphWeaponEffect weaponEffect = null;
    /* when should this be sent?
                send(new PossibleEffectsEvent(username,
                weaponName,
                model.nameToWeapon(weaponName).getCardColour().getColour().toString(),
                ));*/

    public WeaponController (Server server, int roomNumber, Game model){
        super(model, server, roomNumber);
    }

    public WeaponController (Game model){
        super();
        this.model = model;
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
        currentPlayer = model.userToPlayer(message.getSource());
        model.send(new AvailableWeaponsEvent(message.getSource(),
                Card.cardStringify(Card.cardToCard(model.userToPlayer(message.getSource()).getWeapons()))));
    }

    @Override
    public void dispatch(ChosenWeaponEvent message) {
        current = model.nameToWeapon(message.getWeapon());
        nextWeaponEffect();
    }

    private void nextWeaponEffect (){
        List<GraphWeaponEffect> list = new ArrayList<>();
        layersVisited = layersVisited + 1;
        for (GraphNode<GraphWeaponEffect> g: current.getDefinition().getListLayer(layersVisited))
            list.add(g.getKey());
        if (!list.isEmpty()) {
            PossibleEffectsEvent event = new PossibleEffectsEvent(model.playerToUser(currentPlayer), current.getName());
            for (GraphWeaponEffect w: list)
                event.addEffect(w.getName(), w.getEffectType());
            model.send(event);
        }
        else {
            model.send(new MVWeaponEndEvent(model.playerToUser(currentPlayer)));
            endUsage();
        }
    }

    @Override
    protected void endUsage() {
        super.endUsage();
        partialGraphLayer = -1;
        layersVisitedPartial = 0;
        currentLayer = null;
        weaponEffect = null;
    }

    @Override
    public void dispatch(ChosenEffectEvent message) {

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
        partialGraphLayer++;
        handlePartial(currentLayer.get(partialGraphLayer).getKey());
    }

    private void handlePartial (PartialWeaponEffect partial){
        List<Targetable> targets = generateTargetSet(partial, currentPlayer);
        if (partial.getTargetSpecification().getTile())
            model.send(new PartialSelectionEvent(targetableToTile(targets), model.playerToUser(currentPlayer)));
        else
            model.send(new PartialSelectionEvent(model.playerToUser(currentPlayer), targetableToPlayer(targets)));
    }

    @Override
    public void dispatch(VCPartialEffectEvent message) {
        if (message.isSkip()){
            partialGraphLayer++;
            if (partialGraphLayer == currentLayer.size()) {
                model.send(new MVWeaponEndEvent(message.getSource()));
                endUsage();
            }
            else
                handlePartial(currentLayer.get(partialGraphLayer).getKey());
        }
        else {
            if (message.getTargetPlayer() != null)
                model.apply(model.playerToUser(currentPlayer),
                        new ArrayList<>(Arrays.asList(model.userToPlayer(message.getTargetPlayer()))),
                        currentLayer.get(partialGraphLayer).getKey());
            else if (message.getTargetTile() != null) {
                List<Player> targets = new ArrayList<>();
                for (Targetable t : model.getTile(message.getTargetTile()).getPlayers())
                    targets.add((Player) t);
                model.apply(model.playerToUser(currentPlayer), targets, currentLayer.get(partialGraphLayer).getKey());
            }

            layersVisitedPartial++;
            currentLayer = weaponEffect.getEffectGraph().getListLayer(layersVisitedPartial);
            if (currentLayer.isEmpty())
                nextWeaponEffect();
            else {
                partialGraphLayer = 0;
                handlePartial(currentLayer.get(partialGraphLayer).getKey());
            }
        }
    }

    @Override
    public void dispatch(VCSelectionEvent message) {
        List<Player> targets = new ArrayList<>();
        for (String s : message.getSelectedPlayers())
            targets.add(model.userToPlayer(s));
        model.usablePowerUps("onAttack", true, currentPlayer);
        model.apply(model.playerToUser(currentPlayer), targets, currentLayer.get(layersVisitedPartial).getKey());
        nextWeaponEffect();
    }

    @Override
    public void dispatch(VCWeaponEndEvent message) {
        endUsage();
        layersVisitedPartial = 0;
        currentLayer = null;
    }

    private List<Point> targetableToTile (List<Targetable> targetables){
        List<Point> points = new ArrayList<>();
        for (Targetable t : targetables)
            points.add(t.getPosition());
        return points;
    }

    private List<String> targetableToPlayer (List<Targetable> targetables){
        List<String> players = new ArrayList<>();
        //for (Targetable t : targetables)
        return players;

    }

}