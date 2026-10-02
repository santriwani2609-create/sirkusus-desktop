package model;

/**
 * Parent class (Pertemuan 5).
 * Menyimpan data umum yang dimiliki Peserta dan Instruktur.
 */
public class Orang {

    private int id;
    private String nama;
    private String noHp;

    // Constructor parent: dipanggil oleh child melalui super(...)
    public Orang(int id, String nama, String noHp) {
        this.id = id;
        this.nama = nama;
        this.noHp = noHp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getInfo() {
        return "ID: " + id
                + " | Nama: " + nama
                + " | No. HP: " + noHp;
    }
}