package mappers;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Menu;

public class MenuMapper {

    public int insert(Menu menu) throws SQLException {
        String sql = "INSERT INTO menu (nama, deskripsi, kategori, harga, stok) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, menu.getNama());
            pstmt.setString(2, menu.getDeskripsi());
            pstmt.setString(3, menu.getKategori());
            pstmt.setDouble(4, menu.getHarga());
            pstmt.setInt(5, menu.getStok());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Menu findById(int id) throws SQLException {
        String sql = "SELECT * FROM menu WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMenu(rs);
                }
            }
        }
        return null;
    }

    public List<Menu> findAll() throws SQLException {
        List<Menu> menuList = new ArrayList<>();
        String sql = "SELECT * FROM menu";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                menuList.add(mapResultSetToMenu(rs));
            }
        }
        return menuList;
    }

    public List<Menu> findByKategori(String kategori) throws SQLException {
        List<Menu> menuList = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE kategori = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, kategori);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    menuList.add(mapResultSetToMenu(rs));
                }
            }
        }
        return menuList;
    }

    public void update(Menu menu) throws SQLException {
        String sql = "UPDATE menu SET nama = ?, deskripsi = ?, kategori = ?, harga = ?, stok = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, menu.getNama());
            pstmt.setString(2, menu.getDeskripsi());
            pstmt.setString(3, menu.getKategori());
            pstmt.setDouble(4, menu.getHarga());
            pstmt.setInt(5, menu.getStok());
            pstmt.setInt(6, menu.getId());

            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM menu WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Menu mapResultSetToMenu(ResultSet rs) throws SQLException {
        return new Menu(
                rs.getInt("id"),
                rs.getString("nama"),
                rs.getString("deskripsi"),
                rs.getString("kategori"),
                rs.getDouble("harga"),
                rs.getInt("stok")
        );
    }
}
