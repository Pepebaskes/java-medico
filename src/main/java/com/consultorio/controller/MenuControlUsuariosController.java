package com.consultorio.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;

import com.consultorio.model.Usuario;
import com.consultorio.util.bdConexionLalo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuControlUsuariosController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, Integer> colEdad;
    @FXML private TableColumn<Usuario, String> colCedula;
    @FXML private TextField txtFiltro;

    private ObservableList<Usuario> listaCompleta = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));

        cargarUsuariosDesdeFuera();

        txtFiltro.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltro(newVal));
    }

    public void cargarUsuariosDesdeFuera() {
        listaCompleta = obtenerUsuariosDirectamente();
        tablaUsuarios.setItems(listaCompleta);
    }

    public ObservableList<Usuario> obtenerUsuariosDirectamente() {
        ObservableList<Usuario> lista = FXCollections.observableArrayList();
        String sql = "SELECT id, nombre, apellido, correo, especialidad, domicilio, telefono, carrera, cedula, fecha_nacimiento FROM usuarios";

        try (Connection con = bdConexionLalo.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setCorreo(rs.getString("correo"));
                u.setEspecialidad(rs.getString("especialidad"));
                u.setDomicilio(rs.getString("domicilio"));
                u.setTelefono(rs.getString("telefono"));
                u.setCarrera(rs.getString("carrera"));
                u.setCedula(rs.getString("cedula"));
                u.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());

                lista.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }

    @FXML
    private void editarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Sin selección", "Selecciona un usuario antes de editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularioEditarUsuario.fxml"));
            Parent root = loader.load();

            EditarUsuarioController editarController = loader.getController();
            editarController.setUsuario(seleccionado);
            editarController.setMenuController(this);

            Stage stage = new Stage();
            stage.setTitle("Editar Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            mostrarAlerta("Error al abrir edición", e.getMessage());
        }
    }

    private void aplicarFiltro(String texto) {
        String filtro = texto.trim().toLowerCase();
        ObservableList<Usuario> filtrado = FXCollections.observableArrayList();

        for (Usuario u : listaCompleta) {
            boolean coincideNombre = u.getNombre().toLowerCase().contains(filtro);
            boolean coincideApellido = u.getApellido().toLowerCase().contains(filtro);
            boolean coincideId = String.valueOf(u.getId()).contains(filtro);
            boolean coincideEdad = String.valueOf(u.getEdad()).contains(filtro);
            boolean coincideCedula = u.getCedula().toLowerCase().contains(filtro);

            if (coincideNombre || coincideApellido || coincideId || coincideEdad || coincideCedula) {
                filtrado.add(u);
            }
        }

        tablaUsuarios.setItems(filtrado);
    }

    private void mostrarAlerta(String header, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Advertencia");
        alerta.setHeaderText(header);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @FXML
    private void abrirFormularioRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/formularioRegistroUsuarios.fxml"));
            Parent root = loader.load();

            RegistroDoctoresController registroController = loader.getController();
            registroController.setMenuController(this);

            Stage stage = new Stage();
            stage.setTitle("Formulario Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error al abrir formularioRegistroUsuarios.fxml: " + e.getMessage());
        }
    }
}