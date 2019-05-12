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
    private Point position;
    private Turn turn;

    //ready

    public Figure (Tile tile, Player player, FigureColour figureColour, Point position, Turn turn){
        this.tile= tile;
        this.player= player;
        this.colour= figureColour;
        this.position= position;
        this.turn= turn;
    }

    public Point getPosition() {
        return position;
    }

    public FigureColour getColour() {
        return colour;
    }

    public Tile getTile() {
        return tile;
    }

    public Player getPlayer() {
        return player;
    }

    public Turn getTurn() {
        return turn;
    }

    public void damage(Figure target){
        target.player.addTear(colour);
        target.player.updatePlayerDamage();
    }

    public void mark(Figure target){
        int alreadyMaximumMarks=0;
        for(Tear marksCounter: target.player.getMarks()){
            if(marksCounter.getColour()==colour){
                alreadyMaximumMarks++;
            }
        }
        if(alreadyMaximumMarks<3) {
            target.player.addMark(colour);
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
                default:
                    break;
        }
        if (boundaryChecker(this, newPosition)){
            position=newPosition;
            tile=GameMap.getMap().get(newPosition);
        }
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
        if (boundaryChecker(target, newPosition)) {
            target.position = newPosition;
            target.tile = GameMap.getMap().get(newPosition);
        }
    }

    public void teleport (Point teleportPosition){ //only called in case of Teleport Event
        if (boundaryChecker(this, teleportPosition)){
            position= teleportPosition;
            tile=GameMap.getMap().get(position);
        }
    }

    public void damage (SpawnTile target){
        if (target.getTileType()==TileType.SPAWNTILE){
            target.addTear(colour);
        }

    }

    private Set<Figure> visibilitySet (){
        Set<Figure> visibleFigures=new HashSet<>();
        Set<Point> positions= new HashSet<>();
        Point northernDoor= position;
        Point southernDoor= position;
        Point easternDoor= position;
        Point westernDoor= position;

        for (Tile tileCounter: GameMap.getTiles()){
            if(tileCounter.getColour().equals(tile.getColour())){
                visibleFigures.addAll(tileCounter.getFigures());
            }
        }
        if (tile.getDoors().get(Direction.NORTH)){
            northernDoor.setY(tile.position.getY()+1);
            positions.add(northernDoor);
        }
        if (tile.getDoors().get(Direction.SOUTH)){
            southernDoor.setY(tile.position.getY()-1);
            positions.add(southernDoor);
        }
        if (tile.getDoors().get(Direction.EAST)){
            easternDoor.setX(tile.position.getX()+1);
            positions.add(easternDoor);
        }
        if (tile.getDoors().get(Direction.WEST)){
            westernDoor.setX(tile.position.getX()-1);
            positions.add(westernDoor);
        }
        for (Point positionCounter: positions){
            visibleFigures.addAll(GameMap.getMap().get(positionCounter).getFigures());
        }
        return visibleFigures;
    }

    private Set<Figure> visibilitySet (Figure targetFigure){
        Set<Figure> visibleFigures=new HashSet<>();
        Set<Point> positions= new HashSet<>();
        Point northernDoor= targetFigure.position;
        Point southernDoor= targetFigure.position;
        Point easternDoor= targetFigure.position;
        Point westernDoor= targetFigure.position;

        for (Tile tileCounter: GameMap.getTiles()){
            if(tileCounter.getColour().equals(targetFigure.tile.getColour())){
                visibleFigures.addAll(tileCounter.getFigures());
            }
        }
        if (targetFigure.tile.getDoors().get(Direction.NORTH)){
            northernDoor.setY(targetFigure.tile.position.getY()+1);
            positions.add(northernDoor);
        }
        if (targetFigure.tile.getDoors().get(Direction.SOUTH)){
            southernDoor.setY(targetFigure.tile.position.getY()-1);
            positions.add(southernDoor);
        }
        if (targetFigure.tile.getDoors().get(Direction.EAST)){
            easternDoor.setX(targetFigure.tile.position.getX()+1);
            positions.add(easternDoor);
        }
        if (targetFigure.tile.getDoors().get(Direction.WEST)){
            westernDoor.setX(targetFigure.tile.position.getX()-1);
            positions.add(westernDoor);
        }
        for (Point positionCounter: positions){
            visibleFigures.addAll(GameMap.getMap().get(positionCounter).getFigures());
        }
        return visibleFigures;
    }

    private int findDistance (Point positionOfTarget){
        return ((positionOfTarget.getX()-tile.getPosition().getX())+(positionOfTarget.getY()-tile.getPosition().getY()));
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
                    targetSet.add(figureCounter);
                    value=0;
                }
            }
        }
        else if (outerRadius != -1 && innerRadius != -1) {
            for(Figure figureCounter: figuresInTargetSet){
                if (figureCounter.findDistance(figureCounter.position)<innerRadius||figureCounter.findDistance(figureCounter.position)>outerRadius){
                    targetSet.add(figureCounter);
                    value=0;
                }
            }
        }

        targetSet= targetSetUpdater(figuresInTargetSet, targetSet, value);
        return targetSet;
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
                    targetSet.add(tileCounter);
                    value=0;
                }
            }
        }
        else if (outerRadius != -1 && innerRadius != -1) {
            for(Tile tileCounter: tilesInTargetSet){
                if (tileCounter.findDistance(tileCounter.position)<innerRadius||tileCounter.findDistance(tileCounter.position)>outerRadius){
                    targetSet.add(tileCounter);
                    value=0;
                }
            }
        }

        targetSet= tileSetUpdater(tilesInTargetSet, targetSet, value);
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

    private Boolean boundaryChecker (Figure figureToMove, Point newPosition){
        if (!GameMap.checkBoundaries(newPosition)){
            return (false);
        }
        if (GameMap.getMap().get(newPosition).getColour().equals(figureToMove.tile.colour)){ //same room, figure can be moved
            return (true);
        }
        else { //not same room, check if door
            Point positionCounter= figureToMove.position;
            for (Direction directionCounter: Direction.values()){
                if (figureToMove.tile.doors.get(directionCounter)){
                    if (directionCounter.equals(Direction.NORTH)){
                        positionCounter.setY(positionCounter.getY()+1);
                        if (positionCounter.equals(newPosition)){
                            return (true);
                        }
                    }
                    if (directionCounter.equals(Direction.SOUTH)){
                        positionCounter.setY(positionCounter.getY()-1);
                        if (positionCounter.equals(newPosition)){
                            return (true);
                        }
                    }
                    if (directionCounter.equals(Direction.EAST)){
                        positionCounter.setX(positionCounter.getX()+1);
                        if (positionCounter.equals(newPosition)){
                            return (true);
                        }
                    }
                    if (directionCounter.equals(Direction.WEST)){
                        positionCounter.setX(positionCounter.getX()-1);
                        if (positionCounter.equals(newPosition)){
                            return (true);
                        }
                    }
                }
            }
        }
        return (false);
    }

    private Set<Figure> targetsOfSelectedEffect(String effectName){
        Set<Figure> targetSet=new HashSet<>();
        targetSet.addAll(turn.mapEffectToTargets(effectName));
        return targetSet;
    }

    private Set<Tile> tilesOfSelectedEffect(String effectName){
        Set<Tile> targetSet=new HashSet<>();
        targetSet.addAll(turn.mapEffectToTiles(effectName));
        return targetSet;
    }

    //missing events

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
            WeaponToGrabEvent weaponToGrabEvent=new WeaponToGrabEvent(Game.getInstance().colourToUser(colour));
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
                WeaponToLeaveEvent weaponToLeaveEvent=new WeaponToLeaveEvent(Game.getInstance().colourToUser(colour));
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
            //TODO: VCEvent: no, end reload/yes, use selectedPowerUp to pay
            PowerUp selectedPowerUp=null;
            player.sellPowerUp(selectedPowerUp);
            reload(weapon);
        }
    }

    //to fix

    public Set<Pair<Effect, Set<Figure>>> generateWeaponEffect (GraphNode<Effect> node){
        Set<Pair<Effect, Set<Figure>>> targetSet=new HashSet<>();
        Pair <Effect, Set<Figure>> effectToTargets;

        Set<Player> players=new HashSet<>(Game.getInstance().getPlayers());
        Set<Figure> figures=new HashSet<>();
        for(Player playerCounter: players){
            figures.add(playerCounter.getFigure());
        }
        Set<Figure> seenFigures=visibilitySet();
        Set<Tile> seenTiles=tile.visibleTiles();
        for (Effect effectCounter: node.getNode()){
            Set<Figure> figuresInTargetSet= new HashSet<>();
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
                    case 2: //figures my previous target can see
                        int indexOfCurrentPlayer= Game.getInstance().getPlayers().indexOf(player);
                        Set<Figure> previousTargets= new HashSet<>(Game.getInstance().getTurns().get(indexOfCurrentPlayer).getFirstTargetSet());
                        for (Figure figureCounter: previousTargets)
                        {
                            figuresInTargetSet.addAll(visibilitySet(figureCounter));
                        }
                        break;
                    default:
                        break;
                }
                if (effectCounter.getTargetSpecification().getDifferent().getFirst()){
                    figuresInTargetSet= targetSetUpdater(figuresInTargetSet, targetsOfSelectedEffect(effectCounter.getName()), 0);
                }
                else {
                    figuresInTargetSet= targetSetUpdater(figuresInTargetSet, targetsOfSelectedEffect(effectCounter.getName()), 1);

                }

                figuresInTargetSet= areaSelectionForFigures(figuresInTargetSet, effectCounter.getTargetSpecification().getRadiusBetween().getFirst(), effectCounter.getTargetSpecification().getRadiusBetween().getSecond());

            }
            else { //target is a tile
                Set<Tile> tilesInTargetSet=new HashSet<>(GameMap.getTiles());
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
                    tilesInTargetSet= tileSetUpdater(tilesInTargetSet, tilesOfSelectedEffect(effectCounter.getName()), 0);
                }
                else {
                    tilesInTargetSet= tileSetUpdater(tilesInTargetSet, tilesOfSelectedEffect(effectCounter.getName()), 1);
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

                for (Tile tileCounter: tilesInTargetSet){ //converts tiles in figures to hit
                    figuresInTargetSet.addAll(tileCounter.getFigures());
                }
            }
            effectToTargets= new Pair<>(effectCounter, figuresInTargetSet);
            targetSet.add(effectToTargets);
        }
        return targetSet;
    } //missing previous
}