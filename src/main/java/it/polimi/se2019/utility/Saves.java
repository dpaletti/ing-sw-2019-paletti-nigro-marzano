package it.polimi.se2019.utility;

import it.polimi.se2019.model.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Saves {
    private static FileChannel fileChannel;

    private Saves(){}

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


        }catch (IOException e){
            Log.severe("File not found");
        }


    }

    public static String saveUser(Player player,String user,String file){
        Pattern pattern= Pattern.compile(user+":"+StateEncoder.getEncodedFigureColour(player.getFigure().getColour())+";(B|Y|G|V|M)*{0,12};(B|Y|G|V|M)*{0,15};\\d*{0,2};\\d;\\w*{0,30};\\w*{0,30};\\w*{0,30};" +
                "\\w*{0,3}(B|Y|R);\\w*{0,3}(B|Y|R);\\w*{0,3}(B|Y|R);\\d*{0,3};R\\d,B\\d,Y\\d;\\d,\\d"+System.lineSeparator());
        Matcher matcher= pattern.matcher(file);
        return matcher.replaceAll(StateEncoder.getEncodedUser(player,user));

    }


}
