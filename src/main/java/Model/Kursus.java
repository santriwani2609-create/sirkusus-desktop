package model;

/**
 * Model domain Kursus (Pertemuan 4).
 * Class ini menyimpan data kursus dan aturan bisnis perhitungan biaya.
 * Class model TIDAK boleh mengakses JTextField, JOptionPane, dsb.
 */
public class Kursus {

    // Field / state object
    String kode;
    String nama;
    String level;
    double biaya;

    // Constructor kosong
    public Kursus() {
    }

    // Constructor lengkap (overload)
    public Kursus(String kode, String nama, String level, double biaya) {
        this.kode = kode;
        this.nama = nama;
        this.level = level;
        this.biaya = biaya;
    }

    // Behavior: persenDiskon ditulis 0 sampai 100 (10 berarti 10%)
    public double hitungBiayaSetelahDiskon(double persenDiskon) {
        return biaya - (biaya * persenDiskon / 100.0);
    }
}