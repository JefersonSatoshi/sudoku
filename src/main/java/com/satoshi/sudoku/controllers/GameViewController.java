package com.satoshi.sudoku.controllers;

import com.satoshi.sudoku.entities.Board;
import com.satoshi.sudoku.entities.Cell;
import com.satoshi.sudoku.model.SudokuModel;
import com.satoshi.sudoku.util.Alerts;
import com.satoshi.sudoku.util.SudokuGridUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class GameViewController implements Initializable {

    private SudokuModel model;
    private SudokuGridUtil gridUtil;
    private Cell selectedCell;
    private Map<Cell, Button> cellButtonMap = new HashMap<>();
    private boolean draftMode = false;

    @FXML
    protected GridPane gridPane;
    @FXML
    private Button btRestart, btClear, btNote, btFinish;
    @FXML
    private Button btOne, btTwo, btThree, btFour, btFive, btSix, btSeven, btEight, btNine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Board board = new Board();

        Cell[][] solutionGrid = new Cell[9][9];
        int[][] solution = {
                {4, 7, 9, 5, 8, 6, 2, 3, 1},
                {1, 3, 5, 4, 7, 2, 8, 9, 6},
                {2, 6, 8, 9, 1, 3, 7, 4, 5},
                {5, 1, 3, 7, 6, 4, 9, 8, 2},
                {8, 9, 7, 1, 2, 5, 3, 6, 4},
                {6, 4, 2, 3, 9, 8, 1, 5, 7},
                {7, 5, 4, 2, 3, 9, 6, 1, 8},
                {9, 8, 1, 6, 4, 7, 5, 2, 3},
                {3, 2, 6, 8, 5, 1, 4, 7, 9}
        };
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                solutionGrid[r][c] = new Cell(solution[r][c], false);
            }
        }

        String[] args = {
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

        board.initializeBoardFromArgs(args);
        model = new SudokuModel(board, solutionGrid);

        gridUtil = new SudokuGridUtil(board, this, cellButtonMap);
        gridUtil.populateSudokuGrid(gridPane);
        updateAllCellStyles(); // Garante que os números fixos apareçam de primeira

        btOne.setOnAction(e -> insertNumber(1));
        btTwo.setOnAction(e -> insertNumber(2));
        btThree.setOnAction(e -> insertNumber(3));
        btFour.setOnAction(e -> insertNumber(4));
        btFive.setOnAction(e -> insertNumber(5));
        btSix.setOnAction(e -> insertNumber(6));
        btSeven.setOnAction(e -> insertNumber(7));
        btEight.setOnAction(e -> insertNumber(8));
        btNine.setOnAction(e -> insertNumber(9));
    }

    @FXML
    public void onBtRestart() {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deseja Reiniciar o jogo?");
        if (result.get() == ButtonType.OK) {
            model.reset();
            selectedCell = null;
            draftMode = false;
            btNote.setStyle("");
            updateAllCellStyles();
            updateCellHighlighting();
            disableOrEnableGameControls(false);
        }
    }

    @FXML
    public void onBtFinishGame() {
        if (model.isGameFinished()) {
            Alerts.showAlert("Parabéns!", null, "Você completou o Sudoku corretamente!", Alert.AlertType.INFORMATION);
            disableOrEnableGameControls(true);
        } else if (!model.isBoardFull()) {
            Alerts.showAlert("Ops!", null, "Ainda tem células vazias! Preencha todas antes de concluir.", Alert.AlertType.WARNING);
        } else {
            Alerts.showAlert("Ops!", null, "O tabuleiro está cheio, mas tem erros. Corrija os números em vermelho!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void onBtNote() {
        draftMode = !draftMode;
        btNote.setStyle(draftMode ? "-fx-background-color: rgba(0, 255, 0, 0.5);" : "");
    }

    @FXML
    public void onBtClear() {
        if (selectedCell != null && !selectedCell.isFixed()) {
            selectedCell.clearDrafts();
            selectedCell.setValue(null);
            updateAllCellStyles();
        }
    }

    private void disableOrEnableGameControls(boolean option) {
        btClear.setDisable(option);
        btNote.setDisable(option);
        btOne.setDisable(option);
        btTwo.setDisable(option);
        btThree.setDisable(option);
        btFour.setDisable(option);
        btFive.setDisable(option);
        btSix.setDisable(option);
        btSeven.setDisable(option);
        btEight.setDisable(option);
        btNine.setDisable(option);
        gridPane.setDisable(option);
    }

    public void selectCell(int row, int col) {
        selectedCell = model.getBoard().getCell(row, col);
        updateCellHighlighting();
    }

    public void insertNumber(int value) {
        if (selectedCell != null && !selectedCell.isFixed()) {
            int row = getRowOfCell(selectedCell);
            int col = getColOfCell(selectedCell);
            if (draftMode) {
                model.getBoard().getCell(row, col).addDraft(value);
            } else {
                selectedCell.clearDrafts();
                updateAllCellStyles();
                selectedCell.setValue(value); // Atualiza o estado da célula localmente
                model.setValue(row, col, value);
            }
            updateAllCellStyles();
            updateCellHighlighting();
        }
    }

    private void updateAllCellStyles() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Cell cell = model.getBoard().getCell(r, c);
                Button button = cellButtonMap.get(cell);
                if (button == null) continue;

                Integer value = cell.getValue();
                if (value != null && value != 0) {
                    button.setText(String.valueOf(value));
                    button.setGraphic(null);
                    List<Cell> conflicts = model.getConflictingCells(r, c, value);
                    if (!conflicts.isEmpty() || !model.matchesSolution(r, c, value)) {
                        button.setStyle("-fx-background-color: rgba(255, 0, 0, 0.8); -fx-background-insets: 0.5;");
                    } else {
                        cell.setLocked(true);
                        button.setStyle("");
                    }
                } else if (!cell.getDrafts().isEmpty()) {
                    GridPane draftGrid = new GridPane();
                    draftGrid.setAlignment(Pos.CENTER);
                    draftGrid.setPadding(new Insets(1, 1, 1, 1)); // Padding interno
                    draftGrid.setPrefSize(20, 20); // Define o tamanho preferencial
                    draftGrid.setMinSize(20, 20); // Garante o tamanho mínimo

                    // Define 3 colunas e 3 linhas com 33.3% cada, centralizadas
                    for (int i = 0; i < 3; i++) {
                        ColumnConstraints col = new ColumnConstraints();
                        col.setPercentWidth(33.3);
                        col.setHalignment(HPos.CENTER); // Centraliza horizontalmente
                        draftGrid.getColumnConstraints().add(col);

                        RowConstraints row = new RowConstraints();
                        row.setPercentHeight(33.3);
                        row.setValignment(VPos.CENTER); // Centraliza verticalmente
                        draftGrid.getRowConstraints().add(row);
                    }

                    for (int draft : cell.getDrafts()) {
                        Text draftText = new Text(String.valueOf(draft));
                        draftText.setStyle("-fx-font-size: 12px;"); // Fonte 10px como tu pediu
                        switch (draft) {
                            case 1: draftGrid.add(draftText, 0, 0); break; // Superior esquerdo
                            case 2: draftGrid.add(draftText, 1, 0); break; // Superior centro
                            case 3: draftGrid.add(draftText, 2, 0); break; // Superior direito
                            case 4: draftGrid.add(draftText, 0, 1); break; // Centro esquerdo
                            case 5: draftGrid.add(draftText, 1, 1); break; // Centro
                            case 6: draftGrid.add(draftText, 2, 1); break; // Centro direito
                            case 7: draftGrid.add(draftText, 0, 2); break; // Inferior esquerdo
                            case 8: draftGrid.add(draftText, 1, 2); break; // Inferior centro
                            case 9: draftGrid.add(draftText, 2, 2); break; // Inferior direito
                        }
                    }
                    button.setText("");
                    button.setGraphic(draftGrid);
                    button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // Garante que o graphic não afete o texto
                    button.setStyle("");
                } else {
                    button.setText("");
                    button.setGraphic(null);
                    button.setContentDisplay(ContentDisplay.CENTER); // Volta pro padrão
                    button.setStyle("");
                }
            }
        }
    }

    private void updateCellHighlighting() {
        if (selectedCell == null) {
            return; // Não reseta estilos aqui pra manter os vermelhos
        }

        int selectedRow = getRowOfCell(selectedCell);
        int selectedCol = getColOfCell(selectedCell);

        for (Map.Entry<Cell, Button> entry : cellButtonMap.entrySet()) {
            Cell cell = entry.getKey();
            Button button = entry.getValue();
            int row = getRowOfCell(cell);
            int col = getColOfCell(cell);

            // Só aplica destaque se não tiver vermelho
            String currentStyle = button.getStyle();
            if (!currentStyle.contains("rgba(255, 0, 0, 0.8)")) {
                if (cell == selectedCell) {
                    button.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1);");
                } else if (row == selectedRow || col == selectedCol ||
                        (row / 3 == selectedRow / 3 && col / 3 == selectedCol / 3)) {
                    button.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2);");
                } else {
                    button.setStyle("");
                }
            }
        }
    }

    private int getRowOfCell(Cell cell) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (model.getBoard().getCell(r, c) == cell) return r;
            }
        }
        return -1;
    }

    private int getColOfCell(Cell cell) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (model.getBoard().getCell(r, c) == cell) return c;
            }
        }
        return -1;
    }
}