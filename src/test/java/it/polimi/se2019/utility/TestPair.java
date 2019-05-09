package it.polimi.se2019.utility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestPair <T, S>{
    @Mock
    T t;
    @Mock
    S s;

    private Pair<T, S>  pair;

    @Test(expected = NullPointerException.class)
    public void testNull(){
        pair = new Pair<>(t, s);
        pair.setFirst(null);
        pair.setSecond(s);
    }
}
