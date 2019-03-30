package it.polimi.se2019.model;

import java.util.List;

public class DominationBoard {
    private KillshotTrack killshotTrack;
    private List<Tear> blueSpawnpointTrack;
    private List<Tear> yellowSpawnpointTrack;
    private List<Tear> redSpawnpointTrack;

    public KillshotTrack getKillshotTrack() {
        return killshotTrack;
    }

    public List<Tear> getBlueSpawnpointTrack() {
        return blueSpawnpointTrack;
    }

    public List<Tear> getRedSpawnpointTrack() {
        return redSpawnpointTrack;
    }

    public List<Tear> getYellowSpawnpointTrack() {
        return yellowSpawnpointTrack;
    }

    public void setBlueSpawnpointTrack(List<Tear> blueSpawnpointTrack) {
        this.blueSpawnpointTrack = blueSpawnpointTrack;
    }

    public void setKillshotTrack(KillshotTrack killshotTrack) {
        this.killshotTrack = killshotTrack;
    }

    public void setRedSpawnpointTrack(List<Tear> redSpawnpointTrack) {
        this.redSpawnpointTrack = redSpawnpointTrack;
    }

    public void setYellowSpawnpointTrack(List<Tear> yellowSpawnpointTrack) {
        this.yellowSpawnpointTrack = yellowSpawnpointTrack;
    }
}
