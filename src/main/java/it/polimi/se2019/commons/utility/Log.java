package it.polimi.se2019.commons.utility;

/**
 * This class implements the functions of a static logger with two different levels of severity: fine, which can be used
 * in any regular situation whilst severe is used when exceptions are thrown, both levels print on the standard error.
 * "info" level simply prints a string on the standard output.
 */

public class Log {
    /*how to use this logger:
      1. this logger is static, constructor has been disabled calls are like Log.fine("string")
      2. there are two severity levels:
      2a. fine is made so that it can be used in every situation
      2b. severe is to be used when exceptions are thrown
      3. setting the level to "severe" disables fine logs when running
      4. input is used to ask the user for input (server address, port...)
      In the end FINE messages are the one that should not show up when the application is in production.
     */

    private static String level = "fine";

    private Log(){}

    public static void input(String message){
        System.out.println(message); //NOSONAR
    }

    public static void info(String message){
        System.out.println("INFO: " + message); //NOSONAR
    }

    public static void fine(String message){
        if(level.equals("fine"))
            System.err.println("FINE: " + message); //NOSONAR
    }

    public static void severe(String message){

        System.err.println("SEVERE: " + message); //NOSONAR
    }

    public static void setLevel(String level) {
        if(level.equals("severe") || level.equals("fine"))
            Log.level = level;
        else
            throw new UnsupportedOperationException("Level must be either severe or fine");
    }

    public static String getLevel() {
        return level;
    }
}
