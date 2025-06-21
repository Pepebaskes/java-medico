package com.consultorio.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PacienteController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDomicilio;
    @FXML private TextField txtTelefono;

    @FXML
    private void guardarPaciente() {
        System.out.println("Paciente guardado (simulado)");
    }

    @FXML
    private void cancelarRegistro() {
        System.out.println("Registro cancelado");
    }
}
