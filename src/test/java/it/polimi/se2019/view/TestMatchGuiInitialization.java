package it.polimi.se2019.view;


import it.polimi.se2019.network.Client;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RunWith(MockitoJUnitRunner.class)
public class TestMatchGuiInitialization {
    @Mock
    Client client;
    @Mock
    GuiController guiController;
    @Mock
    MVEvent event;
    private ViewGUI toTest = new ViewGUI(client);
    private List<String> usernames = new ArrayList<String>();

    @Before
    public void setup(){
        usernames.add("test");
        toTest.startControllerWatchDog();
    }

    @Test
    public void semTest(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        new Thread(toTest::closeMatchMaking).start();
        executorService.submit(() -> {
            ViewGUI.staticRegister(guiController);
            ViewGUI.staticRegister(guiController);
            ViewGUI.staticRegister(guiController);
            ViewGUI.staticRegister(guiController);
            ViewGUI.staticRegister(guiController);});
        executorService.submit(() -> toTest.update(event));
    }
}
