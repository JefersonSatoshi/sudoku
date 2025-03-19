package com.satoshi.sudoku.util;

import com.satoshi.sudoku.controllers.GameViewController;
import com.satoshi.sudoku.entities.Board;
import com.satoshi.sudoku.entities.Cell;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.util.Map;

public class SudokuGridUtil {
    private final Board board;
    private final GameViewController controller;
    private final Map<Cell, Button> cellButtonMap;

    public SudokuGridUtil(Board board, GameViewController controller, Map<Cell, Button> cellButtonMap) {
        this.board = board;
        this.controller = controller;
        this.cellButtonMap = cellButtonMap;
    }

    public void populateSudokuGrid(GridPane gridPane) {
        gridPane.getChildren().clear();
        for (int gridRow = 0; gridRow < 3; gridRow++) {
            for (int gridCol = 0; gridCol < 3; gridCol++) {
                GridPane subGrid = createSubGrid(gridRow, gridCol);
                gridPane.add(subGrid, gridCol, gridRow);
            }
        }
    }

    private GridPane createSubGrid(int gridRow, int gridCol) {
        GridPane subGrid = new GridPane();
        subGrid.getStyleClass().add("sub-grid");

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int globalRow = gridRow * 3 + row;
                int globalCol = gridCol * 3 + col;

                Cell cell = board.getCell(globalRow, globalCol);
                Button button = new Button();
                button.setMinSize(20, 20);
                button.setPrefSize(20, 20);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setAlignment(Pos.CENTER);

                if (cell.getValue() != null && cell.getValue() != 0) {
                    button.setText(String.valueOf(cell.getValue()));
                } else {
                    button.setText("");
                }

                button.setOnAction(e -> controller.selectCell(globalRow, globalCol));

                cellButtonMap.put(cell, button);
                GridPane.setHgrow(button, Priority.ALWAYS);
                GridPane.setVgrow(button, Priority.ALWAYS);
                subGrid.add(button, col, row);
            }
        }
        return subGrid;
    }
}