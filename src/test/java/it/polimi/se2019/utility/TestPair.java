package it.polimi.se2019.utility;

import it.polimi.se2019.commons.utility.Pair;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@Ignore
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

    private Pair<T, S> pair;

    @Test
    public void testSet(){
        pair = new Pair<>(t, s);

    }


}
