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

    //Example: Santoro:fcol:M;hp:VBBBBB;mrk:;w1:Thor,false;w2:,;w3:,;pnt:12;amm:R1,B2,Y3;pos:1,1
    public static String getEncodedUser(Player player, String user){
        return  user+ ":"+
                "fcol:"+getEncodedFigureColour(player.getFigure().getColour())+ ";"+
                "hp:"+getEncodedTears(player.getHp())+ ";"+
                "mrk:"+getEncodedTears(player.getMarks())+ ";"+
                "pval:"+player.getPlayerValue()+ ";"+
                getEncodedWeapons(player.getWeapons())+
                getEncodedPowerUps(player.getPowerUps())+
                "pnt:"+player.getPoints()+ ";"+
                "amm:"+getEncodedAmmo(player.getAmmo())+ ";"+
                "pos:"+player.getFigure().getPosition().getX()+ ","+ player.getFigure().getPosition().getY()+
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

    //TODO Wait till GameMap is not a static class then modify
    public static String getEncodedBoard(GameMap map){
        return map.getConfig().toString()+ getEncodedTiles(map.getTiles()) ;
    }

    // Example of encoded map: (2,2)34;(2,1)LockRifle,Cyberblade,Furnace; ...
    public static String getEncodedTiles(Set<Tile> tiles){
        String tempString="";
        for (Tile tile: tiles){
            tempString=tempString.concat(getEncodedTile(tile));
        }
        return tempString;
    }

    //TODO Refactor method to adjust it to the new structure of tile
    public static String getEncodedTile(Tile tile){
        String tempString="";
        /*
        tempString=tempString.concat("("+tile.getPosition().getX()+","+tile.getPosition().getY()+")");
        if (tile.getTileType().equals(TileType.SPAWNTILE)){
            /*WeaponSpot weaponSpot=tile.getWeaponSpot();
            tempString=tempString.concat(
                    weaponSpot.getFirstWeapon()+","+
                            weaponSpot.getSecondWeapon()+","+
                            weaponSpot.getThirdWeapon() + ";");
        }else if (tile.getTileType().equals(TileType.LOOTTILE)){
            tempString=tempString.concat(tile.getLootCard().getName()+ ";");
        }*/
        return tempString;
    }

    public static String getEncodedWeapons(List<Weapon> weapons){
        String encodedWeapons="";
        for(int i=0;i<3;i++){
            encodedWeapons=encodedWeapons.concat("w"+i+":");
            if (weapons.get(i)!=null)
                encodedWeapons=encodedWeapons.concat(weapons.get(i).getName()+","+weapons.get(i).getLoaded()+";");
            else
                encodedWeapons=encodedWeapons.concat(",;");
        }
        return encodedWeapons;
    }

    public static String getEncodedPowerUps(List<PowerUp> powerUps){
        String encodedPowerUps="";
        for (int i=0;i<3;i++){
            encodedPowerUps=encodedPowerUps.concat("p"+i+":");
            if (powerUps.get(i)!=null)
                encodedPowerUps=encodedPowerUps.concat(getEncodedPowerUpNames(powerUps.get(i))+";");
            else
                encodedPowerUps=encodedPowerUps.concat(",;");
        }
        return encodedPowerUps;
    }

    public static String addBoard(GameMap map,String file){
        Pattern pattern= Pattern.compile("£");
        return pattern.matcher(file).replaceAll(getEncodedBoard(map)+"£");
    }

    public static String addLastUser(String user,String file){
        Pattern pattern= Pattern.compile("@");
        return pattern.matcher(file).replaceAll(user+"@");
    }
}
