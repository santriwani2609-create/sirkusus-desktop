package Model;

import java.sql.Date;

/**
 * Pertemuan 13: transaksi pendaftaran (menghubungkan peserta dan kelas).
 * Field nim / namaPeserta / namaKelas hanya untuk tampilan tabel (hasil JOIN).
 */
public class Pendaftaran {
    private int id;
    private int idPeserta;
    private int idKelas;
    private Date tanggalDaftar;
    private String status;

    // data tampilan hasil JOIN
    private String nim = "";
    private String namaPeserta = "";
    private String namaKelas = "";

    public Pendaftaran(int id, int idPeserta, int idKelas,
                       Date tanggalDaftar, String status) {
        setId(id);
        setIdPeserta(idPeserta);
        setIdKelas(idKelas);
        setTanggalDaftar(tanggalDaftar);
        setStatus(status);
    }

    public int getId() {
        return id;
    }

    public final void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID tidak boleh negatif");
        }
        this.id = id;
    }

    public int getIdPeserta() {
        return idPeserta;
    }

    public final void setIdPeserta(int idPeserta) {
        if (idPeserta <= 0) {
            throw new IllegalArgumentException("Peserta wajib dipilih");
        }
        this.idPeserta = idPeserta;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public final void setIdKelas(int idKelas) {
        if (idKelas <= 0) {
            throw new IllegalArgumentException("Kelas wajib dipilih");
        }
        this.idKelas = idKelas;
    }

    public Date getTanggalDaftar() {
        return tanggalDaftar;
    }

    public final void setTanggalDaftar(Date tanggalDaftar) {
        if (tanggalDaftar == null) {
            throw new IllegalArgumentException("Tanggal daftar wajib diisi");
        }
        this.tanggalDaftar = tanggalDaftar;
    }

    public String getStatus() {
        return status;
    }

    public final void setStatus(String status) {
        if (status == null || !(status.equals("AKTIF")
                || status.equals("SELESAI") || status.equals("BATAL"))) {
            throw new IllegalArgumentException(
                    "Status harus AKTIF, SELESAI, atau BATAL");
        }
        this.status = status;
    }

    public String getNim() {
        return nim;
    }

    public String getNamaPeserta() {
        return namaPeserta;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public void setTampilan(String nim, String namaPeserta, String namaKelas) {
        this.nim = nim == null ? "" : nim;
        this.namaPeserta = namaPeserta == null ? "" : namaPeserta;
        this.namaKelas = namaKelas == null ? "" : namaKelas;
    }
}
