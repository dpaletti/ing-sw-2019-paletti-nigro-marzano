package it.polimi.se2019.server.controller;

import it.polimi.se2019.client.view.VCEvent;
import it.polimi.se2019.commons.utility.JsonHandler;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.vc_events.ChosenEffectEvent;
import it.polimi.se2019.commons.vc_events.ChosenWeaponEvent;
import it.polimi.se2019.commons.vc_events.VCPartialEffectEvent;
import it.polimi.se2019.server.model.*;
import it.polimi.se2019.server.network.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeaponController extends CardController {

    public WeaponController(Server server, int roomNumber, Game model) {
        super(model, server, roomNumber);
        currentPlayer = model.getPlayers().get(0);
    }

    public WeaponController(Game model) {
        super(model);
    }

    @Override
    public void update(VCEvent message) {
        if(disabled)
            return;
        try {
            message.handle(this);
        } catch (UnsupportedOperationException e) {
            //ignore events that this controller does not support
            Log.fine("WeaponController ignored " + JsonHandler.serialize(message));
        }
    }


    @Override
    public void dispatch(ChosenWeaponEvent message) {
        current = model.nameToWeapon(message.getWeapon());
        currentPlayer = model.userToPlayer(message.getSource());
        nextWeaponEffect(true);
    }

    @Override
    protected void endUsage(boolean isWeapon) {
        super.endUsage(true);
        partialGraphLayer = -1;
        layersVisitedPartial = 0;
        currentLayer = null;
        weaponEffect = null;
        model.getTurnMemory().end();
    }

    @Override
    public void dispatch(ChosenEffectEvent message) {
        for (GraphNode<GraphWeaponEffect> w : model.nameToWeapon(message.getWeapon()).getDefinition()) {
            if (w.getKey().getName().equals(message.getEffectName())) {
                weaponEffect = w.getKey();
                break;
            }
        }
        if (weaponEffect == null)
            throw new NullPointerException("Could not find " + message.getEffectName() + " in " + message.getWeapon());

        handleEffect(true);
    }

    @Override
    public void dispatch(VCPartialEffectEvent message) {
        if (!message.isWeapon())
            return;
        for (String s : previousTargets)
            disablePowerUps(s, "onDamage");
        previousTargets.clear();
        if (message.isSkip()) {
            partialGraphLayer++;
            if (partialGraphLayer == currentLayer.size())
                endUsage(true);
            else
                handlePartial(currentLayer.get(partialGraphLayer).getKey(),true);
        } else {
            if (message.getTargetPlayer() != null) {
                List<Targetable> targetables = new ArrayList<>(Arrays.asList(model.userToPlayer(message.getTargetPlayer())));
                String username=model.playerToUser(currentPlayer);
                Player target=model.userToPlayer(message.getTargetPlayer());
                PartialWeaponEffect partialWeaponEffect= currentLayer.get(partialGraphLayer).getKey();
                model.apply(username,
                        new ArrayList<>(Arrays.asList(target)),
                        partialWeaponEffect);
                usablePowerUps(new ArrayList<>(Arrays.asList(message.getTargetPlayer())));
                previousTargets.add(message.getTargetPlayer());
                model.getTurnMemory().hit(currentLayer.get(partialGraphLayer).getKey().getName(),
                        targetables,
                        targetables.get(0)); //not sure this is needed
            } else if (message.getTargetTile() != null) {
                PartialWeaponEffect currentPartial= currentLayer.get(partialGraphLayer).getKey();
                List<Targetable> targetables = new ArrayList<>(Arrays.asList(model.getTile(message.getTargetTile())));
                List<Player> targets = new ArrayList<>();
                List<String> users = new ArrayList<>();
                //Check if every effect weapon has every action with isArea as true
                if (currentPartial.getActions().get(0).isArea()) {
                    if (currentPartial.getTargetSpecification().getRadiusBetween().getFirst() == -3 && currentPartial.getTargetSpecification().getRadiusBetween().getSecond() == -3) {
                        //I have to put every player in the room
                        for (Tile t : model.getGameMap().getRoom(model.getTile(message.getTargetTile()).getColour()))
                            targets.addAll(getPlayerOnTile(t));
                    } else
                        targets = getPlayerOnTile(model.getTile(message.getTargetTile()));
                }else
                    targets.add((Player)model.getTile(message.getTargetTile()).getPlayers().get(0));
                targets.remove(model.userToPlayer(message.getSource()));
                model.apply(model.playerToUser(currentPlayer), targets, currentLayer.get(partialGraphLayer).getKey());
                usablePowerUps(users);
                previousTargets.addAll(users);
                model.getTurnMemory().hit(currentLayer.get(partialGraphLayer).getKey().getName(),
                        targetables,
                        model.getTile(message.getTargetTile()));
            }

            layersVisitedPartial++;
            currentLayer = weaponEffect.getEffectGraph().getListLayer(layersVisitedPartial);
            if (currentLayer.isEmpty()) {
                layersVisitedPartial = 0;
                nextWeaponEffect(true);
            }
            else {
                partialGraphLayer = 0;
                handlePartial(currentLayer.get(partialGraphLayer).getKey(),true);
            }
        }
    }

    /*@Override
    public void dispatch(VCCardEndEvent message) {
        if (message.isWeapon()) {
            currentPlayer.getWeaponByName(current.getName()).setLoaded(false);
            endUsage(true);
            layersVisitedPartial = 0;
            currentLayer = null;
        }
    }*/

    private void usablePowerUps(List<String> targets) {
        if (targets.size() == 1 && targets.get(0).equals(model.playerToUser(currentPlayer)))
            model.usablePowerUps("onAttack", true, currentPlayer);
        else {
            for (String s : targets)
                model.usablePowerUps("onDamage", false, model.userToPlayer(s));
        }
    }

    private List<Player> getPlayerOnTile(Tile t){
        List<Player> players=new ArrayList<>();
        for (Targetable targetable: t.getPlayers()){
            players.add((Player)targetable);
        }
        return players;
    }

}