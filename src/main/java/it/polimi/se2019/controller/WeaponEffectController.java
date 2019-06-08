package it.polimi.se2019.controller;

import it.polimi.se2019.model.*;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.Action;
import it.polimi.se2019.view.vc_events.ChosenEffectEvent;
import it.polimi.se2019.view.vc_events.ConflictResolutionEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeaponEffectController extends WeaponController{

    public WeaponEffectController(Server server, int roomNumber, Game model) {
        super(server, roomNumber, model);
    }

//TODO

    private void selectNode (Targetable selectedTargets, Player player) {
        List<GraphNode<PartialWeaponEffect>> collidingEffects= new ArrayList<>();
        List<ArrayList<Action>> actions= new ArrayList<>();
        List<ArrayList<String>> finalTargets = new ArrayList<>();
        List<String> targets= new ArrayList<>();
        for (GraphNode<PartialWeaponEffect> p: currentLayer){
            if(generateTargetSet(p.getKey(),player).contains(selectedTargets))
                collidingEffects.add(p);
        }
        if (collidingEffects.isEmpty())
            throw new IllegalArgumentException("Target not found in generateTargetset");
        if(collidingEffects.size()==1){
            model.apply(inferTargetSet
                    (collidingEffects.get(0).getKey(),selectedTargets,generateTargetSet
                            (collidingEffects.get(0).getKey(),player)),
                    collidingEffects.get(0).getKey().getActions());
            nextWeaponEffect();
            return;
        }

        for (GraphNode<PartialWeaponEffect> g: collidingEffects){
            actions.add(new ArrayList<>(g.getKey().getActions()));
            for (Player p: inferTargetSet(g.getKey(), selectedTargets, generateTargetSet(g.getKey(), player))){
                targets.add(model.playerToUser(p));
            }
            finalTargets.add(new ArrayList<>(targets));
            targets.clear();
        }
        model.sendPartialEffectConflict(model.playerToUser(player), actions, finalTargets);

    }

    private List<Player> inferTargetSet(PartialWeaponEffect partialWeaponEffect,Targetable targetable,List<Targetable> targetables){
        List<Player> players= new ArrayList<>();
        if (partialWeaponEffect.getTargetSpecification().getTile()){
            if (isArea(partialWeaponEffect)){
               for (Targetable t: targetables){
                   for (Figure figure:model.getGameMap().getMap().get(t.getPosition()).getFigures())
                       players.add(figure.getPlayer());
               }
            }else {
                for (Figure figure: model.getGameMap().getMap().get(targetable.getPosition()).getFigures())
                    players.add(figure.getPlayer());
            }
            return players;
        }
        players.add((Player)targetable);
        return players;
    }

    private boolean isArea(PartialWeaponEffect partialWeaponEffect) {
        boolean set = false;
        boolean isArea = false;
        for (Action action : partialWeaponEffect.getActions()) {
            if (action.isArea()) {
                if (set) {
                    throw new IllegalStateException("Error in json files, found two actions with different area value");
                }
                isArea = true;
            } else {
                if (isArea) {
                    throw new IllegalStateException("Error in json files, found two actions with different area value");
                }
                set = true;
            }
        }
        return isArea;
    }

    @Override
    public void dispatch(ConflictResolutionEvent message) {
        List<Player> targets= new ArrayList<>();
        for (String s: message.getSelectedPlayers())
            targets.add(model.userToPlayer(s));
        model.apply(targets, message.getSelectedActions());
        nextWeaponEffect();
    }
}
