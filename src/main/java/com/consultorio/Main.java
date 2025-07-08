package com.consultorio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application { 
    public static void main(String[] args) {
        launch(args);
    }
/* @Override
    public void start(Stage stage) throws Exception {

        // Cargar el archivo correcto
        URL fxmlURL = getClass().getResource("/fxml/menuPrincipal.fxml");
>>>>>>> 389a64a872785a6a6e89e362f0ef9ba2ad914c89

        if (fxmlURL == null) {
            throw new RuntimeException("No se encontró el archivo menuPrincipal en la ruta /fxml/");
        }

        Parent root = FXMLLoader.load(fxmlURL);
        Scene scene = new Scene(root);

        stage.setTitle("Menú Principal");
        stage.setScene(scene);
        stage.show();
    }
<<<<<<< HEAD
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
