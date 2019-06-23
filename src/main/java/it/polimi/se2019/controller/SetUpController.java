package it.polimi.se2019.controller;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.FinalConfigurationEvent;
import it.polimi.se2019.model.mv_events.SetUpEvent;
import it.polimi.se2019.network.Server;
import it.polimi.se2019.utility.BiSet;
import it.polimi.se2019.utility.Log;
import it.polimi.se2019.utility.Pair;
import it.polimi.se2019.utility.Point;
import it.polimi.se2019.view.VCEvent;
import it.polimi.se2019.view.vc_events.VcMatchConfigurationEvent;

import java.util.*;

public class SetUpController extends Controller {
    private List<Integer> skulls = new ArrayList<>();
    private List<String> configs = new ArrayList<>();
    private List<Boolean> isFinalFrenzy = new ArrayList<>();
    private int counter = 0;
    private Random random = new Random();

    public SetUpController(Game model, Server server, int roomNumber) {
        super(model, server, roomNumber);
        startTimer(server.getMatchSetupTimer());
    }

    @Override
    public void update(VCEvent message)
    {
        try {
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //this is the only controller registered on matchMaking thus it cannot receive unsupported events
            Log.severe("Received unsupported event " + message);
            throw new UnsupportedOperationException("SetUpController: " + e.getMessage(), e);
        }
    }

    @Override
    public void dispatch(VcMatchConfigurationEvent message) {
        skulls.add(message.getSkulls());
        configs.add(message.getConf());
        isFinalFrenzy.add(message.isFrenzy());
        counter++;
        if (counter==5) {
            endTimer();
        }

    }

    @Override
    protected void endTimer() {
        super.endTimer();
        int skull;
        String config;
        boolean finalFrenzy;

        if (counter==0){
            skull = 8;
            config = "Large";
            finalFrenzy = true;
        }
        else {
            skull = mostVoted(skulls);
            config = mostVoted(configs);
            finalFrenzy = mostVoted(isFinalFrenzy);
        }
        model.send(new FinalConfigurationEvent("*", skull, config, finalFrenzy));

        model.setGameMap(new GameMap("Large"));
        model.send(new SetUpEvent("*", assignFigureToUser(), weaponSpotsSetUp(), lootTilesSetUp()));

        new MatchController(model, server, model.getUsernames(), getRoomNumber());
        new TurnController(model, server, getRoomNumber());
        new WeaponController(server, getRoomNumber(), model);
        server.removeController(this, getRoomNumber());
    }

    private Map<String, RoomColour> weaponSpotsSetUp (){
        Map<String, RoomColour> weaponSpots= new HashMap<>();
        Grabbable drawnGrabbable;

        for (Tile t: model.getGameMap().getSpawnTiles()){
            for (int i=0; i<3; i++){
                drawnGrabbable= (Weapon)model.getWeaponDeck().draw();
                t.add(drawnGrabbable);
                weaponSpots.put(drawnGrabbable.getName(), t.getColour());
            }
        }
        return weaponSpots;
    }

    private Map<Point, String> lootTilesSetUp(){
        Map<Point, String> lootCards= new HashMap<>();
        Grabbable drawnGrabbable;

        for (Tile t: model.getGameMap().getLootTiles()){
            drawnGrabbable= (LootCard)model.getLootDeck().draw();
            t.add(drawnGrabbable);
            lootCards.put(t.getPosition(), drawnGrabbable.getName());
        }
        return lootCards;
    }

    private Map<String, FigureColour> assignFigureToUser (){
        HashMap<String, FigureColour> userToColour= new HashMap<>();
        int colourCounter=0;
        BiSet<FigureColour, String> lookup= new BiSet<>();
        List<Player> players= new ArrayList<>();
        for (String userCounter: model.getUsernames()){
            lookup.add(new Pair<>(FigureColour.values()[colourCounter], userCounter));
            players.add(new Player(new Figure(FigureColour.values()[colourCounter]), model));
            userToColour.put(userCounter, FigureColour.values()[colourCounter]);
            colourCounter++;
        }
        model.setUserLookup(new BiSet<>(lookup));
        model.setPlayers(new ArrayList<>(players));
        return userToColour;
    }

    private <T> T mostVoted (List<T> objects){
        Map<T, Integer> votes= new HashMap<>();
        int maximum = -1;
        List<T> mostVoted= new ArrayList<>();
        for (T o: objects)
            votes.put(o, Collections.frequency(objects, o));
        for (Map.Entry<T, Integer> e: votes.entrySet()){
            if (maximum == -1 || e.getValue()>maximum){
                mostVoted.clear();
                mostVoted.add(e.getKey());
                maximum = e.getValue();
            }
            else
                mostVoted.add(e.getKey());
        }
        return mostVoted.get(random.nextInt(mostVoted.size()));
    }
}
