package com.satoshi.sudoku.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene mainScene;

    public static Scene getMainScene() {
        return mainScene;
    }

    @Override
    public void start(Stage stage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/satoshi/sudoku/views/GameView.fxml"));
            AnchorPane anchorPane = loader.load();

            mainScene = new Scene(anchorPane);
            mainScene.getStylesheets().add(getClass().getResource("/com/satoshi/sudoku/style/styles.css").toExternalForm());
            stage.setScene(mainScene);
            stage.setTitle("SUDOKU");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error ao iniciar jogo");
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}
