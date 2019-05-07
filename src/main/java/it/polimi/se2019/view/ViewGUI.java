package it.polimi.se2019.view;


import it.polimi.se2019.utility.MVEventDispatcher;

public class ViewGUI extends View {
    Dispatcher dispatcher;

    public ViewGUI(){
        super();
    }

    private class Dispatcher extends MVEventDispatcher{

    }

    @Override
    public void update(MVEvent message) {
        message.handle(dispatcher);
    }
}
