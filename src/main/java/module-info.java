module com.satoshi.sudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls; // Certifique-se de que a dependência do ControlsFX está configurada

    exports com.satoshi.sudoku.application; // Tornar a classe pública disponível
    opens com.satoshi.sudoku.application to javafx.fxml; // Permitir acesso de reflexão ao pacote
    opens com.satoshi.sudoku.controllers to javafx.fxml;
}
