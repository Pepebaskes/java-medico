package com.consultorio.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.consultorio.model.Cita;
import com.consultorio.util.bdConexion;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class RegistroCitasController {

    @FXML private Button btnRegistrarCita;
    @FXML private Button btnConsultarCita;
    @FXML private Button btnEditarCita;
    @FXML private Button btnEliminarCita;
    @FXML private Button btnRegresarMenu;

    @FXML private TableView<Cita> tablaCitas;
    @FXML private TableColumn<Cita, Boolean> colAsistio;
    @FXML private TableColumn<Cita, String> tblPaciente;
    @FXML private TableColumn<Cita, String> tblFechaConsulta;
    @FXML private TableColumn<Cita, String> tblHoraConsulta;
    @FXML private TableColumn<Cita, String> tblLocalicad;
    @FXML private TableColumn<Cita, String> tblTelefono;

    private final ObservableList<Cita> citas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colAsistio.setCellValueFactory(cellData -> {
            Cita cita = cellData.getValue();
            SimpleBooleanProperty property = new SimpleBooleanProperty(cita.isAsistencia());
            property.addListener((obs, oldVal, newVal) -> {
                cita.setAsistencia(newVal);
                actualizarAsistenciaEnBD(cita.getId(), newVal);
            });
            return property;
        });

        colAsistio.setCellFactory(CheckBoxTableCell.forTableColumn(colAsistio));
        tblPaciente.setCellValueFactory(cita ->
            new SimpleStringProperty(cita.getValue().getNombre() + " " + cita.getValue().getApellido()));
        tblFechaConsulta.setCellValueFactory(new PropertyValueFactory<>("fechaConsulta"));
        tblHoraConsulta.setCellValueFactory(new PropertyValueFactory<>("horaConsulta"));
        tblLocalicad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        tblTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tablaCitas.setItems(citas);
        cargarCitasDesdeBD();

        btnRegistrarCita.setOnAction(e -> abrirFormularioRegistro());

        btnConsultarCita.setOnAction(e -> System.out.println("Consultar cita"));
        btnEditarCita.setOnAction(e -> System.out.println("Editar cita"));
        btnEliminarCita.setOnAction(e -> System.out.println("Eliminar cita"));
        btnRegresarMenu.setOnAction(e -> System.out.println("Regresar al menú"));
    }

    private void cargarCitasDesdeBD() {
        citas.clear();
        String sql = "SELECT id, nombre_paciente, apellido, fechaConsulta, horaConsulta, localidad, telefono, asistencia FROM citas";

        try (Connection conn = bdConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cita cita = new Cita(
                    rs.getInt("id"),
                    rs.getString("nombre_paciente"),
                    rs.getString("apellido"),
                    rs.getString("fechaConsulta"),
                    rs.getString("horaConsulta"),
                    rs.getString("localidad"),
                    rs.getString("telefono"),
                    rs.getBoolean("asistencia")
                );
                citas.add(cita);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void actualizarAsistenciaEnBD(int id, boolean asistencia) {
        String sql = "UPDATE citas SET asistencia = ? WHERE id = ?";

        try (Connection conn = bdConexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, asistencia);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void abrirFormularioRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/moduloRegistrarConsulta.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Registrar nueva cita");
            stage.setScene(new Scene(root));

            // Recarga citas al cerrar el formulario
            stage.setOnHidden(e -> cargarCitasDesdeBD());

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}