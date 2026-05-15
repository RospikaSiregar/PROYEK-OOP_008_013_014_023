package mappers;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Customer;

public class CustomerMapper {

    public int insert(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (nama, no_telepon, email, alamat) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, customer.getNama());
            pstmt.setString(2, customer.getNoTelepon());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getAlamat());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Customer findById(int id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null;
    }

    public List<Customer> findAll() throws SQLException {
        List<Customer> customerList = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customerList.add(mapResultSetToCustomer(rs));
            }
        }
        return customerList;
    }

    public List<Customer> findByNama(String nama) throws SQLException {
        List<Customer> customerList = new ArrayList<>();
        String sql = "SELECT * FROM customer WHERE nama LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nama + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customerList.add(mapResultSetToCustomer(rs));
                }
            }
        }
        return customerList;
    }

    public void update(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET nama = ?, no_telepon = ?, email = ?, alamat = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getNama());
            pstmt.setString(2, customer.getNoTelepon());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getAlamat());
            pstmt.setInt(5, customer.getId());

            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM customer WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        return new Customer(
                rs.getInt("id"),
                rs.getString("nama"),
                rs.getString("no_telepon"),
                rs.getString("email"),
                rs.getString("alamat")
        );
    }
}
