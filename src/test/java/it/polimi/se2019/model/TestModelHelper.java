package it.polimi.se2019.model;

import it.polimi.se2019.model.mv_events.MVWeaponEndEvent;
import it.polimi.se2019.view.MVEvent;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.vc_events.VCWeaponEndEvent;

import java.util.List;

public class TestModelHelper extends View {
    private MVEvent current;

    @Override
    public void update(MVEvent message) {
        current=message;
    }

    @Override
    public void matchMaking(List<String> usernames, List<String> configs) {
        throw new UnsupportedOperationException("NotSupported");
    }

    @Override
    public void addPlayer(String username) {
        throw new UnsupportedOperationException("NotSupported");
    }

    public MVEvent getCurrent() {
        return current;
    }

}
