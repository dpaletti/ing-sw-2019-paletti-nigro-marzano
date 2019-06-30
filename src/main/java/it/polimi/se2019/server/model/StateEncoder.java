package it.polimi.se2019.server.model;

import java.util.regex.Pattern;

public class StateEncoder {
    private StateEncoder(){}

    //This method is used to encode the user when the game is starting, so it creates the right user structure
    public static String getEncodedUser(FigureColour figureColour, String user){
        return  user+ ":"+
                "fcol:"+getEncodedFigureColour(figureColour)+ ";"+
                "hp:;mrk:;dths:0;wpns:,;,;,;pups:,;,;,;pnt:;amm:R3,B3,Y3;pos:,;"+
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
            encodedPowerUp=encodedPowerUp.concat(",B");
        else if (powerUpName.contains("Red"))
            encodedPowerUp=encodedPowerUp.concat(",R");
        else if (powerUpName.contains("Yellow"))
            encodedPowerUp=encodedPowerUp.concat(",Y");
        return encodedPowerUp;
    }

    public static String getEncodedAmmo(AmmoColour ammo){
        if (ammo.equals(AmmoColour.RED))
            return "R";
        else if (ammo.equals(AmmoColour.BLUE))
            return "B";
        else if (ammo.equals(AmmoColour.YELLOW))
            return "Y";
        throw new IllegalArgumentException("Does not exist ammo of another colour");
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

    public static String addBoard(String map,String file){
        Pattern pattern= Pattern.compile("£");
        return pattern.matcher(file).replaceAll(getEncodedBoard(map)+"£");
    }

    public static String addLastUser(String user,String file){
        Pattern pattern= Pattern.compile("@");
        return pattern.matcher(file).replaceAll(user+"@");
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
