package models;

public class OrderItem extends Entity {
    private int orderId;
    private int menuId;
    private String menuNama;
    private int kuantitas;
    private double hargaSatuan;
    private double subtotal;

    public OrderItem() {
        super();
    }

    public OrderItem(int id, int orderId, int menuId, String menuNama, int kuantitas, double hargaSatuan) {
        super(id);
        this.orderId = orderId;
        this.menuId = menuId;
        this.menuNama = menuNama;
        this.kuantitas = kuantitas;
        this.hargaSatuan = hargaSatuan;
        this.subtotal = kuantitas * hargaSatuan;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuNama() {
        return menuNama;
    }

    public void setMenuNama(String menuNama) {
        this.menuNama = menuNama;
    }

    public int getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(int kuantitas) {
        this.kuantitas = kuantitas;
        this.subtotal = kuantitas * hargaSatuan;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(double hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
        this.subtotal = kuantitas * hargaSatuan;
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", menuNama='" + menuNama + '\'' +
                ", kuantitas=" + kuantitas +
                ", hargaSatuan=" + hargaSatuan +
                ", subtotal=" + subtotal +
                '}';
    }
}
