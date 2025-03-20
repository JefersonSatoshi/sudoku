package com.satoshi.sudoku.model;

import com.satoshi.sudoku.entities.Board;
import com.satoshi.sudoku.entities.Cell;

import java.util.ArrayList;
import java.util.List;

public class SudokuModel {

    private Board board;
    private Cell[][] solutionGrid;

    public SudokuModel(Board board, Cell[][] solutionGrid) {
        this.board = board;
        this.solutionGrid = solutionGrid;
    }

    public List<Cell> getConflictingCells(int row, int col, int value) {
        List<Cell> conflicts = new ArrayList<>();

        Cell[] rowCells = board.getRow(row);
        for (int c = 0; c < 9; c++) {
            if (c != col) {
                Integer cellValue = rowCells[c].getValue();
                if (cellValue != null && cellValue == value) {
                    conflicts.add(rowCells[c]);
                }
            }
        }

        Cell[] colCells = board.getColumn(col);
        for (int r = 0; r < 9; r++) {
            if (r != row) {
                Integer cellValue = colCells[r].getValue();
                if (cellValue != null && cellValue == value) {
                    conflicts.add(colCells[r]);
                }
            }
        }

        int gridRow = row / 3;
        int gridCol = col / 3;
        Cell[][] subGrid = board.getSubGrid(gridRow, gridCol);
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                int globalR = gridRow * 3 + r;
                int globalC = gridCol * 3 + c;
                if (globalR != row || globalC != col) {
                    Integer cellValue = subGrid[r][c].getValue();
                    if (cellValue != null && cellValue == value) {
                        conflicts.add(subGrid[r][c]);
                    }
                }
            }
        }
        return conflicts;
    }

    public boolean isBoardFull() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Integer value = board.getCell(row, col).getValue();
                if (value == null || value == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGameFinished() {
        if (!isBoardFull()) {
            return false;
        }

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Integer boardValue = board.getCell(row, col).getValue();
                Integer solutionValue = solutionGrid[row][col].getValue();
                if (!boardValue.equals(solutionValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean matchesSolution(int row, int col, int value) {
        Integer solutionValue = solutionGrid[row][col].getValue();
        return solutionValue != null && solutionValue == value;
    }

    public void setValue(int row, int col, int value) {
        Cell cell = board.getCell(row, col);
        if (!cell.isFixed()) {
            cell.setValue(value);
        }
    }

    public void reset() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!board.getCell(row, col).isFixed()) {
                    Cell cell = board.getCell(row, col);
                    cell.setLocked(false);
                    board.getCell(row, col).clearCell();
                }
            }
        }
    }

    public Board getBoard() {
        return board;
    }
}
