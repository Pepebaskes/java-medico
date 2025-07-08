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
        // Cargar el archivo correcto
        URL fxmlURL = getClass().getResource("/fxml/menuPrincipal.fxml");

        if (fxmlURL == null) {
            throw new RuntimeException("No se encontró el archivo menuPrincipal en la ruta /fxml/");
        }

        Parent root = FXMLLoader.load(fxmlURL);
        Scene scene = new Scene(root);

        stage.setTitle("Menú Principal");
        stage.setScene(scene);
        stage.show();
    }
}
