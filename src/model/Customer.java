package models;

public class Customer extends Entity {
    private String nama;
    private String noTelepon;
    private String email;
    private String alamat;

    public Customer() {
        super();
    }

    public Customer(int id, String nama, String noTelepon, String email, String alamat) {
        super(id);
        this.nama = nama;
        this.noTelepon = noTelepon;
        this.email = email;
        this.alamat = alamat;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", nama='" + nama + '\'' +
                ", noTelepon='" + noTelepon + '\'' +
                ", email='" + email + '\'' +
                ", alamat='" + alamat + '\'' +
                '}';
    }
}
