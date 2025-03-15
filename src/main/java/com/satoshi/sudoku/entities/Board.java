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
                grid[row][col] = new Cell(null); // começa com null
            }
        }
    }

    public void setCellValue(Integer value, int row, int col, boolean isFixed) {
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
            int row = Integer.parseInt(coords[0]);
            int col = Integer.parseInt(coords[1]);
            int value = Integer.parseInt(parts[1]);
            boolean isFixed = Boolean.parseBoolean(parts[2]);
            setCellValue(row, col, value, isFixed);
        }
    }

    public void printBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.print(grid[row][col].getValue() + " ");
            }
            System.out.println();
        }
    }
}
