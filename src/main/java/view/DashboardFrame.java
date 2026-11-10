package view;

import Model.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import util.UserSession;
import util.WindowManager;

/**
 * Lobby aplikasi: menu Master, Transaksi, Laporan, Keluar.
 * Menu disesuaikan dengan role user yang tersimpan di UserSession.
 */
public class DashboardFrame extends JFrame {

    private static final Color KUNING_HEADER = new Color(247, 207, 95);
    private static final Color KREM_LATAR = new Color(255, 251, 235);
    private static final Color KREM_PANEL = new Color(255, 253, 244);
    private static final Color GARIS = new Color(230, 200, 110);
    private static final Color TEKS_GELAP = new Color(90, 64, 0);
    private static final Color ABU = new Color(161, 136, 127);

    // ===== Status =====
    private final JLabel lblUser = new JLabel("-");
    private final JLabel lblRole = new JLabel("-");

    // ===== Menu =====
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu menuMaster = new JMenu("Master");
    private final JMenuItem miPeserta = new JMenuItem("Peserta");
    private final JMenuItem miInstruktur = new JMenuItem("Instruktur");
    private final JMenuItem miKursus = new JMenuItem("Kursus");
    private final JMenuItem miKelas = new JMenuItem("Kelas");

    private final JMenu menuTransaksi = new JMenu("Transaksi");
    private final JMenuItem miPendaftaran = new JMenuItem("Pendaftaran");
    private final JMenuItem miPembayaran = new JMenuItem("Pembayaran");

    private final JMenu menuLaporan = new JMenu("Laporan");
    private final JMenuItem miLaporanPeserta = new JMenuItem("Laporan Peserta");
    private final JMenuItem miLaporanPendaftaran = new JMenuItem("Laporan Pendaftaran");

    private final JMenu menuKeluar = new JMenu("Keluar");
    private final JMenuItem miLogout = new JMenuItem("Logout");
    private final JMenuItem miTutup = new JMenuItem("Tutup Aplikasi");

    // ===== Popup menu =====
    private final JPopupMenu popupDashboard = new JPopupMenu();
    private final JMenuItem miTentang = new JMenuItem("Tentang Aplikasi");
    private final JMenuItem miRefresh = new JMenuItem("Refresh");

    public DashboardFrame() {
        setTitle("Dashboard - SIKURSUS Desktop");
        // Dashboard = jendela utama, jadi X boleh menutup aplikasi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(KREM_LATAR);
        setLayout(new BorderLayout());

        initMenu();
        initKonten();
        initEvent();

        setSize(820, 520);
        setLocationRelativeTo(null);
        applySession();
    }

    // ------------------------------------------------------------------ UI

    private void initMenu() {
        menuMaster.add(miPeserta);
        menuMaster.add(miInstruktur);
        menuMaster.add(miKursus);
        menuMaster.add(miKelas);

        menuTransaksi.add(miPendaftaran);
        menuTransaksi.add(miPembayaran);

        menuLaporan.add(miLaporanPeserta);
        menuLaporan.add(miLaporanPendaftaran);

        menuKeluar.add(miLogout);
        menuKeluar.addSeparator();
        menuKeluar.add(miTutup);

        menuBar.add(menuMaster);
        menuBar.add(menuTransaksi);
        menuBar.add(menuLaporan);
        menuBar.add(menuKeluar);
        setJMenuBar(menuBar);

        // Icon dari resource project (portable), bukan path komputer pribadi
        miPeserta.setIcon(loadIcon("/icons/peserta.png"));
        miInstruktur.setIcon(loadIcon("/icons/instruktur.png"));
        miKursus.setIcon(loadIcon("/icons/kursus.png"));
        miKelas.setIcon(loadIcon("/icons/kelas.png"));

        popupDashboard.add(miTentang);
        popupDashboard.add(miRefresh);
    }

    private void initKonten() {
        // ---- Header ----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(KUNING_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel lblJudul = new JLabel("SIKURSUS Desktop");
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblJudul.setForeground(TEKS_GELAP);

        JPanel status = new JPanel(new GridLayout(2, 1));
        status.setOpaque(false);
        lblUser.setForeground(TEKS_GELAP);
        lblRole.setForeground(TEKS_GELAP);
        lblUser.setHorizontalAlignment(SwingConstants.RIGHT);
        lblRole.setHorizontalAlignment(SwingConstants.RIGHT);
        status.add(lblUser);
        status.add(lblRole);

        header.add(lblJudul, BorderLayout.WEST);
        header.add(status, BorderLayout.EAST);

        // ---- Area dashboard (klik kanan = popup menu) ----
        JPanel area = new JPanel(new BorderLayout());
        area.setBackground(KREM_PANEL);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16),
                BorderFactory.createLineBorder(GARIS)));
        JLabel lblSelamat = new JLabel(
                "<html><center><h2>Selamat datang di SIKURSUS</h2>"
                + "Pilih menu <b>Master</b>, <b>Transaksi</b>, atau <b>Laporan</b> di bagian atas.<br><br>"
                + "<font color='#A1887F'>Klik kanan di area ini untuk menu cepat.</font></center></html>",
                SwingConstants.CENTER);
        lblSelamat.setForeground(TEKS_GELAP);
        area.add(lblSelamat, BorderLayout.CENTER);
        area.setComponentPopupMenu(popupDashboard);
        // label menutupi panel, jadi popup juga dipasang di label
        lblSelamat.setComponentPopupMenu(popupDashboard);

        add(header, BorderLayout.NORTH);
        add(area, BorderLayout.CENTER);
    }

    private void initEvent() {
        miPeserta.addActionListener(e -> WindowManager.open(FormPeserta.class, FormPeserta::new));
        miInstruktur.addActionListener(e -> WindowManager.open(InstrukturFrame.class, InstrukturFrame::new));
        miKursus.addActionListener(e -> WindowManager.open(KursusFrame.class, KursusFrame::new));
        miKelas.addActionListener(e -> WindowManager.open(KelasFrame.class, KelasFrame::new));

        miPendaftaran.addActionListener(e -> info(
                "Modul Pendaftaran sedang dikembangkan pada tahap project berikutnya."));
        miPembayaran.addActionListener(e -> info(
                "Modul Pembayaran sedang dikembangkan pada tahap project berikutnya."));
        miLaporanPeserta.addActionListener(e -> info(
                "Modul laporan diaktifkan pada Pertemuan 12 dengan Jaspersoft Studio."));
        miLaporanPendaftaran.addActionListener(e -> info(
                "Modul laporan diaktifkan pada Pertemuan 12 dengan Jaspersoft Studio."));

        miLogout.addActionListener(e -> miLogoutActionPerformed());
        miTutup.addActionListener(e -> miTutupActionPerformed());

        miTentang.addActionListener(e -> info(
                "SIKURSUS Desktop\nProject PBO - Program Studi Informatika"));
        miRefresh.addActionListener(e -> {
            applySession();
            repaint();
        });
    }

    // ------------------------------------------------------------- Session

    /** Membaca UserSession lalu mengatur label dan hak akses menu sesuai role. */
    private void applySession() {
        User user = UserSession.getCurrentUser();

        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "Session tidak ditemukan. Silakan login ulang.");
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            return;
        }

        lblUser.setText("User: " + user.getUsername());
        lblRole.setText("Role: " + user.getRole());

        boolean admin = user.isAdmin();
        miInstruktur.setEnabled(admin);
        miKursus.setEnabled(admin);
        miKelas.setEnabled(admin);

        miPeserta.setEnabled(true);
        menuTransaksi.setEnabled(true);
        menuLaporan.setEnabled(true);
    }

    // --------------------------------------------------------------- Event

    private void miLogoutActionPerformed() {
        int pilihan = JOptionPane.showConfirmDialog(this,
                "Logout dari aplikasi?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);

        if (pilihan == JOptionPane.YES_OPTION) {
            WindowManager.closeAll();   // 1. tutup semua form child
            UserSession.clear();        // 2. hapus session
            dispose();                  // 3. tutup dashboard
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true)); // 4. kembali ke login
        }
    }

    private void miTutupActionPerformed() {
        int pilihan = JOptionPane.showConfirmDialog(this,
                "Tutup aplikasi SIKURSUS?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (pilihan == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ------------------------------------------------------------- Helper

    private void info(String pesan) {
        JOptionPane.showMessageDialog(this, pesan);
    }

    /** Memuat icon dari classpath (src/main/resources/icons). Null jika tidak ada. */
    private ImageIcon loadIcon(String path) {
        URL url = getClass().getResource(path);
        return url == null ? null : new ImageIcon(url);
    }
}
