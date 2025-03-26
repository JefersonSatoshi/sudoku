package com.satoshi.sudoku.view;

import com.satoshi.sudoku.entities.Cell;
import com.satoshi.sudoku.model.SudokuModel;
import com.satoshi.sudoku.util.SudokuGridUtil;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.util.Map;

public class SudokuBoardView {
    private final GridPane gridPane;
    private final SudokuModel model;
    private final Map<Cell, Button> cellButtonMap;
    private final SudokuGridUtil gridUtil;

    public SudokuBoardView(GridPane gridPane, SudokuModel model, Map<Cell, Button> cellButtonMap) {
        this.gridPane = gridPane;
        this.model = model;
        this.cellButtonMap = cellButtonMap;
        this.gridUtil = new SudokuGridUtil(model.getBoard(), cellButtonMap);
        gridUtil.populateSudokuGrid(gridPane);
    }

    public void updateView(Cell selectedCell) {
        clearAllButtons();
        updateCellsWithValuesAndStyles(selectedCell);
        updateCellsWithDrafts();
        updateCellHighlighting(selectedCell);
    }

    private void clearAllButtons() {
        for (Map.Entry<Cell, Button> entry : cellButtonMap.entrySet()) {
            Button button = entry.getValue();
            button.getStyleClass().removeAll("cell-fixed", "cell-correct", "cell-error", "cell-error-highlighted", "cell-selected");
            button.setText("");
            button.setGraphic(null);
            button.setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }

    private void updateCellsWithValuesAndStyles(Cell selectedCell) {
        int selectedRow = selectedCell != null ? model.getRowOfCell(selectedCell) : -1;
        int selectedCol = selectedCell != null ? model.getColOfCell(selectedCell) : -1;
        String selectedCellKey = selectedCell != null ? selectedRow + "," + selectedCol : null;
        List<Cell> selectedCellConflicts = getSelectedCellConflicts(selectedRow, selectedCol);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = model.getBoard().getCell(r, c);
                Button button = cellButtonMap.get(cell);
                if (button == null || cell.getValue() == null || cell.getValue() == 0) continue;

                button.setText(String.valueOf(cell.getValue()));

                String cellKey = r + "," + c;
                updateCellStyle(cell, button, cellKey, selectedCellKey, selectedCellConflicts);
            }
        }
    }

    private void updateCellsWithDrafts() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = model.getBoard().getCell(r, c);
                Button button = cellButtonMap.get(cell);
                if (button == null || (cell.getValue() != null && cell.getValue() != 0)) continue;

                if (!cell.getDrafts().isEmpty()) {
                    GridPane draftGrid = gridUtil.createDraftGrid(cell.getDrafts());
                    button.setText("");
                    button.setGraphic(draftGrid);
                    button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    button.getStyleClass().removeAll("cell-fixed", "cell-correct", "cell-error", "cell-error-highlighted", "cell-selected");
                } else {
                    button.setText("");
                    button.setGraphic(null);
                    button.setContentDisplay(ContentDisplay.CENTER);
                    button.getStyleClass().removeAll("cell-fixed", "cell-correct", "cell-error", "cell-error-highlighted", "cell-selected");
                }
            }
        }
    }

    private List<Cell> getSelectedCellConflicts(int selectedRow, int selectedCol) {
        if (selectedRow < 0 || selectedCol < 0 || model.getBoard().getCell(selectedRow, selectedCol).getValue() == null) {
            return null;
        }
        return model.getConflictingCells(selectedRow, selectedCol, model.getBoard().getCell(selectedRow, selectedCol).getValue());
    }

    private void updateCellStyle(Cell cell, Button button, String cellKey, String selectedCellKey, List<Cell> selectedCellConflicts) {
        List<Cell> conflicts = model.getConflictingCells(model.getRowOfCell(cell), model.getColOfCell(cell), cell.getValue());
        boolean matchesSolution = model.matchesSolution(model.getRowOfCell(cell), model.getColOfCell(cell), cell.getValue());
        boolean isSelectedCell = cellKey.equals(selectedCellKey);
        boolean hasError = !conflicts.isEmpty() || !matchesSolution;
        boolean isInConflictWithSelected = selectedCellConflicts != null && selectedCellConflicts.contains(cell);

        button.getStyleClass().removeAll("cell-fixed", "cell-correct", "cell-error", "cell-error-highlighted", "cell-selected");

        if (cell.isFixed()) {
            button.getStyleClass().add("cell-fixed");
        } else if (matchesSolution) {
            button.getStyleClass().add("cell-correct");
        } else {
            button.getStyleClass().add("cell-error");
        }

        if (hasError && (isSelectedCell || isInConflictWithSelected)) {
            button.getStyleClass().add("cell-error-highlighted");
            for (Cell conflictCell : conflicts) {
                if (conflictCell != null && conflictCell != cell) {
                    Button conflictButton = cellButtonMap.get(conflictCell);
                    if (conflictButton != null) {
                        conflictButton.getStyleClass().add("cell-error-highlighted");
                    }
                }
            }
        }
    }

    private void updateCellHighlighting(Cell selectedCell) {
        if (selectedCell == null) return;

        int selectedRow = model.getRowOfCell(selectedCell);
        int selectedCol = model.getColOfCell(selectedCell);

        for (Map.Entry<Cell, Button> entry : cellButtonMap.entrySet()) {
            Cell cell = entry.getKey();
            Button button = entry.getValue();
            int row = model.getRowOfCell(cell);
            int col = model.getColOfCell(cell);

            button.getStyleClass().removeAll("cell-selected", "cell-highlighted");

            if (cell == selectedCell) {
                button.getStyleClass().add("cell-selected");
            } else if (row == selectedRow || col == selectedCol ||
                    (row / 3 == selectedRow / 3 && col / 3 == selectedCol / 3)) {
                button.getStyleClass().add("cell-highlighted");
            }
        }
    }
}