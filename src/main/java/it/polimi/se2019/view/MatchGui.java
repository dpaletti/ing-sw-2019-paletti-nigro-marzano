package it.polimi.se2019.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.nio.file.Paths;


public class MatchGui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader tableLoader = new FXMLLoader(Paths.get("files/fxml/table.fxml").toUri().toURL());
        FXMLLoader boardLoader = new FXMLLoader(Paths.get("files/fxml/board.fxml").toUri().toURL());
        FXMLLoader weaponLoader = new FXMLLoader(Paths.get("files/fxml/weapon.fxml").toUri().toURL());
        FXMLLoader powerupLoader = new FXMLLoader(Paths.get("files/fxml/powerup.fxml").toUri().toURL());
        GridPane tableGrid = tableLoader.load();
        GridPane boardGrid = boardLoader.load();
        GridPane weaponGrid = weaponLoader.load();
        GridPane powerupGrid = powerupLoader.load();
        tableGrid.getChildren().addAll(tableGrid, boardGrid, weaponGrid, powerupGrid);
        Scene scene = new Scene(tableGrid);

        primaryStage.setTitle("Adrenaline");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void main(){
        launch(MatchGui.class);
    }
}
