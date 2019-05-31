package it.polimi.se2019.utility;

import it.polimi.se2019.model.*;

import java.util.*;
import java.util.regex.Pattern;

public class StateEncoder {
    private StateEncoder(){}
    public static String getEncodedUsers(Game game){
        String encodedUsers="";

        for (Pair<FigureColour,String> pair: game.getUserLookup()){
            String tempString= getEncodedUser(game.userToPlayer(pair.getSecond()),pair.getSecond());
            encodedUsers= encodedUsers.concat(tempString);
        }
        return encodedUsers;
    }

    public static String getEncodedUser(Player player, String user){
        return  user+ ":"+
                getEncodedFigureColour(player.getFigure().getColour())+ ";"+
                getEncodedTears(player.getHp())+ ";"+
                getEncodedTears(player.getMarks())+ ";"+
                player.getPointsToAssign().get(0)+ ";"+
                player.getFirstWeapon().getName()+ ";"+
                player.getSecondWeapon().getName()+ ";"+
                player.getThirdWeapon().getName()+ ";"+
                getEncodedPowerUpNames(player.getFirstPowerUp())+
                getEncodedPowerUpColor(player.getFirstPowerUp())+ ";"+
                getEncodedPowerUpNames(player.getSecondPowerUp())+
                getEncodedPowerUpColor(player.getSecondPowerUp())+ ";"+
                getEncodedPowerUpNames(player.getThirdPowerUp())+
                getEncodedPowerUpColor(player.getThirdPowerUp())+ ";"+
                player.getPoints()+ ";"+
                getEncodedAmmo(player.getUsableAmmo())+ ";"+
                player.getFigure().getPosition().getX()+ ","+ player.getFigure().getPosition().getY()+
                System.lineSeparator();
    }

    public static String getEncodedFigureColour(FigureColour figureColour){
        switch (figureColour){
            case MAGENTA: return "M";
            case BLUE: return "B";
            case GREY: return "G";
            case GREEN: return "V";
            case YELLOW: return "Y";
            default: throw new IllegalArgumentException("Color does not exist");
        }

    }

    public static String getEncodedPowerUpNames(PowerUp powerUp){
        if(powerUp.getName().contains("Targeting")){
            return "Tar";
        }else if(powerUp.getName().contains("Teleport")){
            return "Tel";
        }else if(powerUp.getName().contains("Tagback")){
            return "Tag";
        }else if (powerUp.getName().contains("Newton")){
            return "New";
        }
        throw new IllegalArgumentException("The powerUp does not exist");
    }

    public static String getEncodedPowerUpColor(PowerUp powerUp){
        switch (powerUp.getCardColour().getColour()){
            case YELLOW: return "Y";
            case BLUE: return "B";
            case RED: return "R";
            default: throw new IllegalArgumentException("The powerUp color does not exist");
        }
    }

    public static String getEncodedAmmo(Set<Ammo> ammoSet){
        int redCounter=0;
        int blueCounter=0;
        int yellowCounter=0;
        for (Ammo ammo:ammoSet){
            switch (ammo.getColour()){
                case RED: redCounter++;
                break;
                case BLUE: blueCounter++;
                break;
                case YELLOW: yellowCounter++;
                break;
                default: throw new IllegalArgumentException("AmmoColour not supported");
            }
        }
        return "R"+redCounter+","+"B"+blueCounter+","+"Y"+yellowCounter;
    }

    public static String getEncodedTears(Collection<Tear> list){
        String tempString="";
        for (Tear tear: list){
            switch (tear.getColour()){
                case YELLOW: tempString=tempString.concat("Y");
                break;
                case BLUE: tempString=tempString.concat("B");
                break;
                case GREEN: tempString=tempString.concat("V");
                break;
                case GREY: tempString=tempString.concat("G");
                break;
                case MAGENTA: tempString=tempString.concat("M");
                break;
                default: throw new IllegalArgumentException("Invalid Tear Colour");
            }
        }
        return tempString;
    }

    public static String generateEncodedGame(){
        return "<Players><&>"+System.lineSeparator()+"<Board><£>"+System.lineSeparator()+"<Last><@>"+System.lineSeparator();
    }

    public static String addPlayer(Player player,String user,String file){
        Pattern pattern= Pattern.compile("&");
        return pattern.matcher(file).replaceAll(getEncodedUser(player,user)+"&");
    }

    /*//TODO Wait till GameMap is not a static class then modify
    public static String getEncodedBoard(GameMap map){
        return map.getConfig().toString()+ getEncodedMap(map.) ;
    }*/

    public static String getEncodedMap(Map<Point,Tile> map){
        String tempString="";
        for(Point point: map.keySet()){
            tempString=tempString.concat("("+point.getX()+","+point.getY()+")");
            Tile tempTile=map.get(point);
            if (tempTile.getTileType().equals(TileType.SPAWNTILE)){
               WeaponSpot weaponSpot=tempTile.getWeaponSpot();
               tempString=tempString.concat(
                       weaponSpot.getFirstWeapon()+","+
                       weaponSpot.getSecondWeapon()+","+
                       weaponSpot.getThirdWeapon() + ";");
            }else if (tempTile.getTileType().equals(TileType.LOOTTILE)){
                //TODO After the LootCard commit add the number of the lootCard
            }
        }
        return tempString;
    }

    /*public static String addBoard(GameMap map,String file){
        Pattern
    }*/

    public static String addLastUser(String user,String file){
        Pattern pattern= Pattern.compile("@");
        return pattern.matcher(file).replaceAll(user+"@");
    }
}