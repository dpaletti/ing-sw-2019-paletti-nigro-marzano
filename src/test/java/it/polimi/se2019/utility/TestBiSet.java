package it.polimi.se2019.utility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestBiSet<T, S>{

    BiSet<T, S> set = new BiSet<>();
    @Mock
    T t;
    @Mock
    S s;
    @Mock
    T t1;
    @Mock
    S s1;

    @Test
    public void testEqualPairs(){
        Pair<T, S> pair1 = new Pair<>(t, s);
        Pair<T, S> pair2 = new Pair<>(t, s);
        set.add(pair1);
        set.add(pair2);
        assertEquals(set.size(), 1);
        assertTrue(set.containsFirst(t));
        assertTrue(set.containsSecond(s));
        assertEquals(set.getFirst(s), t);
        assertEquals(set.getSecond(t), s);
        assertTrue(set.contains(pair1));
        BiSet<T, S> set1 = new BiSet<>();
        set1.add(pair1);
        assertEquals(set1, set);
        assertEquals(set.hashCode(), set1.hashCode());
        set.remove(pair1);
        assertEquals(set.size(), 0);
    }

    @Test
    public void testContainsNull(){
        assertFalse(set.containsFirst(null));
        assertFalse(set.containsSecond(null));
    }

    @Test
    public void presenceAbsence(){
        set.add(new Pair<> (t, s));
        Pair<T, S> pair2 = new Pair<>(t1, s1);
        assertFalse(set.contains(pair2));
        assertFalse(set.containsFirst(t1));
        assertFalse(set.containsSecond(s1));
    }

}
