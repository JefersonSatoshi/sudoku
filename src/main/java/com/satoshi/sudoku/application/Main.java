package com.satoshi.sudoku.application;

import com.satoshi.sudoku.controllers.GameViewController;
import com.satoshi.sudoku.entities.Board;
import com.satoshi.sudoku.entities.Cell;
import com.satoshi.sudoku.model.SudokuModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            Board board = new Board();
            String[] gameArgs = {
                    "0,0;4,false", "1,0;7,false", "2,0;9,true", "3,0;5,false", "4,0;8,true", "5,0;6,true", "6,0;2,true", "7,0;3,false", "8,0;1,false",
                    "0,1;1,false", "1,1;3,true", "2,1;5,false", "3,1;4,false", "4,1;7,true", "5,1;2,false", "6,1;8,false", "7,1;9,true", "8,1;6,true",
                    "0,2;2,false", "1,2;6,true", "2,2;8,false", "3,2;9,false", "4,2;1,true", "5,2;3,false", "6,2;7,false", "7,2;4,false", "8,2;5,true",
                    "0,3;5,true", "1,3;1,false", "2,3;3,true", "3,3;7,false", "4,3;6,false", "5,3;4,false", "6,3;9,false", "7,3;8,true", "8,3;2,false",
                    "0,4;8,false", "1,4;9,true", "2,4;7,false", "3,4;1,true", "4,4;2,true", "5,4;5,true", "6,4;3,false", "7,4;6,true", "8,4;4,false",
                    "0,5;6,false", "1,5;4,true", "2,5;2,false", "3,5;3,false", "4,5;9,false", "5,5;8,false", "6,5;1,true", "7,5;5,false", "8,5;7,true",
                    "0,6;7,true", "1,6;5,false", "2,6;4,false", "3,6;2,false", "4,6;3,true", "5,6;9,false", "6,6;6,false", "7,6;1,true", "8,6;8,false",
                    "0,7;9,true", "1,7;8,true", "2,7;1,false", "3,7;6,false", "4,7;4,true", "5,7;7,false", "6,7;5,false", "7,7;2,true", "8,7;3,false",
                    "0,8;3,false", "1,8;2,false", "2,8;6,true", "3,8;8,true", "4,8;5,true", "5,8;1,false", "6,8;4,true", "7,8;7,false", "8,8;9,false"
            };
            board.initializeBoardFromArgs(gameArgs);

            Cell[][] solutionGrid = createSolutionGrid();

            SudokuModel model = new SudokuModel(board, solutionGrid);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/satoshi/sudoku/views/GameView.fxml"));
            loader.setControllerFactory(clazz -> {
                GameViewController controller = new GameViewController();
                controller.setModel(model);
                return controller;
            });
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/satoshi/sudoku/style/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("SUDOKU");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao iniciar o jogo");
        }
    }

    private Cell[][] createSolutionGrid() {
        Cell[][] solutionGrid = new Cell[9][9];
        int[][] solution = {
                {4, 7, 9, 5, 8, 6, 2, 3, 1}, {1, 3, 5, 4, 7, 2, 8, 9, 6}, {2, 6, 8, 9, 1, 3, 7, 4, 5},
                {5, 1, 3, 7, 6, 4, 9, 8, 2}, {8, 9, 7, 1, 2, 5, 3, 6, 4}, {6, 4, 2, 3, 9, 8, 1, 5, 7},
                {7, 5, 4, 2, 3, 9, 6, 1, 8}, {9, 8, 1, 6, 4, 7, 5, 2, 3}, {3, 2, 6, 8, 5, 1, 4, 7, 9}
        };
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                solutionGrid[r][c] = new Cell(solution[r][c], false);
            }
        }
        return solutionGrid;
    }
}