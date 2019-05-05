package it.polimi.se2019.controller;

import it.polimi.se2019.view.VirtualView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TestMatchBeginning {
    List<String> usernames;

    @Mock
    VirtualView virtualView;

    @Before
    public void setup(){
        usernames = new ArrayList<>();
        usernames.add("username1");
        usernames.add("username2");
        usernames.add("username3");
    }


    @Test(expected = NullPointerException.class)
    public void testNullView(){
        new MatchController(null, usernames);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUsernames(){
        new MatchController(virtualView, null);
    }

    @Test(expected = InvalidParameterException.class)
    public void testInvalidUsernames(){
        List<String> invalidUsernames = new ArrayList<>();
        invalidUsernames.add("username1");
        invalidUsernames.add("username2");
        new MatchController(virtualView, invalidUsernames);
    }

}
