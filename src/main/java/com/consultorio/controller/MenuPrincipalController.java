package com.consultorio.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuPrincipalController {

    @FXML
    private Button btnCItas;

    @FXML
    private void initialize() {
        
    }

    @FXML
    private void handleBtnCitas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registroCitas.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registro de Citas");
            stage.setScene(new Scene(root));
           // stage.getIcons().add(new Image("/com/consultorio/resources/icons/cita_icon.png")); // el icono dl usuario
            stage.show();

            // ocional: cerrar el menú principal si se quiere
            // ((Stage) btnCItas.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
