package it.polimi.se2019.network;

public class Settings {
    private Settings(){}
    public static final String REMOTE_SERVER_NAME = "GameServer";
    public static final String REMOTE_CLIENT_NAME = "GameClient";
    public static final int MATCH_MAKING_TIMER = 1000 * 10; //in milliseconds
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MAX_WEAPON_LENGTH = 30; //TODO scan weapon folder
    public static final int MATCH_SETUP_TIMER = 30;
    //TODO put this into configuration file
}

