/*
package com.satoshi.sudoku.model;

import com.satoshi.sudoku.entities.Board;
import com.satoshi.sudoku.entities.Cell;

import java.util.ArrayList;
import java.util.List;

public class SudokuModel {

    private Board board;
    private Cell[][] solutionGrid;

    public SudokuModel(Board board, Cell[][] solutionGrid) {
        if (board == null || solutionGrid == null || solutionGrid.length != 9 || solutionGrid[0].length != 9) {
            throw new IllegalArgumentException("Tabuleiro ou solução inválidos");
        }
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
}*/

package com.satoshi.sudoku.model;

import com.satoshi.sudoku.entities.Board;
import com.satoshi.sudoku.entities.Cell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuModel {

    private Board board;
    private Cell[][] solutionGrid;
    private int errorCount = 0;
    private final int maxErrors = 3;
    private Set<String> cellsWithError = new HashSet<>();

    public SudokuModel(Board board, Cell[][] solutionGrid) {
        if (board == null || solutionGrid == null || solutionGrid.length != 9 || solutionGrid[0].length != 9) {
            throw new IllegalArgumentException("Tabuleiro ou solução inválidos");
        }
        this.board = board;
        this.solutionGrid = solutionGrid;
    }

    public void setValue(int row, int col, int value) {
        Cell cell = board.getCell(row, col);
        if (!cell.isFixed() && !cell.isLocked()) {
            String cellKey = row + "," + col;
            cellsWithError.remove(cellKey);

            cell.setValue(value);
            cell.clearDrafts();
            if (matchesSolution(row, col, value)) {
                cell.setLocked(true);
            } else {
                cell.setLocked(false);
                if (!cellsWithError.contains(cellKey)) {
                    errorCount++;
                    cellsWithError.add(cellKey);
                }
            }
        }
    }

    public void addDraftToCell(int row, int col, int value) {
        Cell cell = board.getCell(row, col);
        if (!cell.isFixed() && !cell.isLocked()) {
            cell.addDraft(value);
        }
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
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Integer boardValue = board.getCell(row, col).getValue();
                if (boardValue == null || !boardValue.equals(solutionGrid[row][col].getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean matchesSolution(int row, int col, int value) {
        return solutionGrid[row][col].getValue() == value;
    }

    public void reset() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = board.getCell(row, col);
                if (!cell.isFixed()) {
                    cell.setLocked(false);
                    cell.clearCell();
                }
            }
        }
        errorCount = 0;
        cellsWithError.clear();
    }

    public int getRowOfCell(Cell cell) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board.getCell(r, c) == cell) return r;
            }
        }
        return -1;
    }

    public int getColOfCell(Cell cell) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board.getCell(r, c) == cell) return c;
            }
        }
        return -1;
    }

    public Board getBoard() {
        return board;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public int getMaxErrors() {
        return maxErrors;
    }

    public Set<String> getCellsWithError() {
        return cellsWithError;
    }

    public boolean hasReachedMaxErrors() {
        return errorCount >= maxErrors;
    }
}