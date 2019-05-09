package it.polimi.se2019.view;


public class ViewCLI extends View {
    private Dispatcher dispatcher = new Dispatcher();

    public ViewCLI(){
        super();
    }

    private class Dispatcher extends ClientViewDispatcher{

    }

    @Override
    public void update(MVEvent message) {
        message.handle(dispatcher);
    }
}
