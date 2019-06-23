package it.polimi.se2019.view;

import javafx.application.Application;

import java.util.concurrent.Semaphore;

public class ViewTester {
    public static Semaphore sem = new Semaphore(1, true);

    public  static void main(String[] args){
        sem.acquireUninterruptibly();
        new Thread(() -> Application.launch(TestApp.class)).start();

    }
}
