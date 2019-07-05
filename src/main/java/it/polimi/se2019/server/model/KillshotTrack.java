package it.polimi.se2019.server.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the killshot track in which all the kills are stored. When the size of the killshot equals
 * the number of chosen skulls, the regular match is over.
 */

public class KillshotTrack {
    private List<Skull> killshot= new ArrayList<>();
    private Integer numberOfSkulls;

    public KillshotTrack (Integer numberOfSkulls){
        this.numberOfSkulls= numberOfSkulls;
    }
    public List<Skull> getKillshot() {
        return killshot;
    }

    public Integer getNumberOfSkulls() {
        return numberOfSkulls;
    }

    public void addKillshot (FigureColour figureColour, Boolean overkill){
        Skull skull= new Skull(new Tear(figureColour), overkill);
        killshot.add(skull);
    }
}
