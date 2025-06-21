package com.consultorio.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class bdConexion {
    private static final String URL = "jdbc:mysql://localhost:3306/bdconsultas";
    private static final String USER = "root";       // Cambia por tu usuario MySQL
    private static final String PASSWORD = "root"; // Cambia por tu contraseña

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}