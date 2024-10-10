package org.devsystem.TicketPrinterAPI.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/parking_system"; // Cambia el nombre de la base de datos
    private static final String USER = "root"; // Cambia a tu usuario
    private static final String PASSWORD = ""; // Cambia a tu contraseña

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return null; // O lanza una excepción personalizada
        }
    }
}
