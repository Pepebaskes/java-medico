package com.consultorio.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.consultorio.model.Usuario;
import com.consultorio.util.bdConexionLalo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarUsuarioController {

    private Usuario usuario;
    private MenuControlUsuariosController menuController;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtDomicilio;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCarrera;
    @FXML private TextField txtCedula;
    @FXML private DatePicker dateNacimiento;

    @FXML private Button btnGuardar;
    @FXML private Button btnEliminar;
    @FXML private Button btnCancelar;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarDatosEnCampos();
    }

    public void setMenuController(MenuControlUsuariosController controller) {
        this.menuController = controller;
    }

    private void cargarDatosEnCampos() {
        if (usuario != null) {
            txtNombre.setText(usuario.getNombre());
            txtApellido.setText(usuario.getApellido());
            txtCorreo.setText(usuario.getCorreo());
            txtEspecialidad.setText(usuario.getEspecialidad());
            txtDomicilio.setText(usuario.getDomicilio());
            txtTelefono.setText(usuario.getTelefono());
            txtCarrera.setText(usuario.getCarrera());
            txtCedula.setText(usuario.getCedula());
            dateNacimiento.setValue(usuario.getFechaNacimiento());
        }
    }

    @FXML
    private void guardarCambios() {
        if (usuario == null) return;

        String sql = "UPDATE usuarios SET nombre=?, apellido=?, correo=?, especialidad=?, domicilio=?, telefono=?, carrera=?, cedula=?, fecha_nacimiento=? WHERE id=?";

        try (Connection con = bdConexionLalo.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtApellido.getText());
            ps.setString(3, txtCorreo.getText());
            ps.setString(4, txtEspecialidad.getText());
            ps.setString(5, txtDomicilio.getText());
            ps.setString(6, txtTelefono.getText());
            ps.setString(7, txtCarrera.getText());
            ps.setString(8, txtCedula.getText());
            ps.setDate(9, java.sql.Date.valueOf(dateNacimiento.getValue()));
            ps.setInt(10, usuario.getId());

            ps.executeUpdate();
            mostrarAlertaInfo("Actualización exitosa", "Los datos han sido actualizados correctamente.");

            if (menuController != null) {
                menuController.cargarUsuariosDesdeFuera();
            }

            cerrarVentana();
        } catch (SQLException e) {
            mostrarAlertaError("Error al actualizar usuario", e.getMessage());
        }
    }

    @FXML
    private void eliminarUsuario() {
        if (usuario == null) return;

        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection con = bdConexionLalo.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuario.getId());
            ps.executeUpdate();
            mostrarAlertaInfo("Usuario eliminado", "El usuario ha sido eliminado correctamente.");

            if (menuController != null) {
                menuController.cargarUsuariosDesdeFuera();
            }

            cerrarVentana();
        } catch (SQLException e) {
            mostrarAlertaError("Error al eliminar usuario", e.getMessage());
        }
    }

    @FXML
    private void cancelarEdicion() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlertaInfo(String header, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
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