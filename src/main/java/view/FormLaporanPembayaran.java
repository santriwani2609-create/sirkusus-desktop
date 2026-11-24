package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import report.ReportService;

/**
 * Pertemuan 13 (latihan lanjut): pilih rentang tanggal lalu tampilkan laporan pembayaran.
 */
public class FormLaporanPembayaran extends JFrame {

    private static final Color KUNING_HEADER = new Color(247, 207, 95);
    private static final Color KUNING_TOMBOL = new Color(244, 193, 62);
    private static final Color KREM_LATAR = new Color(255, 251, 235);
    private static final Color KREM_PANEL = new Color(255, 253, 244);
    private static final Color GARIS = new Color(230, 200, 110);
    private static final Color TEKS_GELAP = new Color(90, 64, 0);
    private static final Color ABU = new Color(161, 136, 127);

    private final JTextField txtTanggalAwal = new JTextField(14);
    private final JTextField txtTanggalAkhir = new JTextField(14);
    private final JButton btnTampilkan = new JButton("Tampilkan Laporan");
    private final JButton btnTutup = new JButton("Tutup");

    public FormLaporanPembayaran() {
        setTitle("Laporan Pembayaran - SIKURSUS Desktop");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(KREM_LATAR);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(KUNING_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(18, 10, 14, 10));
        JLabel lblJudul = new JLabel("Laporan Pembayaran", SwingConstants.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblJudul.setForeground(TEKS_GELAP);
        JLabel lblSub = new JLabel("Pilih rentang tanggal pembayaran", SwingConstants.CENTER);
        lblSub.setForeground(TEKS_GELAP);
        header.add(lblJudul, BorderLayout.CENTER);
        header.add(lblSub, BorderLayout.SOUTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(KREM_PANEL);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(16, 24, 16, 24),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GARIS),
                        BorderFactory.createEmptyBorder(16, 18, 16, 18))));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        form.add(new JLabel("Tanggal Awal"), c);
        c.gridx = 1; c.weightx = 1;
        form.add(txtTanggalAwal, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0;
        form.add(new JLabel("Tanggal Akhir"), c);
        c.gridx = 1; c.weightx = 1;
        form.add(txtTanggalAkhir, c);

        JLabel lblFormat = new JLabel("Format: yyyy-MM-dd (contoh 2026-11-01)");
        lblFormat.setForeground(ABU);
        c.gridx = 1; c.gridy = 2;
        form.add(lblFormat, c);

        styleTombol(btnTampilkan, KUNING_TOMBOL, TEKS_GELAP);
        styleTombol(btnTutup, ABU, Color.WHITE);
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelTombol.setOpaque(false);
        panelTombol.add(btnTampilkan);
        panelTombol.add(btnTutup);
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        c.insets = new Insets(14, 6, 0, 6);
        form.add(panelTombol, c);

        add(header, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);

        // nilai awal: tanggal 1 bulan ini s/d hari ini
        LocalDate hariIni = LocalDate.now();
        txtTanggalAwal.setText(hariIni.withDayOfMonth(1).toString());
        txtTanggalAkhir.setText(hariIni.toString());

        btnTampilkan.addActionListener(e -> btnTampilkanActionPerformed());
        btnTutup.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnTampilkan);

        pack();
        setMinimumSize(new Dimension(480, getHeight()));
        setLocationRelativeTo(null);
    }

    private void styleTombol(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker()),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // Tahap 8F: validasi tanggal sebelum report dipanggil
    private void btnTampilkanActionPerformed() {
        String awalText = txtTanggalAwal.getText().trim();
        String akhirText = txtTanggalAkhir.getText().trim();

        if (awalText.isEmpty()) {
            peringatan("Tanggal awal tidak boleh kosong.", txtTanggalAwal);
            return;
        }
        if (akhirText.isEmpty()) {
            peringatan("Tanggal akhir tidak boleh kosong.", txtTanggalAkhir);
            return;
        }

        LocalDate awal;
        LocalDate akhir;
        try {
            awal = LocalDate.parse(awalText);
        } catch (DateTimeParseException ex) {
            peringatan("Tanggal awal tidak valid. Gunakan format yyyy-MM-dd.", txtTanggalAwal);
            return;
        }
        try {
            akhir = LocalDate.parse(akhirText);
        } catch (DateTimeParseException ex) {
            peringatan("Tanggal akhir tidak valid. Gunakan format yyyy-MM-dd.", txtTanggalAkhir);
            return;
        }
        if (awal.isAfter(akhir)) {
            peringatan("Tanggal awal tidak boleh setelah tanggal akhir.", txtTanggalAwal);
            return;
        }

        ReportService.cetakPembayaran(Date.valueOf(awal), Date.valueOf(akhir));
    }

    private void peringatan(String pesan, JTextField fokus) {
        JOptionPane.showMessageDialog(this, pesan, "Validasi",
                JOptionPane.WARNING_MESSAGE);
        fokus.requestFocus();
    }
}
