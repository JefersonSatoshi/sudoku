package com.satoshi.sudoku.entities;

public class Cell {
    private Integer value;
    private boolean fixed;

    public Cell(Integer value) {
        this.value = value;
        this.fixed = false;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        if (!fixed) {
            this.value = value;
        }
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public void clearCell() {
        setValue(null);
    }
}
