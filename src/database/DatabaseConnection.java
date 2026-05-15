package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:database/restoran.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        try {
            // Load SQLite JDBC Driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver tidak ditemukan!");
            System.err.println("Error: " + e.getMessage());
        }

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Koneksi database berhasil dibuat!");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Koneksi database ditutup!");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
