package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.utility.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Figure {
    private Tile tile;
    private FigureColour colour;
    private Player player;
    private String figureName;
    private Point position;


    public Point getPosition() {
        return position;
    }

    public FigureColour getColour() {
        return colour;
    }

    public Tile getTile() {
        return tile;
    }

    public void setColour(FigureColour colour) {
        this.colour = colour;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Player getPlayer() {
        return player;
    }

    public String getFigureName() {
        return figureName;
    }

    public void setFigureName(String figureName) {
        this.figureName = figureName;
    }

    public void setPlayer(Player player) { this.player = player; }

    public void damage(Figure target, Figure shooter){
        target.player.addTear(shooter.colour);
        target.player.updatePlayerDamage();
    }

    public void mark(Figure target, Figure shooter){
        int alreadyMaximumMarks=0;
        for(Tear marksCounter: target.player.getMarks()){
            if(marksCounter.getColour()==shooter.colour){
                alreadyMaximumMarks++;
            }
        }
        if(alreadyMaximumMarks<3) {
            target.player.addMark(shooter.colour);
        }
    }

    public void move(Direction direction){
        Point newPosition= new Point(tile.getPosition().getX(), tile.getPosition().getY());
        switch (direction){
            case EAST:
                newPosition.setX(newPosition.getX()+1);
                break;
            case WEST:
                newPosition.setX(newPosition.getX()-1);
                break;
            case NORTH:
                newPosition.setY(newPosition.getY()+1);
                break;
            case SOUTH:
                newPosition.setY(newPosition.getY()-1);
                break;
            case TELEPORT:
                TeleportEvent teleportEvent= new TeleportEvent(newPosition.getX(), newPosition.getY());
                Game.getInstance().sendMessage(teleportEvent); //sends player its position, asks where to move
                //TODO: VMEvent: set newPosition to the specified one
        }
        position=newPosition;
        //tile=GameMap.getMap().get(newPosition);
    }

    public void move(Figure target, Direction direction){
        Point newPosition= new Point(target.tile.getPosition().getX(), target.tile.getPosition().getY());
        switch (direction){
            case EAST:
                newPosition.setY(newPosition.getY()+1);
                break;
            case WEST:
                newPosition.setY(newPosition.getY()-1);
                break;
            case NORTH:
                newPosition.setX(newPosition.getX()+1);
                break;
            case SOUTH:
                newPosition.setX(newPosition.getX()-1);
                break;
                default:
                    break;
        }
        target.position=newPosition;
        //target.tile=GameMap.getMap().get(newPosition);
    } //TODO: move does not consider boundaries, to fix

    public void grab(){
        if (tile.getTileType()==TileType.LOOTTILE) { //when on a Loot Tile, adds grabbed ammo to usable ammo making sure the number of ammo of a colour does not exceed 3
            int ammoOfSelectedColour=0;
            for (Ammo lootCounter : tile.getLootCard().getAmmo()) {
                for (Ammo ammoCounter: player.getUsableAmmo()){
                    if (ammoCounter==lootCounter) {
                        ammoOfSelectedColour++;
                    }
                }
                if (ammoOfSelectedColour<3) {
                    player.getUsableAmmo().add(lootCounter);
                    player.getUnusableAmmo().remove(lootCounter);
                }
                ammoOfSelectedColour=0;
            }
        }
        else {  //when on a Spawn Tile, allows player to grab a weapon and leave one among those they previously grabbed if they already own 3
            Weapon selectedWeapon= null;
            Set<String> availableWeapons= new HashSet<>();
            availableWeapons.add(tile.getWeaponSpot().getFirstWeapon().getName());
            availableWeapons.add(tile.getWeaponSpot().getSecondWeapon().getName());
            availableWeapons.add(tile.getWeaponSpot().getThirdWeapon().getName());
            WeaponToGrabEvent weaponToGrabEvent=new WeaponToGrabEvent();
            weaponToGrabEvent.setAvailableWeapons(availableWeapons);
            Game.getInstance().sendMessage(weaponToGrabEvent);
            // TODO: vc_events: a Weapon is returned and assigned to selectedWeapon

            if (player.getFirstWeapon()==null){
                player.setFirstWeapon(selectedWeapon);
            }
            else if (player.getSecondWeapon()==null){
                player.setSecondWeapon(selectedWeapon);
            }

            else if (player.getThirdWeapon()==null){
                player.setThirdWeapon(selectedWeapon);
            }

            else {
                WeaponToLeaveEvent weaponToLeaveEvent=new WeaponToLeaveEvent();
                Set<String> weaponsOwned=new HashSet<>();
                weaponsOwned.add(player.getFirstWeapon().getName());
                weaponsOwned.add(player.getSecondWeapon().getName());
                weaponsOwned.add(player.getThirdWeapon().getName());
                weaponToLeaveEvent.setWeaponsOwned(weaponsOwned);
                Game.getInstance().sendMessage(weaponToLeaveEvent);
                //TODO: vc_events: a weapon is returned and selectedWeapon is assigned to free weapon slot
            }
        }
    }

    public Set<Pair<Effect, Set<Figure>>> generateTargetSet(GraphNode<Effect> node){
        Set<Pair<Effect, Set<Figure>>> targetSet=new HashSet<>();
        Pair <Effect, Set<Figure>> effectToTargets;

        Set<Player> players=new HashSet<>();
        Set<Figure> figures=new HashSet<>();
        players.addAll(Game.getInstance().getPlayers());
        for(Player playerCounter: players){
            figures.add(playerCounter.getFigure());
        }
        Set<Figure> seenFigures=visibilitySet();
        Figure chosenPreviousTarget=null;

        Set<Tile> seenTiles=tile.visibleTiles();
        for (Effect effectCounter: node.getNode()){
            Set<Figure> figuresInTargetSet=null;
            if(!effectCounter.getTargetSpecification().getTile()) { //target is a figure

                switch (effectCounter.getTargetSpecification().getVisible()) {
                    case -1:
                        figuresInTargetSet = figures;
                        break;
                    case 0:
                        figuresInTargetSet= targetSetUpdater(figures, seenFigures, 0);
                        break;
                    case 1:
                        figuresInTargetSet = seenFigures;
                        break;
                    case 2: //figures one of my previous targets can see
                        //TODO: simplify
                        ChooseAmongPreviousTargetsEvent chooseTargetEvent=new ChooseAmongPreviousTargetsEvent();
                        Set<String> previousTargetNames= new HashSet<> ();
                        Set<Figure> previousTargets=new HashSet<>();
                        int indexOfCurrentPlayer= Game.getInstance().getPlayers().indexOf(player);
                        previousTargets.addAll(Game.getInstance().getTurns().get(indexOfCurrentPlayer).getFirstTargetSet());
                        for(Figure figureCounter: previousTargets){
                            previousTargetNames.add(figureCounter.figureName);
                        }
                        chooseTargetEvent.setPreviousTargets(previousTargetNames); //no, previous target is already chosen
                        Game.getInstance().sendMessage(chooseTargetEvent);
                        //TODO: vc_events a target is returned and assigned to chosenPreviousTarget
                        Set<Figure> seenByTargetFigures=visibilitySet(chosenPreviousTarget);
                        figuresInTargetSet = seenByTargetFigures;
                        break;
                    default:
                        break;
                }
                if (effectCounter.getTargetSpecification().getDifferent().getFirst()){
                    figuresInTargetSet= targetSetUpdater(figuresInTargetSet, targetsOfSelectedEffect(effectCounter), 0);
                }
                else {
                    figuresInTargetSet= targetSetUpdater(figuresInTargetSet, targetsOfSelectedEffect(effectCounter), 1);

                }

                figuresInTargetSet= areaSelectionForFigures(figuresInTargetSet, effectCounter.getTargetSpecification().getRadiusBetween().getFirst(), effectCounter.getTargetSpecification().getRadiusBetween().getSecond());

            }
            else { //target is a tile
                Set<Tile> tilesInTargetSet=new HashSet<>();
                tilesInTargetSet.addAll(GameMap.getTiles());
                switch (effectCounter.getTargetSpecification().getVisible()){ //same as figures
                    case -1:
                        break;
                    case 0:
                        tilesInTargetSet= GameMap.getTiles();
                        tilesInTargetSet= tileSetUpdater(tilesInTargetSet, seenTiles, 0);
                        break;
                    case 1:
                        tilesInTargetSet= seenTiles;
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                if(effectCounter.getTargetSpecification().getDifferent().getFirst()){
                    tilesInTargetSet= tileSetUpdater(tilesInTargetSet, tilesOfSelectedEffect(effectCounter), 1);
                }
                else {
                    tilesInTargetSet= tileSetUpdater(tilesInTargetSet, tilesOfSelectedEffect(effectCounter), 1);
                }

                switch (effectCounter.getTargetSpecification().getEnlarge()){
                    case -1: //selects whole room
                        for(Tile sameRoomCounter: GameMap.getTiles()){
                            if(!tile.getColour().equals(sameRoomCounter.getColour())){
                                tilesInTargetSet.remove(sameRoomCounter);
                            }
                        }
                        break;
                    case -2: //TODO: enlarges in specified direction
                        break;
                        default: //Enlarge value specifies radius to enlarge area of action, must be >0
                            break;
                }
                if (effectCounter.getTargetSpecification().getArea()){ //Area weapon, targetset=figures on all selected tiles (player does not chose this)
                    //TODO: call a method that signals all figures in targetSet must be targeted by the effect or add flag to returned value
                }

                tilesInTargetSet= areaSelectionForTiles(tilesInTargetSet, effectCounter.getTargetSpecification().getRadiusBetween().getFirst(), effectCounter.getTargetSpecification().getRadiusBetween().getSecond());

                for (Tile tileCounter: tilesInTargetSet){ //coonverts tiles in figures to hit
                    figuresInTargetSet.addAll(tileCounter.getFigures());
                }
            }
            effectToTargets= new Pair<>(effectCounter, figuresInTargetSet);
            targetSet.add(effectToTargets);
        }
        return targetSet;
    }

    public Map<Figure, List<Effect>> generateWeaponEffect (GraphNode<Effect> node, Figure target){ return null;}

    public void reload (Weapon weapon){
        int enoughAmmoForReload=0;
        for (Ammo reloadCostCounter: weapon.getPrice()) {   //checks whether the reload price can be payed
            for (Ammo availableAmmoCounter: player.getUsableAmmo()){
                if(reloadCostCounter==availableAmmoCounter){
                    enoughAmmoForReload++;
                    break;
                }
            }

        }
        if(enoughAmmoForReload==weapon.getPrice().size()){  //in case player has enough ammo to pay for the reload
            weapon.setLoaded(true);
            Set<Ammo> ammo= player.getUsableAmmo();
            player.setUsableAmmo(ammo);
        }
        else{
            NotEnoughAmmoEvent notEnoughAmmoEvent=null;
            Game.getInstance().sendMessage(notEnoughAmmoEvent); //not enough ammo available to reload, want to use PowerUps?
            //TODO: VMEvent: no, end reload/yes, use selectedPowerUp to pay
            PowerUp selectedPowerUp=null;
            player.sellPowerUp(selectedPowerUp);
            reload(weapon);
        }
    }

    public void damage (SpawnTile target){
        if (target.getTileType()==TileType.SPAWNTILE){
            target.addTear(colour);
        }

    }

    private int findDistance (){
        return tile.getPosition().getX()+tile.getPosition().getY();
    }

    public Set<Figure> visibilitySet (){
        Set<Figure> visibleFigures=new HashSet<>();
        Point point= position;
        Tile pointToTile=null;
        for (Tile tileCounter: GameMap.getTiles()){
            if(tileCounter.getColour().equals(tile.getColour())){
                visibleFigures.addAll(tileCounter.getFigures());
            }
        }
        if (tile.getDoors().get(Direction.NORTH)!=null){
            point.setX(tile.position.getX());
            point.setY(tile.position.getY()+1);
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.getPosition().equals(point)){
                    pointToTile= tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if (tileCounter.colour.equals(pointToTile.colour)){
                    visibleFigures.addAll(tileCounter.getFigures());
                }
            }
        }
        if (tile.getDoors().get(Direction.SOUTH)){
            point.setX(tile.position.getX());
            point.setY(tile.position.getY()-1);
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.getPosition().equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if (tileCounter.colour.equals(pointToTile.colour)){
                    visibleFigures.addAll(tileCounter.getFigures());
                }
            }
        }
        if (tile.getDoors().get(Direction.EAST)){
            point.setX(tile.position.getX()+1);
            point.setY(tile.position.getY());
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.getPosition().equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if (tileCounter.colour.equals(pointToTile.colour)){
                    visibleFigures.addAll(tileCounter.getFigures());
                }
            }
        }
        if (tile.getDoors().get(Direction.WEST)){
            point.setX(tile.position.getX()-1);
            point.setY(tile.position.getY());
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.getPosition().equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if (tileCounter.colour.equals(pointToTile.colour)){
                    visibleFigures.addAll(tileCounter.getFigures());
                }
            }
        }
        return visibleFigures;
    }

    public Set<Figure> visibilitySet (Figure targetFigure){
        Set<Figure> visibleFigures=new HashSet<>();
        Point point= targetFigure.position;
        Tile pointToTile= null;
        for (Tile tileCounter: GameMap.getTiles()){
            if(tileCounter.getColour().equals(targetFigure.tile.getColour())){
                visibleFigures.addAll(tileCounter.getFigures());
            }
        }
        if (targetFigure.tile.getDoors().get(Direction.NORTH)) {
            point.setX(targetFigure.tile.position.getX());
            point.setY(targetFigure.tile.position.getY() + 1);
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.getPosition().equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if (tileCounter.colour.equals(pointToTile.colour)){
                    visibleFigures.addAll(tileCounter.getFigures());
                }
            }
        }
        if (targetFigure.tile.getDoors().get(Direction.SOUTH)){
            point.setX(targetFigure.tile.position.getX());
            point.setY(targetFigure.tile.position.getY()-1);
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.getPosition().equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if (tileCounter.colour.equals(pointToTile.colour)){
                    visibleFigures.addAll(tileCounter.getFigures());
                }
            }
        }
        if (targetFigure.tile.getDoors().get(Direction.EAST)){
            point.setX(targetFigure.tile.position.getX()+1);
            point.setY(targetFigure.tile.position.getY());
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.getPosition().equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if (tileCounter.colour.equals(pointToTile.colour)){
                    visibleFigures.addAll(tileCounter.getFigures());
                }
            }
        }
        if (targetFigure.tile.getDoors().get(Direction.WEST)){
            point.setX(targetFigure.tile.position.getX()-1);
            point.setY(targetFigure.tile.position.getY());
            for (Tile tileCounter: GameMap.getTiles()){
                if(tileCounter.getPosition().equals(point)){
                    pointToTile=tileCounter;
                    break;
                }
            }
            for (Tile tileCounter: GameMap.getTiles()){
                if (tileCounter.colour.equals(pointToTile.colour)){
                    visibleFigures.addAll(tileCounter.getFigures());
                }
            }
        }
        return visibleFigures;
    }

    public Set<Figure> targetsOfSelectedEffect(Effect effect){ //TODO: write method to calculate targets of previous actions
        Set<Figure> targetSet=new HashSet<>();
        return targetSet;
    }

    public Set<Tile> tilesOfSelectedEffect(Effect effect){ //TODO: write method to calculate tiles of previous actions
        Set<Tile> targetSet=new HashSet<>();
        return targetSet;
    }

    private Set <Figure> areaSelectionForFigures (Set<Figure> figuresInTargetSet, Integer innerRadius, Integer outerRadius){
        Set<Figure> targetSet=new HashSet<>();
        int value=0;
        if (innerRadius== 0 && outerRadius== 0) {
            targetSet.add(this);
            value=2;
        }
        else if (innerRadius == -2 && outerRadius == -2) {
            targetSet= visibilitySet();
            value=1;
        }
        else if (innerRadius == -3 && outerRadius == -3) {
            for (Figure figureCounter : figuresInTargetSet) {
                if (figureCounter.tile.getColour().equals(tile.getColour())) {
                    figuresInTargetSet.remove(figureCounter);
                }
            }
        }
        else if (outerRadius != -1 && innerRadius != -1) {
            for(Figure figureCounter: figuresInTargetSet){
                if (figureCounter.findDistance()<innerRadius||figureCounter.findDistance()>outerRadius){
                    figuresInTargetSet.remove(figureCounter);
                }
            }
        }

        targetSet= targetSetUpdater(figuresInTargetSet, targetSet, value);
        return targetSet;
    }

    private Set<Figure> targetSetUpdater (Set<Figure> originalTargetSet, Set<Figure> resultTargetSet, Integer value){
        switch (value){
            case 0:
                originalTargetSet.removeAll(resultTargetSet);
                break;
            case 1:
                for (Figure figureCounter: originalTargetSet){
                    if(!resultTargetSet.contains(figureCounter)){
                        originalTargetSet.remove(figureCounter);
                    }
                }
                break;
            case 2:
                originalTargetSet.clear();
                originalTargetSet.add(this);
                break;
                default:
                    break;
        }
        return originalTargetSet;
    }

    private Set<Tile> tileSetUpdater (Set<Tile> originalTargetSet, Set<Tile> resultTargetSet, Integer value){
        switch (value){
            case 0:
                originalTargetSet.removeAll(resultTargetSet);
                break;
            case 1:
                for (Tile tileCounter: originalTargetSet){
                    if(!resultTargetSet.contains(tileCounter)){
                        originalTargetSet.remove(tileCounter);
                    }
                }
                break;
            case 2:
                originalTargetSet.clear();
                originalTargetSet.add(this.tile);
                break;
                default:
                    break;
        }
        return originalTargetSet;
    }

    private Set <Tile> areaSelectionForTiles (Set<Tile> tilesInTargetSet, Integer innerRadius, Integer outerRadius){
        Set<Tile> targetSet=new HashSet<>();
        int value= 0;
        if (innerRadius== 0 && outerRadius== 0) {
            targetSet.add(this.tile);
            value=2;
        }
        else if (innerRadius == -2 && outerRadius == -2) {
            targetSet= tile.visibleTiles();
            value=1;
        }
        else if (innerRadius == -3 && outerRadius == -3) {
            for (Tile tileCounter : tilesInTargetSet) {
                if (tileCounter.colour.equals(tile.getColour())) {
                    tilesInTargetSet.remove(tileCounter);
                }
            }
        }
        else if (outerRadius != -1 && innerRadius != -1) {
            for(Tile tileCounter: tilesInTargetSet){
                if (tileCounter.findDistance()<innerRadius||tileCounter.findDistance()>outerRadius){
                    tilesInTargetSet.remove(tileCounter);
                }
            }
        }

        targetSet= tileSetUpdater(tilesInTargetSet, targetSet, value);
        return targetSet;
    }
}