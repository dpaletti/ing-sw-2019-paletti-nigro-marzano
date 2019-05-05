package it.polimi.se2019.view;

import it.polimi.se2019.controller.MatchController;
import it.polimi.se2019.controller.MatchMakingController;

public class JoinEvent extends VCEvent {
    private String password;
    private String bootstrapId;

    public JoinEvent(String remoteEndId, String p, String bootstrapId){
        super(remoteEndId);
        this.password = p;
        this.bootstrapId = bootstrapId;

    }

    public String getPassword() {
        return password;
    }

    public String getBootstrapId() {
        return bootstrapId;
    }

    @Override
    public void handle(MatchMakingController controller) {
        controller.update(this);
    }

    public void handle(MatchController controller){
        controller.update(this);
    }
}
