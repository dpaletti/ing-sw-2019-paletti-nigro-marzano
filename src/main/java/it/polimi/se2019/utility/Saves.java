package it.polimi.se2019.utility;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.mv_events.DiscardedPowerUpEvent;
import it.polimi.se2019.model.mv_events.MVMoveEvent;
import it.polimi.se2019.view.MVEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Set;
import java.util.regex.Matcher;
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

    @Override
    public void dispatch(MVMoveEvent message) {

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
        Pattern pattern= Pattern.compile(user+":fcol:"+StateEncoder.getEncodedFigureColour(player.getFigure().getColour())+";hp:(B|Y|G|V|M){0,12};mrk:(B|Y|G|V|M){0,15};pval:\\d;w1:\\w{0,30},(true|false);w2:\\w{0,30},(true|false);w3:\\w{0,30},(true|false);" +
                "p1:\\w{0,3}(B|Y|R);p2:\\w{0,3}(B|Y|R);p3:\\w{0,3}(B|Y|R);pnt:\\d{0,3};amm:R\\d,B\\d,Y\\d;pos:\\d,\\d"+System.lineSeparator());
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

    public static String getBoard(String file){
        Pattern patternBoard=Pattern.compile("<Board><*>");
        return patternBoard.matcher(file).group();
    }

    public static Pattern getUser(String user, String file) {
        return Pattern.compile(user + "://w*" + System.lineSeparator());
    }

    public static String getUserColour(String user,String file){
        Pattern pattern=Pattern.compile("fcol:\\w");
        Pattern colour=Pattern.compile("(B|Y|G|V|M)");
        return colour.matcher(pattern.matcher(getUser(user,file).matcher(file).group()).group()).group();
    }

    public static String saveHp(String attackedUser,String attackerUser,String file){
        Matcher userMatch=getUser(attackedUser,file).matcher(file);
        String user=userMatch.group();
        Pattern pattern= Pattern.compile("hp:(B|Y|G|V|M){0,12};");
        Matcher userHpMatch=pattern.matcher(user);
        String hps= userHpMatch.group();
        Pattern hp=Pattern.compile("(B|Y|G|V|M){0,12}");
        return userMatch.replaceAll(userHpMatch.replaceAll("hp:"+hp.matcher(hps).group()+getUserColour(attackerUser,file)+";"));
    }

    public static String savePosition(String user,Point newPosition,String file){
        Matcher userMatch=getUser(user,file).matcher(file);
        Pattern pattern= Pattern.compile("pos:\\d,\\d");
        return userMatch.replaceAll(pattern.matcher(userMatch.group()).replaceAll("pos:"+newPosition.getX()+","+newPosition.getY()));
    }

    public static String saveMarks(String markedUser,String markerUser,String file){
        Matcher userMatch=getUser(markedUser,file).matcher(file);
        String user=userMatch.group();
        Pattern pattern= Pattern.compile("mrk:(B|Y|G|V|M){0,15};");
        Matcher userHpMatch=pattern.matcher(user);
        String mrk= userHpMatch.group();
        Pattern marks=Pattern.compile("(B|Y|G|V|M){0,15}");
        return userMatch.replaceAll(userHpMatch.replaceAll("mrk:"+marks.matcher(mrk).group()+getUserColour(markerUser,file)+";"));
    }

    public static String savePoints(String user,String file,int points){
        Matcher userMatch=getUser(user,file).matcher(file);
        Pattern pattern= Pattern.compile("pnt:\\d{0,3};");
        return userMatch.replaceAll(pattern.matcher(userMatch.group()).replaceAll("pnt:"+points+";"));
    }

}
