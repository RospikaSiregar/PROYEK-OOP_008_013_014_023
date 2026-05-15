package model;

public class Table extends Entity {
    private int nomorMeja;
    private int kapasitas;
    private String status;

    public Table() {
        super();
    }

    public Table(int id, int nomorMeja, int kapasitas, String status) {
        super(id);
        this.nomorMeja = nomorMeja;
        this.kapasitas = kapasitas;
        this.status = status;
    }

    public int getNomorMeja() {
        return nomorMeja;
    }

    public void setNomorMeja(int nomorMeja) {
        this.nomorMeja = nomorMeja;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", nomorMeja=" + nomorMeja +
                ", kapasitas=" + kapasitas +
                ", status='" + status + '\'' +
                '}';
    }
}
