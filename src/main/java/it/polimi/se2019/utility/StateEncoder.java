package it.polimi.se2019.utility;

import it.polimi.se2019.model.*;

import java.util.*;
import java.util.regex.Pattern;

public class StateEncoder {
    private StateEncoder(){}

    //This method is used to encode the user when the game is starting, so it creates the right user structure
    public static String getEncodedUser(FigureColour figureColour, String user){
        return  user+ ":"+
                "fcol:"+getEncodedFigureColour(figureColour)+ ";"+
                "hp:;mrk:;dths:0;w1:,;w2:,;w3:,;p1:;p2:;p3:;pnt:;amm:;pos:,;"+
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

    public static String getEncodedPowerUp(String powerUpName){
        String encodedPowerUp="";
        if(powerUpName.contains("Targeting"))
            encodedPowerUp=encodedPowerUp.concat("Tar");
        else if(powerUpName.contains("Teleport"))
            encodedPowerUp=encodedPowerUp.concat("Tel");
        else if(powerUpName.contains("Tagback"))
            encodedPowerUp=encodedPowerUp.concat("Tag");
        else if (powerUpName.contains("Newton"))
            encodedPowerUp=encodedPowerUp.concat("New");

        if (powerUpName.contains("Blue"))
            encodedPowerUp=encodedPowerUp.concat("B");
        else if (powerUpName.contains("Red"))
            encodedPowerUp=encodedPowerUp.concat("R");
        else if (powerUpName.contains("Yellow"))
            encodedPowerUp=encodedPowerUp.concat("Y");
        return encodedPowerUp;
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
        return "<Players><&>"+System.lineSeparator()+
                "<Board><£>"+System.lineSeparator()+
                "<Last><@>"+System.lineSeparator()+
                "<KillShot><%>"+System.lineSeparator()+
                "<Discarded><!>"+System.lineSeparator()+
                "<Frenzy><?>";
    }

    public static String addPlayer(FigureColour figureColour,String user,String file){
        Pattern pattern= Pattern.compile("&");
        return pattern.matcher(file).replaceAll(getEncodedUser(figureColour,user)+"&");
    }

    //Encoded board at the beginning without things placed on the tiles
    public static String getEncodedBoard(String config){
        return "map:config:"+config+";";
    }

    // Example of encoded map: (2,2)34;(2,1)LockRifle,Cyberblade,Furnace; ...
    public static String getEncodedTiles(Set<Tile> tiles){
        String tempString="";
        for (Tile tile: tiles){
            tempString=tempString.concat(getEncodedTile(tile));
        }
        return tempString;
    }

    public static String getEncodedTile(Tile tile){
        String tempString="";
        tempString=tempString.concat("pos:"+tile.getPosition().getX()+","+tile.getPosition().getY()+";");
        if (tile.getGrabbables().size()>1){
            //The tile is a spawn tile
            for(int i=0;i<3;i++){
                tempString=tempString.concat("w"+i+":");
                if (tile.getGrabbables().get(i)!=null)
                    tempString=tempString.concat(tile.getGrabbables().get(i).getName()+";");
                else
                    tempString=tempString.concat(",;");
            }
        }else{
            //The tile is a lootTile
            tempString=tempString.concat("lc:"+tile.getGrabbables().get(0).getName()+";");
        }
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
                encodedPowerUps=encodedPowerUps.concat(getEncodedPowerUp(powerUps.get(i).getName())+";");
            else
                encodedPowerUps=encodedPowerUps.concat(",;");
        }
        return encodedPowerUps;
    }

    public static String addBoard(String map,String file){
        Pattern pattern= Pattern.compile("£");
        return pattern.matcher(file).replaceAll(getEncodedBoard(map)+"£");
    }

    public static String addLastUser(String user,String file){
        Pattern pattern= Pattern.compile("@");
        return pattern.matcher(file).replaceAll(user+"@");
    }

    public static int getEncodedKills(Player player){
        switch (player.getPlayerValue().getMaxValue()){
            case(8): return 0;
            case (6): return 1;
            case (4): return 2;
            case (2): return 3;
            case (1): return 4;
        }
        throw new IllegalArgumentException("PlayerValue can't be different from these");
    }

    public static String addKillShot(int skulls,String file){
        Pattern pattern= Pattern.compile("%");
        String killshot="";
        for(int i=0;i<skulls;i++){
            killshot=killshot.concat(killshot+"S");
        }
        return pattern.matcher(file).replaceAll(killshot+"%");
    }

    public static String addFrenzy(boolean frenzy,String file){
        Pattern pattern= Pattern.compile("%");
        return pattern.matcher(file).replaceAll(frenzy+"?");
    }


}
