package com.consultorio.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import com.consultorio.util.bdConexion;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModuloRegistrarConsultaController {

    @FXML private TextField textFieldNombre;
    @FXML private TextField textFieldApellido;
    @FXML private TextField textFieldLocalidad;
    @FXML private TextField textFieldTelefono;
    @FXML private DatePicker fechaConsulta;
    @FXML private ComboBox<String> cbHora;
    @FXML private ComboBox<String> cbMinutos;
    @FXML private Button btnGuardar;
    @FXML private Button btnRegresar;

    @FXML
    private void initialize() {
        for (int h = 7; h <= 20; h++) cbHora.getItems().add(String.format("%02d", h));
        for (int m = 0; m < 60; m += 5) cbMinutos.getItems().add(String.format("%02d", m));

        btnGuardar.setOnAction(e -> guardarCita());
        btnRegresar.setOnAction(e -> ((Stage) btnRegresar.getScene().getWindow()).close());
    }

    private void guardarCita() {
        String nombre = textFieldNombre.getText();
        String apellido = textFieldApellido.getText();
        String localidad = textFieldLocalidad.getText();
        String telefono = textFieldTelefono.getText();
        LocalDate fecha = fechaConsulta.getValue();
        String hora = cbHora.getValue();
        String minutos = cbMinutos.getValue();

        if (nombre == null || apellido == null || fecha == null || hora == null || minutos == null) {
            System.out.println("⚠️ Completa todos los campos requeridos.");
            return;
        }

        String horaCompleta = hora + ":" + minutos;

        try (Connection conn = bdConexion.getConnection()) {
            String sql = "INSERT INTO citas (nombre_paciente, apellido, fechaConsulta, horaConsulta, localidad, telefono, asistencia) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setDate(3, java.sql.Date.valueOf(fecha));
            stmt.setString(4, horaCompleta);
            stmt.setString(5, localidad);
            stmt.setString(6, telefono);
            stmt.setBoolean(7, false);

            stmt.executeUpdate();
            System.out.println("✅ Cita guardada correctamente.");
            ((Stage) btnGuardar.getScene().getWindow()).close();

        } catch (SQLException ex) {
            System.err.println("❌ Error al guardar en BD: " + ex.getMessage());
        }
    }
}