package it.polimi.se2019.utility;

import it.polimi.se2019.model.*;
import it.polimi.se2019.view.MVEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.regex.Pattern;

public class Saves implements Observer<MVEvent>, MVEventDispatcher{
    private static FileChannel fileChannel;

    public Saves(){}

    @Override
    public void update(MVEvent message) {
        try{
            //pre-processing
            message.handle(this);
        }catch (UnsupportedOperationException e){
            //default behaviour
        }
    }



    //Opens the filechannel
    public static void start(){
        try {
            fileChannel= FileChannel.open(Paths.get(Saves.class.getClassLoader().getResource("config/gamesState").getPath()));
        }catch (IOException e){
            //TODO What to do in case of exception?
            Log.severe("Found IOException");
        }

    }

    public static void save(Game game, Integer roomNumber){
        //TODO: Handle the exception
        //Now the string contains the right part of the file
        try {
            String file = Base64.getEncoder().encodeToString(fileChannel.map(FileChannel.MapMode.READ_WRITE, roomNumber * 600, 600).array());
            if (file.isEmpty()){
                //Then it's the first time saving in the match
                file=StateEncoder.generateEncodedGame();
                //Now i'll add the players
                for(Pair<FigureColour,String> pair: game.getUserLookup()){
                    file=StateEncoder.addPlayer(game.userToPlayer(pair.getSecond()),pair.getSecond(),file);
                }
                //Now i'll add the lastUser

            }
            fileChannel.write(ByteBuffer.wrap(file.getBytes(Charset.defaultCharset())));


        }catch (IOException e) {
            Log.severe("File not found");
        }

    }

    public static String saveUser(Player player,String user,String file){
        Pattern pattern= Pattern.compile(user+":"+StateEncoder.getEncodedFigureColour(player.getFigure().getColour())+";(B|Y|G|V|M)*{0,12};(B|Y|G|V|M)*{0,15};\\d*{0,2};\\d;\\w*{0,30};\\w*{0,30};\\w*{0,30};" +
                "\\w*{0,3}(B|Y|R);\\w*{0,3}(B|Y|R);\\w*{0,3}(B|Y|R);\\d*{0,3};R\\d,B\\d,Y\\d;\\d,\\d"+System.lineSeparator());
        return pattern.matcher(file).replaceAll(StateEncoder.getEncodedUser(player,user));

    }

    public static String saveTile(Tile tile,String file){
        /*if (tile.getTileType().equals(TileType.SPAWNTILE)){
            Pattern pattern=Pattern.compile("\\("+tile.getPosition().getX()+","+tile.getPosition().getY()+"\\)\\w+{1,30}," +
                    "\\w+{1,30},\\w+{1,30};");
           return pattern.matcher(file).replaceAll(StateEncoder.getEncodedTile(tile));
        }*/
        //If the Tile is a LootTile then i use another Pattern
        Pattern pattern=Pattern.compile("\\("+tile.getPosition().getX()+","+tile.getPosition().getY()+"\\)\\d*{0,3};");
        return pattern.matcher(file).replaceAll(StateEncoder.getEncodedTile(tile));

    }

    public static String getBoardToChange(String file){
        Pattern patternBoard=Pattern.compile("<Board><*>");
        return patternBoard.matcher(file).group();
    }

    //This method saves a weapon on a weaponSpot, it needs the oldWeapon and the newWeapon to make the change,
    public static String saveWeapon(Weapon oldWeapon,Weapon newWeapon,String file){
        Pattern patternWeapon= Pattern.compile(oldWeapon.getName());
        return patternWeapon.matcher(getBoardToChange(file)).replaceAll(newWeapon.getName());
    }

}
