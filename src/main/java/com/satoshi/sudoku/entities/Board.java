package com.satoshi.sudoku.entities;

public class Board {

    private Cell[][] grid;

    public Board() {
        this.grid = new Cell[9][9];
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                grid[col][row] = new Cell(0, false);
            }
        }
        System.out.println("board inicializado");
    }

    public void setCellValue(int col, int row, Integer value, boolean isFixed) {
        grid[row][col].setValue(value);
        grid[row][col].setFixed(isFixed);
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    public Cell[] getRow(int row) {
        return grid[row];
    }

    public Cell[] getColumn(int col) {
        Cell[] column = new Cell[9];
        for (int i = 0; i < 9; i++) {
            column[i] = grid[i][col];
        }
        return column;
    }

    public Cell[][] getSubGrid(int gridRow, int gridCol) {
        Cell[][] subGrid = new Cell[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                subGrid[row][col] = grid[gridRow * 3 + row][gridCol * 3 + col];
            }
        }
        return subGrid;
    }

    public void initializeBoardFromArgs(String[] args) {
        for (String arg : args) {
            String[] parts = arg.split(";");
            String[] coords = parts[0].split(",");
            int col = Integer.parseInt(coords[0]);
            int row = Integer.parseInt(coords[1]);

            // Correção: separar o valor do booleano
            String[] valueAndState = parts[1].split(",");
            int value = Integer.parseInt(valueAndState[0]);
            boolean isFixed = Boolean.parseBoolean(valueAndState[1]);

            if (isFixed) {
                setCellValue(col, row, value, isFixed);
            } else {
                setCellValue(col, row, 0, isFixed);
            }
        }
    }

    public boolean clearValue(int row, int col){
        var cell = grid[col][row];
        if (cell.isFixed()){
            return false;
        }

        cell.clearCell();
        return true;
    }

    public void reset() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                    grid[row][col].clearCell();
            }
        }
    }
}
