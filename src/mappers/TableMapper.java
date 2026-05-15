package mappers;


import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Table;

public class TableMapper {

    public int insert(Table table) throws SQLException {
        String sql = "INSERT INTO restaurant_table (nomor_meja, kapasitas, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, table.getNomorMeja());
            pstmt.setInt(2, table.getKapasitas());
            pstmt.setString(3, table.getStatus());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Table findById(int id) throws SQLException {
        String sql = "SELECT * FROM restaurant_table WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTable(rs);
                }
            }
        }
        return null;
    }

    public List<Table> findAll() throws SQLException {
        List<Table> tableList = new ArrayList<>();
        String sql = "SELECT * FROM restaurant_table";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tableList.add(mapResultSetToTable(rs));
            }
        }
        return tableList;
    }

    public List<Table> findByStatus(String status) throws SQLException {
        List<Table> tableList = new ArrayList<>();
        String sql = "SELECT * FROM restaurant_table WHERE status = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tableList.add(mapResultSetToTable(rs));
                }
            }
        }
        return tableList;
    }

    public void update(Table table) throws SQLException {
        String sql = "UPDATE restaurant_table SET nomor_meja = ?, kapasitas = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, table.getNomorMeja());
            pstmt.setInt(2, table.getKapasitas());
            pstmt.setString(3, table.getStatus());
            pstmt.setInt(4, table.getId());

            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM restaurant_table WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Table mapResultSetToTable(ResultSet rs) throws SQLException {
        return new Table(
                rs.getInt("id"),
                rs.getInt("nomor_meja"),
                rs.getInt("kapasitas"),
                rs.getString("status")
        );
    }
}
