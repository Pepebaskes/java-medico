package com.consultorio.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.consultorio.util.bdConexionLalo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RegistroDoctoresController {

    // 🔗 Referencia al controlador del menú
    private MenuControlUsuariosController menuController;

    public void setMenuController(MenuControlUsuariosController controller) {
        this.menuController = controller;
    }

    // 📝 Campos del formulario
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDomicilio;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtCarrera;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtCedula;
    @FXML private DatePicker dateNacimiento;

    // 🔘 Botones
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;
    @FXML private Button btnVolver;

    @FXML private AnchorPane fondo;

    // 🔄 Navegación por ENTER
    @FXML
    private void initialize() {
        if (txtNombre != null) txtNombre.setOnAction(e -> txtApellido.requestFocus());
        if (txtApellido != null) txtApellido.setOnAction(e -> txtDomicilio.requestFocus());
        if (txtDomicilio != null) txtDomicilio.setOnAction(e -> txtTelefono.requestFocus());
        if (txtTelefono != null) txtTelefono.setOnAction(e -> txtCorreo.requestFocus());
        if (txtCorreo != null) txtCorreo.setOnAction(e -> txtCarrera.requestFocus());
        if (txtCarrera != null) txtCarrera.setOnAction(e -> txtCedula.requestFocus());
        if (txtCedula != null) txtCedula.setOnAction(e -> txtEspecialidad.requestFocus());
        if (txtEspecialidad != null) txtEspecialidad.setOnAction(e -> dateNacimiento.requestFocus());
        if (fondo != null) {
            fondo.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER -> {
                        if (btnRegistrar != null) btnRegistrar.fire();
                    }
                    default -> {}
                }
            });
            Platform.runLater(() -> fondo.requestFocus());
        }
    }

    // 💾 Registrar doctor en MySQL
    @FXML
    private void registrarDoctor(ActionEvent event) {
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
            mostrarAlertaInfo("Registro exitoso", "El doctor fue registrado correctamente.");

            if (menuController != null) {
                menuController.cargarUsuariosDesdeFuera();
            }

            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error al registrar", e.getMessage());
        }
    }

    // 🧼 Limpiar campos tras registro
    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtDomicilio.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtCarrera.clear();
        txtCedula.clear();
        txtEspecialidad.clear();
        if (dateNacimiento != null) dateNacimiento.setValue(null);
    }

    // ▶ Abre menú principal
    @FXML
    private void abrirMenuUsuarios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menuControlUsuarios.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Menú Control de Usuarios");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            mostrarAlerta("No se pudo abrir menuControlUsuarios.fxml", "Verifica que esté en /fxml/");
        }
    }

    // ▶ Abre formulario desde menú
    @FXML
    private void abrirFormularioRegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularioRegistroUsuarios.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Formulario Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            mostrarAlerta("No se pudo abrir formularioRegistroUsuarios.fxml", "Verifica que esté en /fxml/");
        }
    }

    // ⛔ Cancelar formulario
    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    // ⬅ Volver al menú sin crear nueva ventana
    @FXML
    private void volverAlMenu(ActionEvent event) {
        Stage ventanaActual = (Stage) btnVolver.getScene().getWindow();
        ventanaActual.close();

        if (menuController != null) {
            menuController.cargarUsuariosDesdeFuera();
        }
    }

    // 📢 Alertas
    private void mostrarAlerta(String header, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(header);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlertaInfo(String header, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Éxito");
        alerta.setHeaderText(header);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}