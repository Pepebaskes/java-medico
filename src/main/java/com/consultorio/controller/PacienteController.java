package com.consultorio.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.consultorio.util.bdConexion;

public class PacienteController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDomicilio;
    @FXML private TextField txtTelefono;

    @FXML
    private void guardarPaciente() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String domicilio = txtDomicilio.getText();
        String telefono = txtTelefono.getText();

        String sql = "INSERT INTO pacientes(nombre, apellido, domicilio, telefono) VALUES (?, ?, ?, ?)";

        try (Connection conn = bdConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, domicilio);
            stmt.setString(4, telefono);
            stmt.executeUpdate();

            mostrarAlerta("Éxito", "Paciente guardado correctamente.", Alert.AlertType.INFORMATION);
            cancelarRegistro(); // Limpia los campos

        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo guardar el paciente:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cancelarRegistro() {
        txtNombre.clear();
        txtApellido.clear();
        txtDomicilio.clear();
        txtTelefono.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}