package database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_FILE_NAME = "restoran.db";
    private static Connection connection;
    private static String dbUrl;

    public static synchronized Connection getConnection() throws SQLException {
        try {
            // Load SQLite JDBC Driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC Driver tidak ditemukan. Pastikan sqlite-jdbc.jar ada di classpath.", e);
        }

        if (connection == null || connection.isClosed()) {
            try {
                connection = openAndValidateConnection();
            } catch (SQLException e) {
                if (isInvalidDatabaseFile(e)) {
                    Path databasePath = resolveDatabasePath();
                    try {
                        Files.deleteIfExists(databasePath);
                    } catch (Exception deleteError) {
                        throw new SQLException("Gagal memperbaiki file database yang rusak: " + databasePath, deleteError);
                    }
                    connection = openAndValidateConnection();
                } else {
                    throw e;
                }
            }
            System.out.println("Koneksi database berhasil dibuat: " + resolveDatabasePath().toAbsolutePath());
        }
        return connection;
    }

    private static Connection openAndValidateConnection() throws SQLException {
        Connection candidate = DriverManager.getConnection(getDatabaseUrl());

        try (Statement statement = candidate.createStatement()) {
            statement.executeQuery("SELECT name FROM sqlite_master LIMIT 1");
            statement.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            try {
                candidate.close();
            } catch (SQLException closeError) {
                e.addSuppressed(closeError);
            }
            throw e;
        }

        return candidate;
    }

    private static String getDatabaseUrl() {
        if (dbUrl == null) {
            dbUrl = "jdbc:sqlite:" + resolveDatabasePath().toAbsolutePath().toString().replace('\\', '/');
        }
        return dbUrl;
    }

    private static Path resolveDatabasePath() {
        Path current = Paths.get("").toAbsolutePath();

        while (current != null) {
            Path candidate = current.resolve("database").resolve(DB_FILE_NAME);
            Path parent = candidate.getParent();
            if (parent != null && Files.exists(parent)) {
                return candidate;
            }
            current = current.getParent();
        }

        Path fallback = Paths.get("").toAbsolutePath().resolve("database").resolve(DB_FILE_NAME);
        try {
            Files.createDirectories(fallback.getParent());
        } catch (Exception e) {
            throw new IllegalStateException("Tidak dapat membuat folder database.", e);
        }
        return fallback;
    }

    private static boolean isInvalidDatabaseFile(SQLException exception) {
        String message = exception.getMessage();
        return message != null && (message.contains("SQLITE_NOTADB") || message.contains("file is not a database"));
    }

    public static synchronized void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Koneksi database ditutup!");
            }
            connection = null;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
