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
    @Mock
    T t1;
    @Mock
    S s1;

    private Pair<T, S>  pair;

    @Test(expected = NullPointerException.class)
    public void testNull(){
        pair = new Pair<>(t, s);
        pair.setFirst(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNull2(){
        pair = new Pair<>(t, s);
        pair.setSecond(null);
    }

    @Test
    public void testSet(){
        pair = new Pair<>(t, s);
        pair.setFirst(t1);
        pair.setSecond(s1);
        assertEquals(pair.getFirst(), t1);
        assertEquals(pair.getSecond(), s1);

    }


}
