package com.satoshi.sudoku.entities;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private Integer value;
    private boolean fixed;
    private boolean locked;
    private Set<Integer> drafts;

    public Cell(Integer value, boolean fixed) {
        this.value = value;
        this.fixed = fixed;
        this.locked = false;
        this.drafts = new HashSet<>();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        if (!fixed && !locked) {
            this.value = value;
            if (value != null && value != 0) {
                this.drafts.clear();
            }
        }
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        if (!fixed) {
            this.locked = locked;
        }
    }

    public void clearCell() {
        setValue(null);
        clearDrafts();
    }

    public Set<Integer> getDrafts() {
        return drafts;
    }

    public void addDraft(int draft) {
        if (value == null || value == 0) {
            drafts.add(draft);
        }
    }

    public void clearDrafts() {
        if (!fixed) {
            drafts.clear();
        }
    }
}
