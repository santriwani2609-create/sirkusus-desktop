package Model;

public class Kursus {
    private String kode;
    private String nama;
    private String level;
    private double biaya;

    public Kursus() {
    }

    public Kursus(String kode, String nama, String level, double biaya) {
        setKode(kode);
        setNama(nama);
        setLevel(level);
        setBiaya(biaya);
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode kursus wajib diisi");
        }
        this.kode = kode.trim();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama kursus wajib diisi");
        }
        this.nama = nama.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = (level == null) ? "" : level.trim();
    }

    public double getBiaya() {
        return biaya;
    }

    public void setBiaya(double biaya) {
        if (biaya < 0) {
            throw new IllegalArgumentException("Biaya tidak boleh negatif");
        }
        this.biaya = biaya;
    }

    public double hitungBiayaSetelahDiskon(double persentaseDiskon) {
        if (persentaseDiskon < 0) persentaseDiskon = 0;
        if (persentaseDiskon > 100) persentaseDiskon = 100;
        
        double potongan = this.biaya * (persentaseDiskon / 100.0);
        return this.biaya - potongan;
    }
}