package com.consultorio.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.consultorio.model.Cita;
import com.consultorio.util.bdConexion;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;


public class ModuloConsultarCitaController implements Initializable {

    

    // labels del formulario
    @FXML private TextField textFieldID;
    @FXML private TextField textFieldNombre;
    @FXML private TextField textFieldApellido;
    @FXML private TextField textFieldLocalidad;
    @FXML private TextField textFieldTelefono;
    @FXML private DatePicker fechaConsulta;
    @FXML private ComboBox<String> cbHora;
    @FXML private ComboBox<String> cbMinutos;

    // botones
    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnRegresar;

    // seleccion de la cita
    private Cita citaActual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // combobox de la hora y los minuitos
        cbHora.setItems(FXCollections.observableArrayList("08", "09", "10", "11", "12", "13", "14", "15", "16"));
        cbMinutos.setItems(FXCollections.observableArrayList("00", "15", "30", "45"));

        // botones
        btnGuardar.setOnAction(e -> guardarCambios());
        btnEliminar.setOnAction(e -> eliminarCita());
        btnRegresar.setOnAction(e -> btnRegresar.getScene().getWindow().hide());
    }

    // recibe la cita
    public void setCitaSeleccionada(Cita cita) {
        if (cita == null) return;

        this.citaActual = cita;
        textFieldID.setText(String.valueOf(cita.getId()));
        textFieldNombre.setText(cita.getNombre());
        textFieldApellido.setText(cita.getApellido());
        textFieldLocalidad.setText(cita.getLocalidad());
        textFieldTelefono.setText(cita.getTelefono());

        if (cita.getFechaConsulta() != null)
            fechaConsulta.setValue(LocalDate.parse(cita.getFechaConsulta()));

        if (cita.getHoraConsulta() != null && cita.getHoraConsulta().contains(":")) {
            String[] partes = cita.getHoraConsulta().split(":");
            cbHora.setValue(partes[0]);
            cbMinutos.setValue(partes[1]);
        }
    }

    private void guardarCambios() {
         if (citaActual == null) return;

    String sql = "UPDATE citas SET nombre_paciente = ?, apellido = ?, localidad = ?, telefono = ?, fechaConsulta = ?, horaConsulta = ? WHERE id = ?";

    try (Connection conn = bdConexion.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, textFieldNombre.getText().trim());
        stmt.setString(2, textFieldApellido.getText().trim());
        stmt.setString(3, textFieldLocalidad.getText().trim());
        stmt.setString(4, textFieldTelefono.getText().trim());
        stmt.setString(5, fechaConsulta.getValue().toString());
        stmt.setString(6, cbHora.getValue() + ":" + cbMinutos.getValue());
        stmt.setInt(7, citaActual.getId());

        stmt.executeUpdate();

        mostrarAlerta("Cita actualizada correctamente.");
        btnRegresar.getScene().getWindow().hide();

    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlerta("Error al guardar los cambios en la base de datos.");
    }

    }

    private void eliminarCita() {
         if (citaActual == null) return;

    String sql = "DELETE FROM citas WHERE id = ?";

    try (Connection conn = bdConexion.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, citaActual.getId());
        stmt.executeUpdate();

        mostrarAlerta("Cita eliminada correctamente.");
        btnRegresar.getScene().getWindow().hide();

    } catch (Exception e) {
        e.printStackTrace();
        mostrarAlerta("Error al eliminar la cita.");
    }

        btnRegresar.getScene().getWindow().hide();
    }
    
    private void mostrarAlerta(String mensaje) {
    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    alerta.setTitle("Información");
    alerta.setHeaderText(null);
    alerta.setContentText(mensaje);
    alerta.showAndWait();
}
}