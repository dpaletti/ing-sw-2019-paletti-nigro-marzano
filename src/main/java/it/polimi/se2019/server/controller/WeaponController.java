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

/**
 * This class handles all the interaction with the view concerning weapon usage.
 * See {@link it.polimi.se2019.server.controller.CardController}.
 */

public class WeaponController extends CardController {

    public WeaponController(Server server, int roomNumber, Game model) {
        super(model, server, roomNumber);
        currentPlayer = model.getPlayers().get(0);
    }

    public WeaponController(Game model) {
        super(model);
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
        } catch (UnsupportedOperationException e) {
            //ignore events that this controller does not support
            Log.fine("WeaponController ignored " + JsonHandler.serialize(message));
        }
    }

    /**
     * This method handles the start of the usage of a weapon and sends the player a list of effects they can use.
     * @param message
     */
    @Override
    public void dispatch(ChosenWeaponEvent message) {
        current = model.nameToWeapon(message.getWeapon());
        currentPlayer = model.userToPlayer(message.getSource());
        nextWeaponEffect(true);
    }

    /**
     * Sets all the indexes back when weapon usage is over.
     * @param isWeapon a boolean that defines whether a card is a weapon or a power up.
     */
    @Override
    protected void endUsage(boolean isWeapon) {
        super.endUsage(true);
        partialGraphLayer = -1;
        layersVisitedPartial = 0;
        currentLayer = null;
        weaponEffect = null;
        model.getTurnMemory().end();
    }

    /**
     * Sends the user the possible targets of their chosen effect.
     * @param message
     */
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

    /**
     * Causes damage to the chosen targets.
     * @param message
     */
    @Override
    public void dispatch(VCPartialEffectEvent message) {try {
        if ((partialGraphLayer == -1) || (!message.isWeapon() && !message.isSkip()))
            return;
        PartialWeaponEffect currentPartial = currentLayer.get(partialGraphLayer).getKey();

        for (String s : previousTargets)
            disablePowerUps(s, "onDamage");
        previousTargets.clear();

        if (message.isSkip()) {
            partialGraphLayer++;
            if (partialGraphLayer == currentLayer.size())
                nextWeaponEffect(message.isWeapon());
            else
                handlePartial(currentLayer.get(partialGraphLayer).getKey(), true);
        } else { //Is not skip
            if (message.getTargetPlayer() != null) { //The weapon has a player targetset

                List<Targetable> targetables = new ArrayList<>(Arrays.asList(model.userToPlayer(message.getTargetPlayer())));
                String username = model.playerToUser(currentPlayer);
                Player target = model.userToPlayer(message.getTargetPlayer());

                model.apply(username,
                        new ArrayList<>(Arrays.asList(target)),
                        currentPartial);

                usablePowerUps(new ArrayList<>(Arrays.asList(message.getTargetPlayer())));

                previousTargets.add(message.getTargetPlayer());
                model.getTurnMemory().hit(currentLayer.get(partialGraphLayer).getKey().getName(),
                        targetables,
                        targetables.get(0));

            } else if (message.getTargetTile() != null) { //The weapon is tile based
                List<Targetable> targetables = new ArrayList<>(Arrays.asList(model.getTile(message.getTargetTile())));
                List<Player> targets = new ArrayList<>();
                //Void list used in usablepowerups
                List<String> users = new ArrayList<>();


                //Check if every effect weapon has every action with isArea as true
                if (currentPartial.getActions().get(0).isArea()) {
                    if (currentPartial.getTargetSpecification().getRadiusBetween().getFirst() == -3 && currentPartial.getTargetSpecification().getRadiusBetween().getSecond() == -3) {
                        //I have to put every player in the room
                        for (Tile t : model.getGameMap().getRoom(model.getTile(message.getTargetTile()).getColour()))
                            targets.addAll(getPlayerOnTile(t));
                    } else
                        targets = getPlayerOnTile(model.getTile(message.getTargetTile()));

                } else
                    targets.add((Player) model.getTile(message.getTargetTile()).getPlayers().get(0));

                for (Player p : targets)
                    users.add(model.playerToUser(p));


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
            } else {
                partialGraphLayer = 0;
                handlePartial(currentLayer.get(partialGraphLayer).getKey(), true);
            }
        }
    }catch (IndexOutOfBoundsException e){
        Log.fine("Empty targetset");
    }
    }


    /**
     * activates power ups that can be used while attacking for the attacker and those that can be used when being damaged for the targets.
     * @param targets
     */
    private void usablePowerUps(List<String> targets) {
        model.usablePowerUps("OnAttack",true,currentPlayer);
        for (String s: targets)
            model.usablePowerUps("onDamage",false,model.userToPlayer(s));
    }

    private List<Player> getPlayerOnTile(Tile t){
        List<Player> players=new ArrayList<>();
        for (Targetable targetable: t.getPlayers()){
            players.add((Player)targetable);
        }
        return players;
    }

}