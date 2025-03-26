package com.satoshi.sudoku.controllers;

import com.satoshi.sudoku.entities.Cell;
import com.satoshi.sudoku.model.SudokuModel;
import com.satoshi.sudoku.util.Alerts;
import com.satoshi.sudoku.view.SudokuBoardView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameViewController implements Initializable {

    private SudokuModel model;
    private SudokuBoardView boardView;
    private Cell selectedCell;
    private Map<Cell, Button> cellButtonMap = new HashMap<>();
    private boolean draftMode = false;

    @FXML protected GridPane gridPane;
    @FXML private Button btRestart, btClear, btNote, btFinish;
    @FXML private Button btOne, btTwo, btThree, btFour, btFive, btSix, btSeven, btEight, btNine;
    @FXML private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (model == null) {
            throw new IllegalStateException("Model is not set before initialization.");
        }
        boardView = new SudokuBoardView(gridPane, model, cellButtonMap);
        configureButtonEvents();
        updateView();
        errorLabel.setText("Erros: " + model.getErrorCount() + "/" + model.getMaxErrors());

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

    public void setModel(SudokuModel model) {
        this.model = model;
    }

    private void configureButtonEvents() {
        for (Map.Entry<Cell, Button> entry : cellButtonMap.entrySet()) {
            Cell cell = entry.getKey();
            Button button = entry.getValue();
            button.setOnAction(e -> selectCell(model.getRowOfCell(cell), model.getColOfCell(cell)));
        }
    }

    public void selectCell(int row, int col) {
        selectedCell = model.getBoard().getCell(row, col);
        updateView();
    }

    public void insertNumber(int value) {
        if (selectedCell != null && !selectedCell.isFixed() && !selectedCell.isLocked()) {
            int row = model.getRowOfCell(selectedCell);
            int col = model.getColOfCell(selectedCell);
            if (draftMode) {
                model.addDraftToCell(row, col, value);
            } else {
                model.setValue(row, col, value);
                if (model.hasReachedMaxErrors()) {
                    Alerts.showAlert("Você Perdeu!", null, "Você atingiu o limite de erros (" + model.getMaxErrors() + "). O jogo terminou!", Alert.AlertType.ERROR);
                    disableOrEnableGameControls(true);
                }
            }
            errorLabel.setText("Erros: " + model.getErrorCount() + "/" + model.getMaxErrors());
            updateView();
        }
    }

    @FXML
    public void onBtRestart() {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmar", "Deseja Reiniciar o jogo?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            model.reset();
            selectedCell = null;
            draftMode = false;
            errorLabel.setText("Erros: " + model.getErrorCount() + "/" + model.getMaxErrors());
            updateView();
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
        if (draftMode) {
            btNote.getStyleClass().add("button-note");
        } else {
            btNote.getStyleClass().remove("button-note");
        }
    }

    @FXML
    public void onBtClear() {
        if (selectedCell != null && !selectedCell.isFixed()) {
            int row = model.getRowOfCell(selectedCell);
            int col = model.getColOfCell(selectedCell);
            Cell cell = model.getBoard().getCell(row, col);
            cell.clearDrafts();
            cell.setValue(null);
            cell.setLocked(false);
            model.getCellsWithError().remove(row + "," + col);
            updateView();
        }
    }

    private void updateView() {
        boardView.updateView(selectedCell);
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
}