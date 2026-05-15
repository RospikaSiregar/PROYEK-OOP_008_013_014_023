package mappers;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Order;
import models.OrderItem;

public class OrderMapper {

    public int insert(Order order) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, table_id, status, total_harga) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, order.getCustomerId());
            pstmt.setInt(2, order.getTableId());
            pstmt.setString(3, order.getStatus());
            pstmt.setDouble(4, order.getTotalHarga());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Order findById(int id) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    // Load order items
                    order.getOrderItems().addAll(getOrderItems(id));
                    return order;
                }
            }
        }
        return null;
    }

    public List<Order> findAll() throws SQLException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                // Load order items untuk setiap order
                order.getOrderItems().addAll(getOrderItems(order.getId()));
                orderList.add(order);
            }
        }
        return orderList;
    }

    public List<Order> findByCustomerId(int customerId) throws SQLException {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.getOrderItems().addAll(getOrderItems(order.getId()));
                    orderList.add(order);
                }
            }
        }
        return orderList;
    }

    private List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_item WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToOrderItem(rs));
                }
            }
        }
        return items;
    }

    public void update(Order order) throws SQLException {
        String sql = "UPDATE orders SET customer_id = ?, table_id = ?, status = ?, total_harga = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, order.getCustomerId());
            pstmt.setInt(2, order.getTableId());
            pstmt.setString(3, order.getStatus());
            pstmt.setDouble(4, order.getTotalHarga());
            pstmt.setInt(5, order.getId());

            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("id"),
                rs.getInt("customer_id"),
                rs.getInt("table_id"),
                rs.getTimestamp("tanggal_pesanan").toLocalDateTime(),
                rs.getString("status"),
                rs.getDouble("total_harga")
        );
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
