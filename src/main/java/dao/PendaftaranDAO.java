package dao;

import Model.Opsi;
import Model.Pendaftaran;
import config.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PendaftaranDAO {

    // READ: daftar pendaftaran lengkap dengan NIM, nama peserta, dan nama kelas (JOIN)
    public List<Pendaftaran> findAll() throws SQLException {
        List<Pendaftaran> data = new ArrayList<>();
        String sql = "SELECT p.id_pendaftaran, p.id_peserta, p.id_kelas, "
                   + "p.tanggal_daftar, p.status, "
                   + "ps.nim, ps.nama AS nama_peserta, kls.nama_kelas "
                   + "FROM pendaftaran p "
                   + "INNER JOIN peserta ps ON p.id_peserta = ps.id_peserta "
                   + "INNER JOIN kelas kls ON p.id_kelas = kls.id_kelas "
                   + "ORDER BY p.id_pendaftaran";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pendaftaran p = new Pendaftaran(
                        rs.getInt("id_pendaftaran"),
                        rs.getInt("id_peserta"),
                        rs.getInt("id_kelas"),
                        rs.getDate("tanggal_daftar"),
                        rs.getString("status"));
                p.setTampilan(rs.getString("nim"),
                        rs.getString("nama_peserta"),
                        rs.getString("nama_kelas"));
                data.add(p);
            }
        }
        return data;
    }

    // CREATE
    public boolean insert(Pendaftaran p) throws SQLException {
        String sql = "INSERT INTO pendaftaran "
                   + "(id_peserta, id_kelas, tanggal_daftar, status) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getIdPeserta());
            ps.setInt(2, p.getIdKelas());
            ps.setDate(3, p.getTanggalDaftar());
            ps.setString(4, p.getStatus());
            return ps.executeUpdate() == 1;
        }
    }

    // DELETE (gagal jika pendaftaran sudah punya pembayaran -> foreign key)
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM pendaftaran WHERE id_pendaftaran=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Isi combo peserta: "NIM - Nama"
    public List<Opsi> findPesertaOpsi() throws SQLException {
        List<Opsi> data = new ArrayList<>();
        String sql = "SELECT id_peserta, nim, nama FROM peserta ORDER BY nama";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.add(new Opsi(rs.getInt("id_peserta"),
                        rs.getString("nim") + " - " + rs.getString("nama")));
            }
        }
        return data;
    }

    // Isi combo kelas: "Nama Kelas - Nama Kursus"
    public List<Opsi> findKelasOpsi() throws SQLException {
        List<Opsi> data = new ArrayList<>();
        String sql = "SELECT kls.id_kelas, kls.nama_kelas, krs.nama_kursus "
                   + "FROM kelas kls "
                   + "INNER JOIN kursus krs ON kls.id_kursus = krs.id_kursus "
                   + "ORDER BY kls.nama_kelas";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                data.add(new Opsi(rs.getInt("id_kelas"),
                        rs.getString("nama_kelas") + " - "
                        + rs.getString("nama_kursus")));
            }
        }
        return data;
    }
}
