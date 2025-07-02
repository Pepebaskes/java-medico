package com.consultorio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application { 
    public static void main(String[] args) {
        launch(args);  // Esto arranca JavaFX
    }
/* @Override
    public void start(Stage stage) throws Exception {
        // Carga tu interfaz de registro de citas
        URL fxmlURL = getClass().getResource("/fxml/ModuloConsultarCitas.fxml");

        if (fxmlURL == null) {
            throw new RuntimeException("No se encontró el archivo RegistroCitas.fxml en la ruta /fxml/");
        }

        Parent root = FXMLLoader.load(fxmlURL);
        Scene scene = new Scene(root);

        stage.setTitle("Agenda de Citas - Prueba");
        stage.setScene(scene);
        stage.show();
    }
*/
@Override
public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/fxml/menuControlUsuarios.fxml"));

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("🚧 Prueba Formulario Registro");
    stage.show();
}

}
