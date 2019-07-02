package it.polimi.se2019.server.controller;

import it.polimi.se2019.server.model.Game;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TestSetUpController {

    private Game game = new Game();
    private SetUpController setUpController = new SetUpController(game);
    private ArrayList<Integer> skulls = new ArrayList<>(Arrays.asList(7, 7, 9, 9));

    @Before
    public void setUp(){
        setUpController.setConfigs(new ArrayList<>(Arrays.asList("Large", "Small", "MediumRight", "Large", "Large")));
        setUpController.setIsFinalFrenzy(new ArrayList<>(Arrays.asList(true, false, true, true, false)));
        setUpController.setSkulls(new ArrayList<>(Arrays.asList(7, 7, 7, 7, 7)));
    }

    @Ignore
    @Test
    public void testEndTimer(){
        setUpController.endTimer();
    }
}
