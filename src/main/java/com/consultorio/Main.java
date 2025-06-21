package com.consultorio;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args); 
    }

    @Override
    public void start(Stage stage) throws Exception {
    com.consultorio.controller.PacienteController test = new com.consultorio.controller.PacienteController();   
    URL location = getClass().getResource("/fxml/pacientes.fxml");
    System.out.println("¿FXML encontrado? " + location);

    if (location == null) {
        throw new RuntimeException("No se encontró el archivo pacientes.fxml");
    }

    Parent root = FXMLLoader.load(location);
    Scene scene = new Scene(root);
    stage.setTitle("Registro de Pacientes");
    stage.setScene(scene);
    stage.show();
}


}