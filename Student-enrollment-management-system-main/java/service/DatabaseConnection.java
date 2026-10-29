package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root"; // Default XAMPP username
    private static final String PASSWORD = "";     // Default XAMPP password is empty

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database Connection Failed: " + e.getMessage());
            return null;
        }
    }
}