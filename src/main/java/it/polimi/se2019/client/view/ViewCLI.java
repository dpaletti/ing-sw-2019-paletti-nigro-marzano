package it.polimi.se2019.client.view;


import it.polimi.se2019.client.network.Client;
import it.polimi.se2019.commons.mv_events.*;
import it.polimi.se2019.commons.utility.Log;
import it.polimi.se2019.commons.utility.Point;
import it.polimi.se2019.commons.vc_events.*;

import java.util.*;


public class ViewCLI extends View {
    private Scanner in = new Scanner(System.in);
    private List<String> configs = new ArrayList<>();
    private List<MockPlayer> players = new ArrayList<>();
    private String currentlyShownFigure;
    private String currentWeapon;
    private String currentPowerUp;


    public ViewCLI(Client client){
        super(client);
    }


    /**
     * This method ignores the events that are not dispatched in this controller.
     * @param message Any message arriving from the view.
     */
    @Override
    public void update(MVEvent message) {
        try {
            if (!(message instanceof TimerEvent)){
                Log.fine("Received: " + message);
            }
            message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.severe("Unsupported event in view");
            //throw new UnsupportedOperationException("Error: " + e.getMessage(), e);
        }
    }

    //-------------------Match Making--------------------------------------

    @Override
    public void matchMaking(List<String> usernames, List<String> configs) {
        Log.info("Players in match-making: " + usernames);
        this.configs = configs;
    }

    @Override
    public void addPlayer(String username, int missing) {
        Log.info(username + " joined.");
    }

    @Override
    public void dispatch(UsernameDeletionEvent message) {
        Log.info(message.getUsername() + " left the game.");
    }

    @Override
    public void dispatch(PausedPlayerEvent message) {
        Log.info(message.getPausedPlayer() + " was paused.");
    }

    @Override
    public void dispatch(UnpausedPlayerEvent message) {
        Log.info(message.getUnpausedPlayer() + " is no longer paused.");
    }

    //-----------------------------Set Up------------------------------------------

    /**
     * When starting the match, all users are notified of the set up of the match.
     * @param message
     */
    @Override
    public void dispatch(MatchConfigurationEvent message) {
        String config;
        boolean isFinalFrenzy = false;
        int skulls;
        Log.info("The available map configurations are: " + configs);
        Log.info("Choose a map configuration: ");
        config = in.nextLine();
        Log.info("Do you want to play in final frenzy mode? [Y/N]");
        String finalFrenzy = in.nextLine();
        if (finalFrenzy.contains("y"))
            isFinalFrenzy = true;
        Log.info("How many rounds do you want to play? Choose a number between 5 and 8.");
        skulls = Integer.parseInt(in.nextLine());

        notify(new VcMatchConfigurationEvent(client.getUsername(), skulls, isFinalFrenzy, config));
    }

    /**
     * This method allows the user to decide where they want to spawn by discarding the correct power up.
     * @param message
     */
    @Override
    public void dispatch(SetUpEvent message){
        for (String user : message.getUserToColour().keySet())
            players.add(new MockPlayer(user, message.getUserToColour().get(user).toLowerCase()));

        currentlyShownFigure = getPlayerOnUsername(client.getUsername()).getPlayerColor();

        Log.info("Your figure for this match has colour " + currentlyShownFigure);

        Log.info("This match has the following set up: ");
        Log.info("map : " + message.getLeftConfig() + " "+ message.getRightConfig());
        Log.info("skulls : " + message.getSkulls());
        Log.info("final frenzy : " + message.isFrenzy());
        Log.info("The loot cards and their positions are the following :" + message.getLootCards());
        Log.info("The weapons and their weapon spots are the following : " + message.getWeaponSpots());
    }

    //----------------------------- turn ------------------------------------------

    /**
     * Notifies the user when their turn (which is the first one of the match) is starting.
     * @param message
     */
    @Override
    public void dispatch(StartFirstTurnEvent message) {
        String discardedPowerUp;
        String powerUpToKeep = "";
        Log.info("Your turn is starting.");
        Log.info("Choose a power up to discard and spawn between " + message.getFirstPowerUpName() + " and "+ message.getSecondPowerUpName());
        discardedPowerUp = in.nextLine();
        if (discardedPowerUp.equalsIgnoreCase(message.getFirstPowerUpName()))
            powerUpToKeep = message.getSecondPowerUpName();
        else if (discardedPowerUp.equalsIgnoreCase(message.getSecondPowerUpName()))
            powerUpToKeep = message.getFirstPowerUpName();
        Log.info("you're keeping " + powerUpToKeep);
        notify(new SpawnEvent(client.getUsername(), getColour(discardedPowerUp), powerUpToKeep));
    }

    /**
     * When a turn starts, it communicates the actions they can perform and asks them which one they would like to use.
     * @param message
     */
    @Override
    public void dispatch(TurnEvent message) {
        String combo;
        Log.info("Pick a combo to play or write 'endturn' to finish playing: " + message.getCombos());
        combo = in.nextLine();
        if (combo.equalsIgnoreCase("endturn")){
            notify(new VCEndOfTurnEvent(client.getUsername()));
            return;
        }
        notify(new ChosenComboEvent(client.getUsername(), combo));
    }

    /**
     * When the user wants to move, a set of possible tiles is shown and they can select where they would like to move to.
     * All other users are notified of this movement.
     * @param message
     */
    @Override
    public void dispatch(AllowedMovementsEvent message) {
        int x;
        int y;
        Log.info("You can move to one of the following tiles : " + message.getAllowedPositions());
        Log.info("Select the x you want to move to : ");
        x = Integer.parseInt(in.nextLine());
        Log.info("Select the y you want to move to : ");
        y = Integer.parseInt(in.nextLine());
        notify(new VCMoveEvent(client.getUsername(), new Point(x, y), false, client.getUsername()));
    }

    /**
     * All the usable weapons are shown to the user.
     * @param message
     */
    @Override
    public void dispatch(AllowedWeaponsEvent message) {
        String chosenWeapon;
        Log.info("You can use the following weapons to shoot, pick one : " + message.getWeapons());
        chosenWeapon = in.nextLine();
        currentWeapon = chosenWeapon;
        notify(new ChosenWeaponEvent(client.getUsername(), chosenWeapon));
    }

    /**
     * All the reloadable weapons are shown to the user.
     * @param message
     */
    @Override
    public void dispatch(ReloadableWeaponsEvent message) {
        List<String> chosenWeapons = new ArrayList<>();
        List<String> choosableWeapons = new ArrayList<>(message.getPriceMap().keySet());
        String inserted;
        while(!choosableWeapons.isEmpty()) {
            Log.info("You can reload the following weapons paying the listed ammos, pick those you want to reload or write 'none': " + choosableWeapons);
            inserted = in.nextLine();
            if (inserted.equalsIgnoreCase("none"))
                break;
            chosenWeapons.add(inserted);
            choosableWeapons.remove(inserted);
        }
        notify(new ReloadEvent(client.getUsername(), chosenWeapons));
    }

    /**
     * When a player wants to grab, this method allows them to grab the grabbable on the tile.
     * @param message
     */
    @Override
    public void dispatch(GrabbablesEvent message) {
        String grabbed;
        Log.info("Pick a card to grab: ");
        grabbed = in.nextLine();
        notify(new GrabEvent(client.getUsername(), grabbed));
    }

    /**
     * Notifies all users when someone moves.
     * @param message
     */
    @Override
    public void dispatch(MVMoveEvent message) {
        if (!message.getUsername().equals(client.getUsername()))
            Log.info("Player " + message.getUsername() + " moved to tile in position" + message.getFinalPosition());
    }

    @Override
    public void dispatch(BoardRefreshEvent message) {
        Log.info("This turn is over. The loot cards and their positions are the following : " + message.getLootCards());
        Log.info("The weapons and their weapon spots are the following : " + message.getWeaponSpots());
    }

    @Override
    public void dispatch(MVEndOfTurnEvent message) {
        Log.info(message.getPlayerEnding() + "'s turn has just ended, " + message.getPlayerStarting() + " is now playing.");
    }

    @Override
    public void dispatch(GrabbedLootCardEvent message) {
        Log.info(message.getGrabbedLootCard() + " was grabbed.");
    }

    @Override
    public void dispatch(MVRespawnEvent message) {
        String discarded;
        Log.info("Pick a power up to spawn : ");
        discarded = in.nextLine();
        notify(new SpawnEvent(client.getUsername(), getColour(discarded)));
    }

    /**
     * Notifies all users when a player dies.
     * @param message
     */
    @Override
    public void dispatch(MVDeathEvent message) {
        String overkill = "shot";
        if (message.isOverkill())
            overkill = "overkilled";
        Log.info(message.getDead() + " was " + overkill + "to death by " + message.getKiller());
        if (message.isMatchOver()) {
            Log.info("Match is over.");
            notify(new CalculatePointsEvent(message.getDead()));
        }
    }

    @Override
    public void dispatch(UpdateHpEvent message) {
        Log.info(message.getAttacked() + " received one damage from " + message.getAttacker());
    }

    @Override
    public void dispatch(FinalFrenzyStartingEvent message) {
        Log.info("Final Frenzy turn is starting.");
    }

    /**
     * Allows the user to use a power up.
     * @param message
     */
    @Override
    public void dispatch(UsablePowerUpEvent message) {
        String powerUp;
        Log.info("You can use the following PowerUps: " + message.getUsablePowerUp());
        Log.info("Do you want to use them? [Select one of the power ups you own or reply 'no']");
        powerUp = in.nextLine();
        if (!powerUp.equalsIgnoreCase("no")) {
            currentPowerUp = powerUp;
            notify(new PowerUpUsageEvent(client.getUsername(), powerUp));
        }
    }

    @Override
    public void dispatch(MVChooseAmmoToPayEvent message) {
        String ammo;
        Log.info("Pick one of the following ammos to pay : " + message.getAvailableAmmos());
        ammo = in.nextLine();
        notify(new VCChooseAmmoToPayEvent(client.getUsername(), getColour(ammo)));
    }

    @Override
    public void dispatch(DrawnPowerUpEvent message) {
        Log.info("You drew a " + message.getDrawn() + " power up.");
    }

    /**
     * Allows to choose the effect of the card that will be used.
     * @param message
     */

    @Override
    public void dispatch(PossibleEffectsEvent message) {
        String card = "weapon";
        String effect;
        if (!message.isWeapon())
            card = "power up";
        Log.info("Your " + card + " has the following effects, which one would you like to use?");
        effect = in.nextLine();
        if (message.isWeapon())
            notify(new ChosenEffectEvent(client.getUsername(), effect, currentWeapon));
        else
            notify(new ChosenEffectPowerUpEvent(client.getUsername(), effect, currentPowerUp));
    }

    @Override
    public void dispatch(PartialSelectionEvent message) {
        String chosenTarget;
        int x;
        int y;
        if (!message.getTargetPlayers().isEmpty()) {
            Log.info("Choose a player to target : " + message.getTargetPlayers());
            chosenTarget = in.nextLine();
            notify(new VCPartialEffectEvent(client.getUsername(), chosenTarget, message.isWeapon()));
        }
        else {
            Log.info("Choose the x of tile to target : " + message.getTargetTiles());
            x = Integer.parseInt(in.nextLine());
            Log.info("Choose the y of tile to target : " + message.getTargetTiles());
            y = Integer.parseInt(in.nextLine());
            notify(new VCPartialEffectEvent(client.getUsername(), new Point (x, y), message.isWeapon()));
        }
    }

    @Override
    public void dispatch(MVCardEndEvent message) {
        String current = currentWeapon;
        if (currentWeapon == null)
            current = currentPowerUp;
        Log.info( "The usage of " + current + " is over.");
        notify(new VCCardEndEvent(client.getUsername(), message.isWeapon()));
    }

    @Override
    public void dispatch(FinanceUpdateEvent message) {
        Log.info(message.getUsername() + "now has the following ammos : " + message.getUpdatedAmmos());
    }

    @Override
    public void dispatch(UpdateMarkEvent message) {
        Log.info("Player " + message.getMarked() + "was marked by " + message.getMarker());
    }

    @Override
    public void dispatch(WeaponToLeaveEvent message) {
        String weaponToLeave;
        Log.info("Choose a weapon to leave : ");
        weaponToLeave = in.nextLine();
        notify(new DiscardedWeaponEvent(client.getUsername(), weaponToLeave));
    }

    @Override
    public void dispatch(PowerUpToLeaveEvent message) {
        String powerUpToLeave;
        Log.info("Choose a power up to leave: ");
        powerUpToLeave = in.nextLine();
        notify(new DiscardedPowerUpEvent(client.getUsername(), powerUpToLeave));
    }

    /**
     * Allows the user to sell the needed power ups to pay for actions.
     * @param message
     */
    @Override
    public void dispatch(MVSellPowerUpEvent message) {
        List<String> soldPowerUps = new ArrayList<>();
        List<String> powerUps = message.getPowerUpsToSell();
        Map<String, Integer> missing = message.getColoursToMissing();
        for (String s : missing.keySet()) {
            for (int i = 0; i < missing.get(s); i++) {
                Log.info("You appear to be missing " + i + " " + s + " ammos.");
                Log.info("Sell a power up : " + getPowerUpsOfColour(s, missing.keySet()));
                soldPowerUps.add(in.nextLine());
                powerUps.remove(s);
            }
        }

        notify(new VCSellPowerUpEvent(client.getUsername(), soldPowerUps));
    }

    @Override
    public void dispatch(EndOfMatchEvent message) {
        Log.info("The match is over, see how you did: " + message.getFinalPoints());
    }

    @Override
    public void dispatch(UpdatePointsEvent message) {
        Log.info("Player " + message.getUsername() + " has gained " + message.getPoints() + " points!");
    }

    //------------------useful methods ------------------------------

    private MockPlayer getPlayerOnUsername(String username){
        for(MockPlayer m: players){
            if(m.getUsername().equalsIgnoreCase(username))
                return m;
        }
        throw new IllegalArgumentException("Could not find any player with username: " + username);
    }

    private String getColour (String toParse){
        if (toParse.contains("Yellow"))
            return "Yellow";
        if (toParse.contains("Blue"))
            return "Blue";
        if (toParse.contains("Red"))
            return "Red";
        else
            return "";
    }

    private List<String> getPowerUpsOfColour (String colour, Set<String> powerUps){
        List<String> toReturn = new ArrayList<>();
        for (String s : powerUps){
            if (getColour(s).equalsIgnoreCase(colour))
                toReturn.add(s);
        }
        return toReturn;
    }
}
