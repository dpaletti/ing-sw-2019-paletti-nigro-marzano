package it.polimi.se2019.view;

import it.polimi.se2019.network.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class TestApp extends Application {
    @Override
    public synchronized void start(Stage primaryStage) throws Exception {
        ViewGUI.create(new Client());
        FXMLLoader tableLoader = new FXMLLoader(Paths.get("files/fxml/table.fxml").toUri().toURL());
        Scene scene = new Scene(tableLoader.load());

        primaryStage.setTitle("Adrenaline");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.show();
        while(!primaryStage.isShowing())
            wait();
        ViewTester.sem.release();
    }

}
