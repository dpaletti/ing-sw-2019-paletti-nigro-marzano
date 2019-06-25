package it.polimi.se2019.view;

import it.polimi.se2019.network.Client;

public enum UIMode {
    GUI{
        @Override
        public View createView(Client client) {
            ViewGUI.create(client);
            return ViewGUI.getInstance();
        }
    },
    CLI{
        @Override
        public View createView(Client client) {
            return new ViewCLI(client);
        }
    };
    public abstract View createView(Client client);
    public static UIMode parseUIMode(String toParse){
        if(toParse.equalsIgnoreCase("CLI"))
            return UIMode.CLI;
        else
            return UIMode.GUI;
    }

}
