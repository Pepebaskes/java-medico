package com.consultorio.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class bdConexion {
    private static final String URL = "jdbc:sqlite:bdconsultas.db"; // Asegúrate de que esté en tu raíz del proyecto

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

}
