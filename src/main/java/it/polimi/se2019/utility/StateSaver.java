package it.polimi.se2019.utility;

import it.polimi.se2019.model.FigureColour;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.mv_events.*;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.VCEvent;
import javafx.beans.Observable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StateSaver implements Observer<MVEvent>, MVEventDispatcher{
    private int roomNumber;
    private String file;
    private FileChannel fileChannel;

    public StateSaver(int roomNumber) {
        this.roomNumber = roomNumber;
        try {
            fileChannel= FileChannel.open(Paths.get("files/config/gamesState"));
            fileChannel.position((long)roomNumber * 600);
            file= Base64.getEncoder().encodeToString(fileChannel.map(FileChannel.MapMode.READ_WRITE, (long)roomNumber * 600, 600).array());
            //Now i have to check if the file is empty, if it is i have to encode the game
            if(file.equals("")){
                file=StateEncoder.generateEncodedGame();
            }else{
                //If the file is not empty it means that the server went down so i have to decode the file
            }

        } catch (IOException e) {
            Log.severe("Can't access to gamesState file");
        }
    }

    private void save(){
        try {
            fileChannel.write(ByteBuffer.wrap(file.getBytes(Charset.defaultCharset())));
        }catch (IOException e){
            Log.severe("Can't write to gamesState file");
        }
    }

    @Override
    public void update(MVEvent message) {
        try{
            if(message.getDestination().equals("*"))
                message.handle(this);
        }catch (UnsupportedOperationException e){
            Log.fine("Ignoring: " + message);
        }
    }

    @Override
    public void dispatch(MVMoveEvent message) {
        file=savePosition(message.getUsername(),message.getFinalPosition(),file);
        save();
    }

    @Override
    public void dispatch(UpdateMarkEvent message) {
        file=saveMarks(message.getMarked(),message.getMarker(),file);
        save();
    }

    @Override
    public void dispatch(UpdateHpEvent message) {
        file=saveHp(message.getAttacked(),message.getAttacker(),file);
        save();
    }

    @Override
    public void dispatch(MVDeathEvent message) {
        file=saveKills(message.getDead(),file);
        file=saveKillshot(message.getKiller(),message.isOverkill(),file);
        save();
    }

    @Override
    public void dispatch(UpdatePointsEvent message) {
        file=savePoints(message.getUsername(),message.getPoints(),file);
        save();
    }

    @Override
    public void dispatch(SetUpEvent message) {
        for(Map.Entry<String, FigureColour> row: message.getUserToColour().entrySet()){
            file=StateEncoder.addPlayer(row.getValue(),row.getKey(),file);
        }
        save();
    }

    @Override
    public void dispatch(StartTurnEvent message) {
        file=StateEncoder.addLastUser(message.getUser(),file);
        save();
    }

    @Override
    public void dispatch(FinalConfigurationEvent message) {
        file=StateEncoder.addBoard(message.getConfig(),file);
        file=StateEncoder.addFrenzy(message.isFrenzy(),file);
        file=StateEncoder.addKillShot(message.getSkulls(),file);
        save();
    }





    //Todo: only keeping the method to look at the pattern while implementing submethods
    private String saveUser(Player player, String user, String file) {
        Pattern pattern = Pattern.compile(user + ":fcol:" + StateEncoder.getEncodedFigureColour(player.getFigure().getColour()) + ";hp:(B|Y|G|V|M){0,12};mrk:(B|Y|G|V|M){0,15};dths:\\d;w1:\\w{0,30},(true|false)?;w2:\\w{0,30},(true|false)?;w3:\\w{0,30},(true|false)?;" +
                "p1:\\w{0,3}(B|Y|R);p2:\\w{0,3}(B|Y|R);p3:\\w{0,3}(B|Y|R);pnt:\\d{0,3};amm:R\\d,B\\d,Y\\d;pos:\\d,\\d" + System.lineSeparator());
        return /*pattern.matcher(file).replaceAll(StateEncoder.getEncodedUser(player,user));*/ "";
    }

    private String getBoard(String file){
        Pattern patternBoard=Pattern.compile("<Board><*>");
        return patternBoard.matcher(file).group();
    }

    private Pattern getUser(String user, String file) {
        return Pattern.compile(user + "://w*" + System.lineSeparator());
    }

    private String getUserColour(String user,String file){
        Pattern pattern=Pattern.compile("fcol:\\w");
        Pattern colour=Pattern.compile("(B|Y|G|V|M)");
        return colour.matcher(pattern.matcher(getUser(user,file).matcher(file).group()).group()).group();
    }

    //This method takes in the damaged and the damager users and the file, and write the changes
    private String saveHp(String attackedUser,String attackerUser,String file){
        Matcher userMatch=getUser(attackedUser,file).matcher(file);
        String user=userMatch.group();
        Pattern pattern= Pattern.compile("hp:(B|Y|G|V|M){0,12};");
        Matcher userHpMatch=pattern.matcher(user);
        String hps= userHpMatch.group();
        Pattern hp=Pattern.compile("(B|Y|G|V|M){0,12}");
        return userMatch.replaceAll(userHpMatch.replaceAll("hp:"+hp.matcher(hps).group()+getUserColour(attackerUser,file)+";"));
    }

    //This method takes in the user that'is moving,the new position and the file and save it
    private String savePosition(String user,Point newPosition,String file){
        Matcher userMatch=getUser(user,file).matcher(file);
        Pattern pattern= Pattern.compile("pos:\\d,\\d");
        return userMatch.replaceAll(pattern.matcher(userMatch.group()).replaceAll("pos:"+newPosition.getX()+","+newPosition.getY()));
    }

    //This method takes in the marked and the marker users and the file, and write the changes
    private String saveMarks(String markedUser,String markerUser,String file){
        Matcher userMatch=getUser(markedUser,file).matcher(file);
        String user=userMatch.group();
        Pattern pattern= Pattern.compile("mrk:(B|Y|G|V|M){0,15};");
        Matcher userHpMatch=pattern.matcher(user);
        String mrk= userHpMatch.group();
        Pattern marks=Pattern.compile("(B|Y|G|V|M){0,15}");
        return userMatch.replaceAll(userHpMatch.replaceAll("mrk:"+marks.matcher(mrk).group()+getUserColour(markerUser,file)+";"));
    }

    //This method takes in the user, the new points and the file and write the changes
    private String savePoints(String user,int points,String file){
        Matcher userMatch=getUser(user,file).matcher(file);
        Pattern pattern= Pattern.compile("pnt:\\d{0,3};");
        return userMatch.replaceAll(pattern.matcher(userMatch.group()).replaceAll("pnt:"+points+";"));
    }

    //This method takes in the dead user and the file and updates the kills
    private String saveKills(String dead,String file){
        Matcher userMatch=getUser(dead,file).matcher(file);
        Pattern pattern= Pattern.compile("dths:\\d;");
        Pattern kills=Pattern.compile("\\d");
        Matcher killMatch= kills.matcher(pattern.matcher(userMatch.group()).group());
        int newKills= Integer.parseInt(killMatch.group())+1;
        return userMatch.replaceAll(killMatch.replaceAll("dths:"+newKills));
    }

   private String saveKillshot(String killer,boolean overkill,String file){
        Pattern pattern=Pattern.compile("<Killshot><*>");
        Matcher areaMatch= pattern.matcher(file);
        Pattern killshot=Pattern.compile("[BYGVMbygvm]{0,7}S");
        Matcher killshotMatch= killshot.matcher(areaMatch.group());
        String killshotTrack=killshotMatch.group();
        String onlyTearKillshot= killshotTrack.substring(0,killshotTrack.length()-1);
        Pattern skull=Pattern.compile("S");
        Matcher skullMatch= skull.matcher(killshotMatch.group());
        String tear=getUserColour(killer,file);
        if (!overkill)
            tear=tear.toLowerCase();
        return areaMatch.replaceAll(killshotMatch.replaceAll(skullMatch.replaceAll(onlyTearKillshot+tear)));
   }


}