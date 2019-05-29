package it.polimi.se2019.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class MatchGui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader tableLoader = new FXMLLoader(MatchMakingGui.class.getClassLoader().getResource("fxml/table.fxml"));
        FXMLLoader boardLoader = new FXMLLoader(MatchMakingGui.class.getClassLoader().getResource("fxml/board.fxml"));
        FXMLLoader weaponLoader = new FXMLLoader(MatchMakingGui.class.getClassLoader().getResource("fxml/weapon.fxml"));
        FXMLLoader powerupLoader = new FXMLLoader(MatchMakingGui.class.getClassLoader().getResource("fxml/powerup.fxml"));
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
