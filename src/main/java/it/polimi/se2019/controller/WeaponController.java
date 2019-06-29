package it.polimi.se2019.controller;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.MVWeaponEndEvent;
import it.polimi.se2019.model.mv_events.PartialSelectionEvent;
import it.polimi.se2019.model.mv_events.PossibleEffectsEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.JsonHandler;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WeaponController extends CardController {
    private int partialGraphLayer = -1;
    private GraphWeaponEffect weaponEffect = null;
    private List<String> previousTargets = new ArrayList<>();
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
    public void dispatch(ChosenWeaponEvent message) {
        current = model.nameToWeapon(message.getWeapon());
        nextWeaponEffect();
    }

    private void nextWeaponEffect (){
        List<GraphWeaponEffect> list = new ArrayList<>();
        layersVisited = layersVisited + 1;
        for (GraphNode<GraphWeaponEffect> g: current.getDefinition().getListLayer(layersVisited)) {
            if (enoughAmmos(g.getKey()).isEmpty() || !enoughPowerUps(g.getKey()).isEmpty())
                list.add(g.getKey());
        }
        if (!list.isEmpty()) {
            PossibleEffectsEvent event = new PossibleEffectsEvent(model.playerToUser(currentPlayer), current.getName(), true);
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
        model.getTurnMemory().end();
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

        currentPlayer.useAmmos(weaponEffect.getPrice());
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
        for (String s : previousTargets)
            disablePowerUps(s, "onDamage");
        previousTargets.clear();
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
            if (message.getTargetPlayer() != null) {
                List<Targetable> targetables = new ArrayList<>(Arrays.asList(model.userToPlayer(message.getTargetPlayer())));
                model.apply(model.playerToUser(currentPlayer),
                        new ArrayList<>(Arrays.asList(model.userToPlayer(message.getTargetPlayer()))),
                        currentLayer.get(partialGraphLayer).getKey());
                usablePowerUps(new ArrayList<>(Arrays.asList(message.getTargetPlayer())));
                previousTargets.add(message.getTargetPlayer());
                model.getTurnMemory().hit(currentLayer.get(partialGraphLayer).getKey().getName(),
                        targetables,
                        targetables.get(0)); //not sure this is needed
            }
            else if (message.getTargetTile() != null) {
                List<Targetable> targetables = new ArrayList<>(Arrays.asList(model.getTile(message.getTargetTile())));
                List<Player> targets = new ArrayList<>();
                List<String> users = new ArrayList<>();
                for (Targetable t : model.getTile(message.getTargetTile()).getPlayers()) {
                    targets.add((Player) t);
                    users.add(model.playerToUser((Player)t));
                }
                model.apply(model.playerToUser(currentPlayer), targets, currentLayer.get(partialGraphLayer).getKey());
                usablePowerUps(users);
                previousTargets.addAll(users);
                model.getTurnMemory().hit(currentLayer.get(partialGraphLayer).getKey().getName(),
                        targetables,
                        model.getTile(message.getTargetTile()));
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

    private void usablePowerUps(List<String> targets){
        if (targets.size() == 1 && targets.get(0).equals(model.playerToUser(currentPlayer)))
            model.usablePowerUps("onAttack", true, currentPlayer);
        else {
            for (String s : targets)
                model.usablePowerUps("onDamage", false, model.userToPlayer(s));
        }
    }

    private List<Ammo> enoughAmmos (GraphWeaponEffect effect){
        List<Ammo> ownedAmmos = new ArrayList<>(currentPlayer.getAmmo());
        List<Ammo> toReturn = new ArrayList<>();
        for (Ammo a : effect.getPrice()){
            if (!ownedAmmos.remove(a))
                toReturn.add(a);
        }
        return toReturn;
    }

    private List<Ammo> enoughPowerUps (GraphWeaponEffect effect){
        List<PowerUp> ownedPowerUps = new ArrayList<>(currentPlayer.getPowerUps());
        List<Ammo> missingAmmos = enoughAmmos(effect);
        List<Ammo> toReturn = new ArrayList<>();
        boolean flag = false;

        for (Ammo a : missingAmmos){
            for (PowerUp p : ownedPowerUps) {
                if (a.getColour().name().equalsIgnoreCase(p.getColour())) {
                    ownedPowerUps.remove(p);
                    toReturn.add(a);
                    flag = true;
                }
            }
            if (!flag)
                return Collections.emptyList();
            flag = false;
        }
        return toReturn;
    }

}