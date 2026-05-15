package mappers;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.OrderItem;

public class OrderItemMapper {

    public int insert(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO order_item (order_id, menu_id, menu_nama, kuantitas, harga_satuan, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, orderItem.getOrderId());
            pstmt.setInt(2, orderItem.getMenuId());
            pstmt.setString(3, orderItem.getMenuNama());
            pstmt.setInt(4, orderItem.getKuantitas());
            pstmt.setDouble(5, orderItem.getHargaSatuan());
            pstmt.setDouble(6, orderItem.getSubtotal());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    public OrderItem findById(int id) throws SQLException {
        String sql = "SELECT * FROM order_item WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrderItem(rs);
                }
            }
        }
        return null;
    }

    public List<OrderItem> findByOrderId(int orderId) throws SQLException {
        List<OrderItem> itemList = new ArrayList<>();
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    itemList.add(mapResultSetToOrderItem(rs));
                }
            }
        }
        return itemList;
    }

    public void update(OrderItem orderItem) throws SQLException {
        String sql = "UPDATE order_item SET order_id = ?, menu_id = ?, menu_nama = ?, kuantitas = ?, harga_satuan = ?, subtotal = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderItem.getOrderId());
            pstmt.setInt(2, orderItem.getMenuId());
            pstmt.setString(3, orderItem.getMenuNama());
            pstmt.setInt(4, orderItem.getKuantitas());
            pstmt.setDouble(5, orderItem.getHargaSatuan());
            pstmt.setDouble(6, orderItem.getSubtotal());
            pstmt.setInt(7, orderItem.getId());

            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM order_item WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        return new OrderItem(
                rs.getInt("id"),
                rs.getInt("order_id"),
                rs.getInt("menu_id"),
                rs.getString("menu_nama"),
                rs.getInt("kuantitas"),
                rs.getDouble("harga_satuan")
        );
    }
}
