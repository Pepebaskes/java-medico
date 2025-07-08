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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class RegistroCitasController {
    @FXML private TextField labelBuscarCita;

    @FXML private Button btnRegistrarCita;
    @FXML private Button btnConsultarCita;
    //@FXML private Button btnEditarCita;
    //@FXML private Button btnEliminarCita;
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
    private void irARegistroCita(ActionEvent event) {
        
         try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/moduloRegistrarCita.fxml"));
        Parent root = loader.load();

        Stage nuevaVentana = new Stage();
        nuevaVentana.setTitle("Registrar Consulta");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/estilos.css").toExternalForm());
        nuevaVentana.setScene(scene);

        nuevaVentana.initModality(Modality.APPLICATION_MODAL);
            
        // muestra y espera a que se cierre la ventana 
        nuevaVentana.showAndWait();

        // actualizar la tabla desde la bd 
        cargarCitasDesdeBD();

                } catch (IOException e) {
        e.printStackTrace();
             }

            }
    

    
        @FXML
    public void irAConsultarCita(ActionEvent event) {
    Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();

    if (citaSeleccionada == null) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Selección necesaria");
        alerta.setHeaderText(null);
        alerta.setContentText("Por favor selecciona una cita de la tabla.");
        alerta.showAndWait();
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/moduloConsultarCitas.fxml")); // ruta importante
        Parent root = loader.load();

        ModuloConsultarCitaController controller = loader.getController();
        controller.setCitaSeleccionada(citaSeleccionada);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/styles/estilos.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Consultar Cita");
        stage.showAndWait();

        cargarCitasDesdeBD();

    } catch (IOException e) {
        e.printStackTrace();
    }

}
    


    @FXML
    public void initialize() {
           // cargar datos dfe la bbase de datos
    cargarCitasDesdeBD();

    // configurar las columnas
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

    // crear el filtro para buscar las citas
    FilteredList<Cita> filteredData = new FilteredList<>(citas, p -> true);

    // esciuchar la busqueda
    labelBuscarCita.textProperty().addListener((obs, oldVal, newVal) -> {
        String texto = newVal.toLowerCase().trim();

        filteredData.setPredicate(cita -> {
            if (texto.isEmpty()) return true;

            return String.valueOf(cita.getId()).toLowerCase().contains(texto)
                || cita.getNombre().toLowerCase().contains(texto)
                || cita.getApellido().toLowerCase().contains(texto)
                || cita.getFechaConsulta().toLowerCase().contains(texto)
                || cita.getHoraConsulta().toLowerCase().contains(texto)
                || cita.getLocalidad().toLowerCase().contains(texto)
                || cita.getTelefono().toLowerCase().contains(texto);
        });
    });

    // ordenar los datos filtrados
    SortedList<Cita> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(tablaCitas.comparatorProperty());

    // darle los datos
    tablaCitas.setItems(sortedData);

    // orden de la fecha
    tablaCitas.getSortOrder().add(tblFechaConsulta);
    tblFechaConsulta.setSortType(TableColumn.SortType.DESCENDING);

    // boton regresar al menu
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

            // recarga citas al cerrar el formulario
            stage.setOnHidden(e -> cargarCitasDesdeBD());

            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}