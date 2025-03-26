package com.satoshi.sudoku.util;

import com.satoshi.sudoku.controllers.GameViewController;
import com.satoshi.sudoku.entities.Board;
import com.satoshi.sudoku.entities.Cell;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

import java.util.Map;
import java.util.Set;

public class SudokuGridUtil {
    private final Board board;
    private final Map<Cell, Button> cellButtonMap;

    public SudokuGridUtil(Board board, Map<Cell, Button> cellButtonMap) {
        this.board = board;
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

                cellButtonMap.put(cell, button);
                GridPane.setHgrow(button, Priority.ALWAYS);
                GridPane.setVgrow(button, Priority.ALWAYS);
                subGrid.add(button, col, row);
            }
        }
        return subGrid;
    }

    public GridPane createDraftGrid(Set<Integer> drafts) {
        GridPane draftGrid = new GridPane();
        draftGrid.setAlignment(Pos.CENTER);
        draftGrid.setPadding(new Insets(1, 1, 1, 1));
        draftGrid.setPrefSize(20, 20);
        draftGrid.setMinSize(20, 20);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.3);
            col.setHalignment(HPos.CENTER);
            draftGrid.getColumnConstraints().add(col);

            RowConstraints row = new RowConstraints();
            row.setPercentHeight(33.3);
            row.setValignment(VPos.CENTER);
            draftGrid.getRowConstraints().add(row);
        }

        for (int draft : drafts) {
            Text draftText = new Text(String.valueOf(draft));
            draftText.getStyleClass().add("drafts");
            switch (draft) {
                case 1: draftGrid.add(draftText, 0, 0); break;
                case 2: draftGrid.add(draftText, 1, 0); break;
                case 3: draftGrid.add(draftText, 2, 0); break;
                case 4: draftGrid.add(draftText, 0, 1); break;
                case 5: draftGrid.add(draftText, 1, 1); break;
                case 6: draftGrid.add(draftText, 2, 1); break;
                case 7: draftGrid.add(draftText, 0, 2); break;
                case 8: draftGrid.add(draftText, 1, 2); break;
                case 9: draftGrid.add(draftText, 2, 2); break;
            }
        }
        return draftGrid;
    }
}