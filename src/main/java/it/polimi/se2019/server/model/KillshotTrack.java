package it.polimi.se2019.server.model;

import java.util.ArrayList;
import java.util.List;

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
