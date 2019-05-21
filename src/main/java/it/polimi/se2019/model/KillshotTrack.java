package it.polimi.se2019.model;

import java.util.List;

public class KillshotTrack {
    private List<Skull> killshot;
    private Integer numberOfSkulls;

    public KillshotTrack (List<Skull> killshot, Integer numberOfSkulls){
        this.killshot=killshot;
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
