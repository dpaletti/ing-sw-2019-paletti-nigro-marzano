package it.polimi.se2019.view;

import it.polimi.se2019.network.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.nio.file.Paths;


public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(ViewGUI.getInstance() == null)
            ViewGUI.create(new Client());
        FXMLLoader loader = new FXMLLoader(Paths.get("files/fxml/table.fxml").toUri().toURL());
        FXMLLoader setUpLoader = new FXMLLoader(Paths.get("files/fxml/match_setup.fxml").toUri().toURL());

        AnchorPane anchorPane = loader.load();
        Pane pane = setUpLoader.load();

        Scene scene = new Scene(anchorPane);

        ((GridPane)scene.lookup("#root")).add(pane, 3, 2);

        primaryStage.setTitle("Adrenaline");
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}



