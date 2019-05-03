package it.polimi.se2019.model;

import it.polimi.se2019.view.ChooseAmongPreviousTargetsEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Figure {
    private Tile tile;
    private FigureColour colour;
    private Player player;
    private String figureName;

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
            case TELEPORT:
                //TODO: MVEvent: ask the user where he wants to be teleported
                //TODO: VMEvent: set newPosition to the specified one
        }
        tile.setPosition(newPosition);
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
    }

    public void grab(){
        if (tile.getTileType()==TileType.LOOTTILE) { //when on a Loot Tile, adds grabbed ammos to usable ammos making sure the number of ammos of a colour does not exceed 3
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
        else {  //se sono su una tile di spawn
            Weapon selectedWeapon= null;
            //TODO: MVEvent: user must pick a weapon from WeaponSpot
            // TODO: VMEvent: a Weapon is returned and assigned to selectedWeapon
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
                //TODO: MVEvent: user must pick a weapon to leave among first, second and ThirdWeapon
                //TODO: VMEvent: a weapon is returned and selectedWeapon is assigned to free weapon slot
            }
        }
    }

    private Set<Pair<Effect, Set<Figure>>> generateTargetSet(GraphNode<Effect> node){
        Set<Pair<Effect, Set<Figure>>> targetSet=null;
        Pair <Effect, Set<Figure>> effectToTargets=null;
        Set<Player> players=null;
        Set<Figure> figures=null;
        players.addAll(Game.getInstance().getPlayers());
        for(Player playerCounter: players){
            figures.add(playerCounter.getFigure());
        }
        Set<Figure> seenFigures=visibilitySet();
        Figure chosenPreviousTarget=null;
        Set<Tile> seenTiles=tile.visibleTiles();
        for (Effect effectCounter: node.getEffects()){
            Set<Figure> figuresInTargetSet=null;
            if(!effectCounter.getTargetSpecification().getTile()) { //target is a figure
                switch (effectCounter.getTargetSpecification().getVisible()) {
                    case -1:
                        figuresInTargetSet = figures;
                        break;
                    case 0:
                        figuresInTargetSet = figures;
                        for(Figure figureCounter: figures){
                            if(seenFigures.contains(figureCounter)){
                                figuresInTargetSet.remove(figureCounter);
                            }
                        }
                        break;
                    case 1:
                        figuresInTargetSet = seenFigures;
                        break;
                    case 2: //figures one of my previous targets can see
                        ChooseAmongPreviousTargetsEvent chooseTargetEvent=null;
                        Set<String> previousTargetNames=null;
                        Set<Figure> previousTargets=null;
                        int indexOfCurrentPlayer= Game.getInstance().getPlayers().indexOf(player);
                        previousTargets.addAll(Game.getInstance().getTurns().get(indexOfCurrentPlayer).getFirstTargetSet());
                        for(Figure figureCounter: previousTargets){
                            previousTargetNames.add(figureCounter.figureName);
                        }
                        chooseTargetEvent.setPreviousTargets(previousTargetNames);
                        Game.getInstance().sendMessage(chooseTargetEvent);
                        //TODO: VMEvent a target is returned and assigned to chosenPreviousTarget
                        Set<Figure> seenByTargetFigures=visibilitySet(chosenPreviousTarget);
                        figuresInTargetSet = seenByTargetFigures;
                        break;
                    default:
                        break;
                }
                switch (effectCounter.getTargetSpecification().getDifferent().getFirst()) {
                    case -1:
                        break;
                    case 0: //targetset+ figures hit in specified events
                        for(Figure figureCounter: figuresInTargetSet){
                            if(!targetsOfSelectedEffect(effectCounter).contains(figureCounter)){
                                figuresInTargetSet.remove(figureCounter);
                            }
                        }
                        break;
                    case 1: //targetset- figures hit in specified events
                        figuresInTargetSet.removeAll(targetsOfSelectedEffect(effectCounter));
                        break;
                    default:
                        break;
                }
                if (effectCounter.getTargetSpecification().getRadiusBetween().getFirst() == 0 & effectCounter.getTargetSpecification().getRadiusBetween().getSecond() == 0) {
                    Set<Figure> thisPlayer = null;
                    thisPlayer.add(player.getFigure());
                    figuresInTargetSet = thisPlayer;
                }
                else if (effectCounter.getTargetSpecification().getRadiusBetween().getFirst() == -2 & effectCounter.getTargetSpecification().getRadiusBetween().getSecond() == -2) {
                    for(Figure figureCounter: figuresInTargetSet){
                        if(!seenFigures.contains(figureCounter)){
                            figuresInTargetSet.remove(figureCounter);
                        }
                    }

                }
                else if (effectCounter.getTargetSpecification().getRadiusBetween().getFirst() == -3 & effectCounter.getTargetSpecification().getRadiusBetween().getSecond() == -3) {
                    //calculation of figures in different room could be moved to separate method
                    for (Figure figureCounter : figuresInTargetSet) {
                        if (figureCounter.tile.getColour().equals(tile.getColour())) {
                            figuresInTargetSet.remove(figureCounter);
                        }
                    }
                }
                else if (effectCounter.getTargetSpecification().getRadiusBetween().getFirst() != -1 & effectCounter.getTargetSpecification().getRadiusBetween().getSecond() != -1) {
                    for(Figure figureCounter: figuresInTargetSet){
                        if (figureCounter.findDistance()<effectCounter.getTargetSpecification().getRadiusBetween().getFirst()||figureCounter.findDistance()>effectCounter.getTargetSpecification().getRadiusBetween().getSecond()){
                            figuresInTargetSet.remove(figureCounter);
                        }
                    }
                }
            }
            else { //target is a tile
                Set<Tile> tilesInTargetSet=null;
                for (Tile tileCounter: GameMap.getTiles()){
                    tilesInTargetSet.add(tileCounter);
                }
                switch (effectCounter.getTargetSpecification().getVisible()){ //same as figures
                    case -1:
                        break;
                    case 0:
                        tilesInTargetSet= GameMap.getTiles();
                        for (Tile tileCounter: GameMap.getTiles()){
                            if(seenTiles.contains(tileCounter)){
                                tilesInTargetSet.remove(tileCounter);
                            }
                        }
                        break;
                    case 1:
                        tilesInTargetSet= seenTiles;
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
                switch (effectCounter.getTargetSpecification().getDifferent().getFirst()){
                    case -1:
                        break;
                    case 0:
                        for (Tile tileCounter: tilesInTargetSet){
                            if(!tilesOfSelectedEffect(effectCounter).contains(tileCounter)){
                                tilesInTargetSet.remove(tileCounter);
                            }
                        }
                        break;
                    case 1:
                        tilesInTargetSet.removeAll(tilesOfSelectedEffect(effectCounter));
                        break;
                        default:
                            break;

                }
                switch (effectCounter.getTargetSpecification().getEnlarge()){
                    case -1: //selects whole room
                        for(Tile sameRoomCounter: GameMap.getTiles()){
                            if(!tile.getColour().equals(sameRoomCounter.getColour())){
                                tilesInTargetSet.remove(sameRoomCounter);
                            }
                        }
                        break;
                    case -2: //enlarges in specified direction
                        break;
                        default: //Enlarge value specifies radius to enlarge area of action, must be >0
                            break;
                }
                if (effectCounter.getTargetSpecification().getArea()){ //Area weapon, targetset=figures on all selected tiles (player does not chose this)
                    //TODO: call a method that signals all figures in targetSet must be targeted by the effect or add flag to returned value
                }
                if (effectCounter.getTargetSpecification().getRadiusBetween().getFirst() == -2 & effectCounter.getTargetSpecification().getRadiusBetween().getSecond() == -2) {
                    for(Tile tileCounter: tilesInTargetSet){
                        if(!seenTiles.contains(tileCounter)){
                            tilesInTargetSet.remove(tileCounter);
                        }
                    }
                }
                else if (effectCounter.getTargetSpecification().getRadiusBetween().getFirst() == -3 & effectCounter.getTargetSpecification().getRadiusBetween().getSecond() == -3) {
                    for (Tile tileCounter : tilesInTargetSet) {
                        if (!tile.getColour().equals(tileCounter.getColour())) {
                            tilesInTargetSet.remove(tileCounter);
                        }
                    }
                }
                else if (effectCounter.getTargetSpecification().getRadiusBetween().getFirst() != -1 & effectCounter.getTargetSpecification().getRadiusBetween().getSecond() != -1) {
                    for(Tile tileCounter: tilesInTargetSet){
                        if (tileCounter.findDistance()<effectCounter.getTargetSpecification().getRadiusBetween().getFirst()||tileCounter.findDistance()>effectCounter.getTargetSpecification().getRadiusBetween().getSecond()){
                            tilesInTargetSet.remove(tileCounter);
                        }
                    }
                }
                for (Tile tileCounter: tilesInTargetSet){ //coonverts tiles in figures to hit
                    figuresInTargetSet.addAll(tileCounter.getFigures());
                }
            }
            effectToTargets= new Pair<>(effectCounter, figuresInTargetSet);
            targetSet.add(effectToTargets);
        }
        return targetSet;
    }

    public Map<Figure, List<Effect>> generateWeaponEffect (GraphNode<Effect> node){ return null;}

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
            for (Ammo ammoToReload: weapon.getPrice()) {
                player.getUsableAmmo().remove(ammoToReload);
                player.getUnusableAmmo().add(ammoToReload);
            }
        }
        else{
            //TODO: MVEvent: not enough ammo available to reload, use PowerUps?
            //TODO: VMEvent: no, end reload/yes, use selectedPowerUp to pay
            PowerUp selectedPowerUp=null;
            player.usePowerUp(selectedPowerUp);
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
        Set<Figure> visibleFigures=null;
        Point point= null;
        Tile pointToTile=null;
        for (Tile tileCounter: GameMap.getTiles()){
            if(tileCounter.getColour().equals(tile.getColour())){
                visibleFigures.addAll(tileCounter.getFigures());
            }
        }
        if (tile.getDoors().get(Direction.NORTH)){
            point.setX(tile.position.getX());
            point.setY(tile.position.getY()+1);
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
        Set<Figure> visibleFigures=null;
        Point point= null;
        Tile pointToTile=null;
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
        Set<Figure> targetSet=null;
        return targetSet;
    }
    public Set<Tile> tilesOfSelectedEffect(Effect effect){ //TODO: write method to calculate tiles of previous actions
        Set<Tile> targetSet=null;
        return targetSet;
    }
}