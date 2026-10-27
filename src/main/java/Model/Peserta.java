package Model;

public class Peserta extends Orang {
    private String nim;
    private String prodi;
    private String jenisKelamin;   // BARU: "L" atau "P"
    private String email;          // BARU: boleh kosong

    // Konstruktor lama TIDAK diubah
    public Peserta(int id, String nama, String noHp, String nim, String prodi) {
        super(id, nama, noHp);
        setNim(nim);
        setProdi(prodi);
    }

    public String getNim() {
        return nim;
    }

    public final void setNim(String nim) {
        if (nim == null || nim.trim().isEmpty()) {
            throw new IllegalArgumentException("NIM wajib diisi");
        }
        this.nim = nim.trim();
    }

    public String getProdi() {
        return prodi;
    }

    public final void setProdi(String prodi) {
        if (prodi == null || prodi.trim().isEmpty()) {
            throw new IllegalArgumentException("Prodi wajib diisi");
        }
        this.prodi = prodi.trim();
    }

    // ===== BARU =====
    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public final void setJenisKelamin(String jenisKelamin) {
        if (jenisKelamin == null
                || !(jenisKelamin.equals("L") || jenisKelamin.equals("P"))) {
            throw new IllegalArgumentException("Jenis kelamin harus L atau P");
        }
        this.jenisKelamin = jenisKelamin;
    }

    public String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        this.email = (email == null) ? "" : email.trim();
    }
    // ================

    @Override
    public String getInfo() {
        return "PESERTA | " + super.getInfo()
                + " | NIM: " + getNim()
                + " | Prodi: " + getProdi();
    }
}