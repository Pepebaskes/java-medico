package com.consultorio.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.consultorio.util.bdConexionLalo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class FormularioRegistroUsuariosController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDomicilio;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtCarrera;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEspecialidad;
    @FXML private DatePicker dateNacimiento;
    @FXML private Button btnRegistrar;

    @FXML
    private void registrarUsuario() {
        String sql = "INSERT INTO usuarios (nombre, apellido, domicilio, telefono, correo, carrera, cedula, especialidad, fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = bdConexionLalo.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtApellido.getText());
            ps.setString(3, txtDomicilio.getText());
            ps.setString(4, txtTelefono.getText());
            ps.setString(5, txtCorreo.getText());
            ps.setString(6, txtCarrera.getText());
            ps.setString(7, txtCedula.getText());
            ps.setString(8, txtEspecialidad.getText());
            ps.setDate(9, java.sql.Date.valueOf(dateNacimiento.getValue()));

            ps.executeUpdate();
            mostrarAlertaInfo("Registro exitoso", "Usuario almacenado correctamente.");
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlertaError("Error al registrar", e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtDomicilio.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtCarrera.clear();
        txtCedula.clear();
        txtEspecialidad.clear();
        dateNacimiento.setValue(null);
    }

    private void mostrarAlertaInfo(String header, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Éxito");
        alerta.setHeaderText(header);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlertaError(String header, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(header);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
