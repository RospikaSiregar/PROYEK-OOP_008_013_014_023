package model;

public class Menu extends Entity {
    private String nama;
    private String deskripsi;
    private String kategori;
    private double harga;
    private int stok;

    public Menu() {
        super();
    }

    public Menu(int id, String nama, String deskripsi, String kategori, double harga, int stok) {
        super(id);
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", kategori='" + kategori + '\'' +
                ", harga=" + harga +
                ", stok=" + stok +
                '}';
    }
}
