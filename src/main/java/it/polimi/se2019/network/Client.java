package it.polimi.se2019.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private String ip;
    private int port;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void startClient() throws IOException{
        logger.log(Level.FINE, "Connection established");
        try(Socket socket = new Socket(ip, port);
            Scanner socketln = new Scanner(socket.getInputStream());
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
            Scanner stdin = new Scanner(System.in)){
            while(true){
                //TODO this placeholder loop needs to be worked to a concrete implementation
                String inputLine = stdin.nextLine();
                if(inputLine.equals("quit"))
                    break;
                socketOut.println(inputLine);
                socketOut.flush();
                String socketLine = socketln.nextLine();
                logger.log(Level.FINE, socketLine);
            }
        }catch(NoSuchElementException e){
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void main(String[] args){
        logger.log(Level.INFO, "input Server IP (with no spaces) then press Enter:");
        Scanner in = new Scanner(System.in);
        String ip = in.nextLine();
        logger.log(Level.INFO, "input Server port then press Enter ");
        int port = in.nextInt();
        Client client = new Client(ip, port);
        try{
            client.startClient();
        }catch(IOException e){
            logger.log(Level.SEVERE, e.getMessage());
        }
    }


}
