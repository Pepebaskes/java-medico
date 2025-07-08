package com.consultorio.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.consultorio.util.bdConexion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModuloRegistrarConsultaController {

    @FXML private TextField textFieldNombre;
    @FXML private TextField textFieldApellido;
    @FXML private DatePicker fechaConsulta;
    @FXML private ComboBox<String> cbHora;
    @FXML private ComboBox<String> cbMinutos;
    @FXML private TextField textFieldLocalidad;
    @FXML private TextField textFieldTelefono;
    @FXML private Button btnGuardar;
    @FXML private Button btnRegresar;

    @FXML
    public void initialize() {
        // items del combo box de hora y minutos
        for (int i = 0; i < 24; i++) {
            cbHora.getItems().add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i += 1) {
            cbMinutos.getItems().add(String.format("%02d", i));
        }
            //acciones de botones guardar y regresar 
        btnGuardar.setOnAction(e -> guardarCita());
        btnRegresar.setOnAction(e -> ((Stage) btnRegresar.getScene().getWindow()).close());
    }

    private void guardarCita() {
        String nombre = textFieldNombre.getText();
        String apellido = textFieldApellido.getText();
        String fecha = (fechaConsulta.getValue() != null) ? fechaConsulta.getValue().toString() : "";
        String hora = cbHora.getValue();
        String minutos = cbMinutos.getValue();
        String localidad = textFieldLocalidad.getText();
        String telefono = textFieldTelefono.getText();
        String horaCompleta = (hora != null && minutos != null) ? hora + ":" + minutos : "";

        if (nombre.isEmpty() || apellido.isEmpty() || fecha.isEmpty() || horaCompleta.isEmpty() || localidad.isEmpty()) {
            mostrarAlerta("Todos los campos deben estar llenos.");
            return;
        }

        String sql = "INSERT INTO citas (nombre_paciente, apellido, fechaConsulta, horaConsulta, localidad, telefono, asistencia) " +
                     "VALUES (?, ?, ?, ?, ?, ?, false)";

        try (Connection conn = bdConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

                //hay que revisar el orden de los campos porque al presionar tab me brinca, no va en orden
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, fecha);
            stmt.setString(4, horaCompleta);
            stmt.setString(5, localidad);
            stmt.setString(6, telefono);

            stmt.executeUpdate();

            mostrarAlerta("Cita guardada correctamente.");
            ((Stage) btnGuardar.getScene().getWindow()).close(); // cerrar la ventana 

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error al guardar en la base de datos.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
