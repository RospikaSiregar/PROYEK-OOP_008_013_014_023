package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order extends Entity {
    private int customerId;
    private int tableId;
    private LocalDateTime tanggalPesanan;
    private String status;
    private double totalHarga;
    private final List<OrderItem> orderItems;

    public Order() {
        super();
        this.orderItems = new ArrayList<>();
    }

    public Order(int id, int customerId, int tableId, LocalDateTime tanggalPesanan, String status, double totalHarga) {
        super(id);
        this.customerId = customerId;
        this.tableId = tableId;
        this.tanggalPesanan = tanggalPesanan;
        this.status = status;
        this.totalHarga = totalHarga;
        this.orderItems = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public LocalDateTime getTanggalPesanan() {
        return tanggalPesanan;
    }

    public void setTanggalPesanan(LocalDateTime tanggalPesanan) {
        this.tanggalPesanan = tanggalPesanan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
    }

    public void removeOrderItem(OrderItem item) {
        this.orderItems.remove(item);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", tableId=" + tableId +
                ", tanggalPesanan=" + tanggalPesanan +
                ", status='" + status + '\'' +
                ", totalHarga=" + totalHarga +
                ", itemCount=" + orderItems.size() +
                '}';
    }
}
