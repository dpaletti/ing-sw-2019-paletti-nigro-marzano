package it.polimi.se2019.model;

import java.util.List;

public class Turn {
    private Player playing;
    private Combo firstCombo;
    private Combo secondCombo;
    List<PowerUp> usedPowerUp;

    public Combo getFirstCombo() { return firstCombo; }

    public Combo getSecondCombo() { return secondCombo; }

    public List<PowerUp> getUsedPowerUp() { return usedPowerUp; }

    public Player getPlaying() { return playing; }

    public void setFirstCombo(Combo firstCombo) { this.firstCombo = firstCombo; }

    public void setPlaying(Player playing) { this.playing = playing; }

    public void setSecondCombo(Combo secondCombo) { this.secondCombo = secondCombo; }

    public void setUsedPowerUp(List<PowerUp> usedPowerUp) { this.usedPowerUp = usedPowerUp; }
}
