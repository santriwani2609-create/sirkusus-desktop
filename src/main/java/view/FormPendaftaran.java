package view;

import Model.Opsi;
import Model.Pendaftaran;
import dao.PendaftaranDAO;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import report.ReportService;

/**
 * Pertemuan 13: transaksi Pendaftaran (peserta + kelas) dan cetak laporan
 * per pendaftaran. ID laporan diambil dari baris JTable yang dipilih.
 */
public class FormPendaftaran extends JFrame {

    private static final Color KUNING_HEADER = new Color(247, 207, 95);
    private static final Color KUNING_TOMBOL = new Color(244, 193, 62);
    private static final Color KUNING_TABEL = new Color(255, 232, 150);
    private static final Color KUNING_PILIH = new Color(255, 236, 170);
    private static final Color KREM_LATAR = new Color(255, 251, 235);
    private static final Color KREM_PANEL = new Color(255, 253, 244);
    private static final Color GARIS = new Color(230, 200, 110);
    private static final Color TEKS_GELAP = new Color(90, 64, 0);
    private static final Color MERAH = new Color(229, 115, 115);
    private static final Color ABU = new Color(161, 136, 127);
    private static final Color HIJAU = new Color(102, 187, 106);

    // ===== Input =====
    private final JComboBox<Opsi> cbPeserta = new JComboBox<>();
    private final JComboBox<Opsi> cbKelas = new JComboBox<>();
    private final JTextField txtTanggal = new JTextField();
    private final JComboBox<String> cbStatus
            = new JComboBox<>(new String[]{"AKTIF", "SELESAI", "BATAL"});

    // ===== Tombol =====
    private final JButton btnSimpan = new JButton("Simpan");
    private final JButton btnHapus = new JButton("Hapus");
    private final JButton btnReset = new JButton("Reset");
    private final JButton btnCetakTerpilih = new JButton("Cetak Terpilih");

    private final JTable tblPendaftaran = new JTable();
    private final PendaftaranDAO dao = new PendaftaranDAO();

    public FormPendaftaran() {
        setTitle("Pendaftaran - SIKURSUS Desktop");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(KREM_LATAR);
        setLayout(new BorderLayout());

        // ---- Header ----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(KUNING_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(18, 10, 18, 10));
        JLabel lblJudul = new JLabel("Transaksi Pendaftaran Kursus", SwingConstants.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblJudul.setForeground(TEKS_GELAP);
        header.add(lblJudul, BorderLayout.CENTER);

        // ---- Form ----
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(KREM_PANEL);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                buatBorderJudul("Form Pendaftaran"),
                BorderFactory.createEmptyBorder(8, 12, 12, 12)));
        addRow(panelForm, 0, "Peserta", cbPeserta);
        addRow(panelForm, 1, "Kelas", cbKelas);
        addRow(panelForm, 2, "Tanggal Daftar", txtTanggal);
        addRow(panelForm, 3, "Status", cbStatus);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelTombol.setOpaque(false);
        panelTombol.add(btnSimpan);
        panelTombol.add(btnHapus);
        panelTombol.add(btnReset);
        panelTombol.add(btnCetakTerpilih);
        addRow(panelForm, 4, "", panelTombol);

        // ---- Tabel ----
        JScrollPane scroll = new JScrollPane(tblPendaftaran);
        scroll.setBorder(BorderFactory.createLineBorder(GARIS));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel panelData = new JPanel(new BorderLayout());
        panelData.setBackground(KREM_PANEL);
        panelData.setBorder(BorderFactory.createCompoundBorder(
                buatBorderJudul("Data Pendaftaran (pilih satu baris untuk dicetak)"),
                BorderFactory.createEmptyBorder(8, 12, 12, 12)));
        panelData.add(scroll, BorderLayout.CENTER);

        JPanel isi = new JPanel(new BorderLayout(0, 12));
        isi.setBackground(KREM_LATAR);
        isi.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        isi.add(panelForm, BorderLayout.NORTH);
        isi.add(panelData, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(isi, BorderLayout.CENTER);

        // ---- Gaya ----
        stylePemasukan(txtTanggal);
        styleCombo(cbPeserta);
        styleCombo(cbKelas);
        styleCombo(cbStatus);
        styleTombol(btnSimpan, KUNING_TOMBOL, TEKS_GELAP);
        styleTombol(btnHapus, MERAH, Color.WHITE);
        styleTombol(btnReset, ABU, Color.WHITE);
        styleTombol(btnCetakTerpilih, HIJAU, Color.WHITE);

        // ---- Event ----
        btnSimpan.addActionListener(e -> btnSimpanActionPerformed());
        btnHapus.addActionListener(e -> btnHapusActionPerformed());
        btnReset.addActionListener(e -> btnResetActionPerformed());
        btnCetakTerpilih.addActionListener(e -> btnCetakTerpilihActionPerformed());

        configureTable();
        loadCombo();
        loadTable();
        clearForm();

        setSize(900, 680);
        setLocationRelativeTo(null);
    }

    // =====================================================
    // TAMPILAN
    // =====================================================
    private TitledBorder buatBorderJudul(String judul) {
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GARIS), judul);
        tb.setTitleFont(new Font("SansSerif", Font.BOLD, 14));
        tb.setTitleColor(TEKS_GELAP);
        return tb;
    }

    private void stylePemasukan(JTextField tf) {
        tf.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(300, 32));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GARIS),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)));
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cb.setPreferredSize(new Dimension(300, 32));
        cb.setBackground(Color.WHITE);
    }

    private void styleTombol(JButton b, Color bg, Color fg) {
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker()),
                BorderFactory.createEmptyBorder(6, 22, 6, 22)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void addRow(JPanel panel, int baris, String label, java.awt.Component komponen) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 5, 6, 5);
        c.gridy = baris;
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(TEKS_GELAP);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lbl.setPreferredSize(new Dimension(120, 28));
        panel.add(lbl, c);

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(komponen, c);
    }

    // =====================================================
    // JTABLE - ID pendaftaran di KOLOM 0 (indeks 0, bukan 1)
    // =====================================================
    private void configureTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "NIM", "Nama Peserta", "Kelas", "Tanggal Daftar", "Status"},
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPendaftaran.setModel(model);
        tblPendaftaran.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPendaftaran.getColumnModel().getColumn(0).setMaxWidth(50);

        tblPendaftaran.setRowHeight(28);
        tblPendaftaran.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tblPendaftaran.setGridColor(new Color(240, 225, 170));
        tblPendaftaran.setSelectionBackground(KUNING_PILIH);
        tblPendaftaran.setSelectionForeground(TEKS_GELAP);
        tblPendaftaran.setFillsViewportHeight(true);

        JTableHeader th = tblPendaftaran.getTableHeader();
        th.setBackground(KUNING_TABEL);
        th.setForeground(TEKS_GELAP);
        th.setFont(new Font("SansSerif", Font.BOLD, 13));
        th.setReorderingAllowed(false);
    }

    private void loadTable() {
        DefaultTableModel model = (DefaultTableModel) tblPendaftaran.getModel();
        model.setRowCount(0);   // kosongkan dulu agar tidak dobel
        try {
            List<Pendaftaran> data = dao.findAll();
            for (Pendaftaran p : data) {
                model.addRow(new Object[]{
                    p.getId(),
                    p.getNim(),
                    p.getNamaPeserta(),
                    p.getNamaKelas(),
                    p.getTanggalDaftar().toString(),   // yyyy-MM-dd
                    p.getStatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat data pendaftaran: " + ex.getMessage()
                    + "\n\nPastikan tabel Pertemuan 13 sudah di-import dari db_sikursus.sql.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCombo() {
        try {
            cbPeserta.removeAllItems();
            for (Opsi o : dao.findPesertaOpsi()) {
                cbPeserta.addItem(o);
            }
            cbKelas.removeAllItems();
            for (Opsi o : dao.findKelasOpsi()) {
                cbKelas.addItem(o);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat daftar peserta/kelas: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        cbPeserta.setSelectedIndex(-1);
        cbKelas.setSelectedIndex(-1);
        cbStatus.setSelectedItem("AKTIF");
        txtTanggal.setText(LocalDate.now().toString());   // yyyy-MM-dd
        tblPendaftaran.clearSelection();
    }

    // =====================================================
    // AKSI TOMBOL
    // =====================================================
    private void btnSimpanActionPerformed() {
        Opsi peserta = (Opsi) cbPeserta.getSelectedItem();
        Opsi kelas = (Opsi) cbKelas.getSelectedItem();
        if (peserta == null || kelas == null) {
            JOptionPane.showMessageDialog(this,
                    "Peserta dan kelas wajib dipilih.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Date tanggal = Date.valueOf(txtTanggal.getText().trim());
            Pendaftaran p = new Pendaftaran(0, peserta.getId(), kelas.getId(),
                    tanggal, (String) cbStatus.getSelectedItem());
            if (dao.insert(p)) {
                JOptionPane.showMessageDialog(this, "Pendaftaran berhasil disimpan.");
                loadTable();
                clearForm();
            }
        } catch (IllegalArgumentException e) {
            // Date.valueOf gagal ATAU validasi model gagal
            JOptionPane.showMessageDialog(this,
                    "Data tidak valid. Tanggal harus berformat yyyy-MM-dd "
                    + "(contoh 2026-11-01).\n" + e.getMessage(),
                    "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this,
                    "Peserta ini sudah terdaftar pada kelas yang sama.",
                    "Data Ganda", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan data:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnHapusActionPerformed() {
        int row = tblPendaftaran.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih satu data pendaftaran terlebih dahulu.");
            return;
        }
        int id = Integer.parseInt(tblPendaftaran.getValueAt(row, 0).toString());
        String nama = tblPendaftaran.getValueAt(row, 2).toString();

        int jawab = JOptionPane.showConfirmDialog(this,
                "Yakin menghapus pendaftaran " + nama + " (ID " + id + ")?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this, "Pendaftaran berhasil dihapus.");
                loadTable();
                clearForm();
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this,
                    "Pendaftaran ini tidak bisa dihapus karena sudah memiliki data pembayaran.",
                    "Tidak Bisa Dihapus", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menghapus data:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnResetActionPerformed() {
        loadCombo();
        loadTable();
        clearForm();
    }

    // Tahap 6-7: ID dibaca dari baris terpilih, TIDAK di-hard-code
    private void btnCetakTerpilihActionPerformed() {
        int row = tblPendaftaran.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih satu data pendaftaran terlebih dahulu.");
            return;
        }
        int idPendaftaran = Integer.parseInt(
                tblPendaftaran.getValueAt(row, 0).toString());

        System.out.println("ID terpilih: " + idPendaftaran);   // bukti Tahap 6C
        ReportService.cetakPendaftaran(idPendaftaran);
    }
}
