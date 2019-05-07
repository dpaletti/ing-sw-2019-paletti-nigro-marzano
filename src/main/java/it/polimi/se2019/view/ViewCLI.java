package it.polimi.se2019.view;


import it.polimi.se2019.utility.MVEventDispatcher;

public class ViewCLI extends View {
    public Dispatcher dispatcher;

    public ViewCLI(){
        super();
    }

    private class Dispatcher extends MVEventDispatcher{

    }

    @Override
    public void update(MVEvent message) {
        message.handle(dispatcher);
    }
}
