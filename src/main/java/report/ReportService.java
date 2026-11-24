package report;

import config.Database;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Pertemuan 12-13: semua pemanggilan report Jasper lewat satu pintu.
 * Alur: Connection -> muat report dari resource -> isi dengan Map parameter -> JasperViewer.
 */
public final class ReportService {

    private ReportService() {}

    // ---------------------------------------------------------- Pertemuan 12
    public static void tampilkanLaporanPeserta() {
        tampilkan("laporan_peserta", new HashMap<String, Object>(),
                "Data peserta belum ada.");
    }

    // ---------------------------------------------------------- Pertemuan 13
    /** Laporan satu pendaftaran. Nama key HARUS sama dengan parameter di JRXML. */
    public static void cetakPendaftaran(int idPendaftaran) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_ID_PENDAFTARAN", idPendaftaran);   // int -> Integer (autoboxing)
        tampilkan("laporan_pendaftaran", params, "Data laporan tidak ditemukan.");
    }

    /** Laporan pembayaran pada rentang tanggal (keduanya java.sql.Date). */
    public static void cetakPembayaran(Date tanggalAwal, Date tanggalAkhir) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_TANGGAL_AWAL", tanggalAwal);
        params.put("P_TANGGAL_AKHIR", tanggalAkhir);
        tampilkan("laporan_pembayaran", params,
                "Tidak ada transaksi pembayaran pada periode tersebut.");
    }

    // ----------------------------------------------------------------- inti
    private static void tampilkan(String namaReport,
                                  Map<String, Object> params,
                                  String pesanKosong) {
        try (Connection conn = Database.getConnection()) {
            JasperPrint print = isiReport(namaReport, params, conn);

            if (print.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(null, pesanKosong,
                        "Laporan", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // false = menutup viewer TIDAK menutup seluruh aplikasi
            JasperViewer.viewReport(print, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Gagal membuka laporan: " + ex.getMessage(),
                    "Laporan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static JasperPrint isiReport(String namaReport,
                                         Map<String, Object> params,
                                         Connection conn) throws Exception {
        // Jalur utama: file .jasper hasil compile Jaspersoft Studio
        InputStream jasper = ReportService.class
                .getResourceAsStream("/report/" + namaReport + ".jasper");
        if (jasper != null) {
            try (InputStream in = jasper) {
                return JasperFillManager.fillReport(in, params, conn);
            }
        }
        // Cadangan: .jasper belum ada -> compile .jrxml langsung saat dijalankan
        InputStream jrxml = ReportService.class
                .getResourceAsStream("/report/" + namaReport + ".jrxml");
        if (jrxml == null) {
            throw new IllegalStateException("File " + namaReport
                    + ".jasper / .jrxml tidak ditemukan di src/main/resources/report/");
        }
        try (InputStream in = jrxml) {
            JasperReport report = JasperCompileManager.compileReport(in);
            return JasperFillManager.fillReport(report, params, conn);
        }
    }
}
