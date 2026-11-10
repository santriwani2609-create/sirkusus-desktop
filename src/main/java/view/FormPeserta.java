package view;

import Model.Peserta;
import dao.PesertaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.print.PrinterException;
import java.sql.SQLException;
import java.text.MessageFormat;
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
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class FormPeserta extends JFrame {

    // ===== Palet warna: kuning soft =====
    private static final Color KUNING_HEADER = new Color(247, 207, 95);   // header atas
    private static final Color KUNING_TOMBOL = new Color(244, 193, 62);   // tombol utama
    private static final Color KUNING_TABEL = new Color(255, 232, 150);   // header tabel
    private static final Color KUNING_PILIH = new Color(255, 236, 170);   // baris terpilih
    private static final Color KREM_LATAR = new Color(255, 251, 235);     // latar form
    private static final Color KREM_PANEL = new Color(255, 253, 244);     // latar panel
    private static final Color GARIS = new Color(230, 200, 110);          // garis border
    private static final Color TEKS_GELAP = new Color(90, 64, 0);         // teks di atas kuning
    private static final Color MERAH = new Color(229, 115, 115);
    private static final Color ABU = new Color(161, 136, 127);
    private static final Color HIJAU = new Color(102, 187, 106);

    // ===== Komponen input =====
    private final JTextField txtNim = new JTextField();
    private final JTextField txtNama = new JTextField();
    private final JComboBox<String> cbJenisKelamin = new JComboBox<>(new String[]{"L", "P"});
    private final JComboBox<String> cbProdi = new JComboBox<>(new String[]{
        "Informatika", "Sistem Informasi", "Teknik Elektro", "Matematika"});
    private final JTextField txtNoHp = new JTextField();
    private final JTextField txtEmail = new JTextField();

    // ===== Tombol =====
    private final JButton btnSimpan = new JButton("Simpan");
    private final JButton btnUbah = new JButton("Ubah");
    private final JButton btnHapus = new JButton("Hapus");
    private final JButton btnReset = new JButton("Reset");
    private final JButton btnCetak = new JButton("Cetak Semua");

    // ===== Pencarian, tabel, DAO, state =====
    private final JTextField txtCari = new JTextField();
    private final JButton btnCari = new JButton("Cari");
    private final JTable tblPeserta = new JTable();
    private final PesertaDAO dao = new PesertaDAO();
    private int selectedId = -1;   // -1 = belum ada peserta yang dipilih

    public FormPeserta() {
        setTitle("Master Peserta - SIKURSUS Desktop");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(KREM_LATAR);
        setLayout(new BorderLayout());

        // ---- Header atas ----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(KUNING_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(18, 10, 18, 10));
        JLabel lblJudul = new JLabel("Master Data Peserta", SwingConstants.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblJudul.setForeground(TEKS_GELAP);
        header.add(lblJudul, BorderLayout.CENTER);

        // ---- Panel form (titled border "Form Peserta") ----
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(KREM_PANEL);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                buatBorderJudul("Form Peserta"),
                BorderFactory.createEmptyBorder(8, 12, 12, 12)));

        addRow(panelForm, 0, "NIM", txtNim);
        addRow(panelForm, 1, "Nama Peserta", txtNama);
        addRow(panelForm, 2, "Jenis Kelamin", cbJenisKelamin);
        addRow(panelForm, 3, "Program Studi", cbProdi);
        addRow(panelForm, 4, "Nomor HP", txtNoHp);
        addRow(panelForm, 5, "Email", txtEmail);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelTombol.setOpaque(false);
        panelTombol.add(btnSimpan);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnReset);
        panelTombol.add(btnCetak);
        addRow(panelForm, 6, "", panelTombol);

        // ---- Panel data (pencarian + tabel) ----
        JPanel panelCari = new JPanel(new BorderLayout(10, 0));
        panelCari.setOpaque(false);
        JLabel lblCari = new JLabel("Pencarian");
        lblCari.setForeground(TEKS_GELAP);
        lblCari.setPreferredSize(new Dimension(110, 30));
        panelCari.add(lblCari, BorderLayout.WEST);
        panelCari.add(txtCari, BorderLayout.CENTER);
        panelCari.add(btnCari, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(tblPeserta);
        scroll.setBorder(BorderFactory.createLineBorder(GARIS));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel panelData = new JPanel(new BorderLayout(0, 8));
        panelData.setBackground(KREM_PANEL);
        panelData.setBorder(BorderFactory.createCompoundBorder(
                buatBorderJudul("Data Peserta"),
                BorderFactory.createEmptyBorder(8, 12, 12, 12)));
        panelData.add(panelCari, BorderLayout.NORTH);
        panelData.add(scroll, BorderLayout.CENTER);

        // ---- Susun semua ke dalam konten tengah ----
        JPanel isi = new JPanel(new BorderLayout(0, 12));
        isi.setBackground(KREM_LATAR);
        isi.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        isi.add(panelForm, BorderLayout.NORTH);
        isi.add(panelData, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(isi, BorderLayout.CENTER);

        // ---- Gaya komponen ----
        stylePemasukan(txtNim);
        stylePemasukan(txtNama);
        stylePemasukan(txtNoHp);
        stylePemasukan(txtEmail);
        stylePemasukan(txtCari);
        styleCombo(cbJenisKelamin);
        styleCombo(cbProdi);

        styleTombol(btnSimpan, KUNING_TOMBOL, TEKS_GELAP);
        styleTombol(btnUbah, new Color(232, 170, 30), TEKS_GELAP);
        styleTombol(btnHapus, MERAH, Color.WHITE);
        styleTombol(btnReset, ABU, Color.WHITE);
        styleTombol(btnCetak, HIJAU, Color.WHITE);
        styleTombol(btnCari, KUNING_TOMBOL, TEKS_GELAP);

        // ---- Hubungkan tombol & klik tabel ke method ----
        btnSimpan.addActionListener(e -> btnSimpanActionPerformed());
        btnUbah.addActionListener(e -> btnUbahActionPerformed());
        btnHapus.addActionListener(e -> btnHapusActionPerformed());
        btnCari.addActionListener(e -> btnCariActionPerformed());
        btnReset.addActionListener(e -> btnResetActionPerformed());
        btnCetak.addActionListener(e -> btnCetakActionPerformed());
        // Tekan Enter di kolom pencarian = klik Cari
        txtCari.addActionListener(e -> btnCariActionPerformed());
        // Setiap kali baris tabel dipilih (klik atau tombol panah), isi form
        tblPeserta.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tblPesertaMouseClicked();
            }
        });

        // ---- Urutan awal: siapkan tabel -> isi data -> bersihkan form ----
        configureTable();
        loadTable();
        clearForm();

        setSize(1000, 740);
        setLocationRelativeTo(null);   // tampil di tengah layar
    }

    // =====================================================
    // TAMPILAN: helper gaya
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

    private void styleCombo(JComboBox<String> cb) {
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
    // JTABLE: konfigurasi, isi, dan muat data
    // =====================================================
    private void configureTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "NIM", "Nama", "JK", "Prodi", "No. HP", "Email"},
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   // edit lewat form, bukan langsung di tabel
            }
        };
        tblPeserta.setModel(model);
        tblPeserta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPeserta.getColumnModel().getColumn(0).setMaxWidth(50);   // kolom ID
        tblPeserta.getColumnModel().getColumn(3).setMaxWidth(40);   // kolom JK

        // Tampilan tabel
        tblPeserta.setRowHeight(28);
        tblPeserta.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tblPeserta.setGridColor(new Color(240, 225, 170));
        tblPeserta.setSelectionBackground(KUNING_PILIH);
        tblPeserta.setSelectionForeground(TEKS_GELAP);
        tblPeserta.setFillsViewportHeight(true);

        JTableHeader th = tblPeserta.getTableHeader();
        th.setBackground(KUNING_TABEL);
        th.setForeground(TEKS_GELAP);
        th.setFont(new Font("SansSerif", Font.BOLD, 13));
        th.setReorderingAllowed(false);
    }

    private void fillTable(List<Peserta> data) {
        DefaultTableModel model = (DefaultTableModel) tblPeserta.getModel();
        model.setRowCount(0);   // WAJIB: kosongkan dulu agar tidak dobel
        for (Peserta p : data) {
            model.addRow(new Object[]{
                p.getId(),
                p.getNim(),
                p.getNama(),
                p.getJenisKelamin(),
                p.getProdi(),
                p.getNoHp(),
                p.getEmail()
            });
        }
    }

    private void loadTable() {
        try {
            fillTable(dao.findAll());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal memuat data: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            // Ada baris di database yang melanggar aturan validasi class Peserta
            JOptionPane.showMessageDialog(this,
                    "Ada data di database yang tidak valid: " + ex.getMessage(),
                    "Data Tidak Valid", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Baris tabel dipilih -> isi form
    private void tblPesertaMouseClicked() {
        int row = tblPeserta.getSelectedRow();
        if (row == -1) {
            return;
        }

        // ID diambil dari KOLOM 0 tabel, bukan dari nomor baris
        selectedId = Integer.parseInt(tblPeserta.getValueAt(row, 0).toString());

        txtNim.setText(tblPeserta.getValueAt(row, 1).toString());
        txtNama.setText(tblPeserta.getValueAt(row, 2).toString());
        cbJenisKelamin.setSelectedItem(tblPeserta.getValueAt(row, 3).toString());
        setPilihanProdi(tblPeserta.getValueAt(row, 4).toString());
        txtNoHp.setText(tblPeserta.getValueAt(row, 5).toString());
        txtEmail.setText(tblPeserta.getValueAt(row, 6).toString());

        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
    }

    // Pilih prodi di combo; kalau belum ada di daftar, tambahkan dulu
    private void setPilihanProdi(String prodi) {
        boolean ada = false;
        for (int i = 0; i < cbProdi.getItemCount(); i++) {
            if (cbProdi.getItemAt(i).equalsIgnoreCase(prodi)) {
                cbProdi.setSelectedIndex(i);
                ada = true;
                break;
            }
        }
        if (!ada && !prodi.isEmpty()) {
            cbProdi.addItem(prodi);
            cbProdi.setSelectedItem(prodi);
        }
    }

    // =====================================================
    // STATE FORM: refresh, clear, ambil data dari form
    // =====================================================
    private void refreshData() {
        loadTable();
        clearForm();
        txtCari.setText("");
    }

    private void clearForm() {
        selectedId = -1;
        txtNim.setText("");
        txtNama.setText("");
        cbJenisKelamin.setSelectedIndex(-1);
        cbProdi.setSelectedIndex(-1);
        txtNoHp.setText("");
        txtEmail.setText("");
        tblPeserta.clearSelection();
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        txtNim.requestFocus();
    }

    private String getJenisKelamin() {
        Object pilihan = cbJenisKelamin.getSelectedItem();
        return pilihan == null ? "" : pilihan.toString();
    }

    private String getProdi() {
        Object pilihan = cbProdi.getSelectedItem();
        return pilihan == null ? "" : pilihan.toString().trim();
    }

    // Validasi (dipakai oleh Simpan dan Ubah)
    private boolean validateForm() {
        String nim = txtNim.getText().trim();
        String nama = txtNama.getText().trim();
        String prodi = getProdi();
        String noHp = txtNoHp.getText().trim();
        String email = txtEmail.getText().trim();

        if (nim.isEmpty() || nama.isEmpty() || prodi.isEmpty()
                || getJenisKelamin().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "NIM, nama, jenis kelamin, dan prodi wajib diisi.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!noHp.isEmpty() && !noHp.matches("\\+?[0-9]+")) {
            JOptionPane.showMessageDialog(this,
                    "No. HP hanya boleh berisi angka.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtNoHp.requestFocus();
            return false;
        }

        if (!email.isEmpty() && !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            JOptionPane.showMessageDialog(this,
                    "Format email tidak valid. Contoh: nama@example.com",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        return true;
    }

    // Ubah isi form menjadi objek Peserta (id = 0 untuk data baru)
    private Peserta buildPeserta(int id) {
        Peserta p = new Peserta(id,
                txtNama.getText().trim(),
                txtNoHp.getText().trim(),
                txtNim.getText().trim(),
                getProdi());
        p.setJenisKelamin(getJenisKelamin());
        p.setEmail(txtEmail.getText().trim());
        return p;
    }

    // =====================================================
    // AKSI TOMBOL
    // =====================================================
    private void btnSimpanActionPerformed() {
        if (!validateForm()) {
            return;
        }
        try {
            Peserta peserta = buildPeserta(0);   // 0 = ID diisi otomatis MySQL
            if (dao.insert(peserta)) {
                JOptionPane.showMessageDialog(this, "Data peserta berhasil disimpan.");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Data tidak tersimpan.", "Informasi",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan data:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void btnUbahActionPerformed() {
        if (selectedId <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data peserta pada tabel terlebih dahulu.");
            return;
        }
        if (!validateForm()) {
            return;
        }
        try {
            Peserta peserta = buildPeserta(selectedId);
            if (dao.update(peserta)) {
                JOptionPane.showMessageDialog(this, "Data peserta berhasil diubah.");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Data tidak ditemukan atau tidak berubah.");
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mengubah data:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnHapusActionPerformed() {
        if (selectedId <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih data peserta yang akan dihapus.");
            return;
        }

        String nama = txtNama.getText().trim();
        int jawab = JOptionPane.showConfirmDialog(this,
                "Yakin menghapus peserta: " + nama + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (jawab != JOptionPane.YES_OPTION) {
            return;   // pilih "No" -> batal, data aman
        }

        try {
            if (dao.delete(selectedId)) {
                JOptionPane.showMessageDialog(this, "Data peserta berhasil dihapus.");
                refreshData();
            }
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            // Biasanya karena foreign key: peserta sudah dipakai tabel lain
            JOptionPane.showMessageDialog(this,
                    "Peserta ini tidak bisa dihapus karena sudah dipakai "
                    + "di data lain (misalnya pendaftaran).",
                    "Tidak Bisa Dihapus", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menghapus data:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnCariActionPerformed() {
        String keyword = txtCari.getText().trim();
        try {
            if (keyword.isEmpty()) {
                loadTable();                      // kata kunci kosong -> tampilkan semua
            } else {
                fillTable(dao.search(keyword));   // tampilkan yang cocok saja
            }
            clearForm();   // supaya Ubah/Hapus tidak menempel ke data lama
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Pencarian gagal: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Ada data di database yang tidak valid: " + e.getMessage(),
                    "Data Tidak Valid", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnResetActionPerformed() {
        txtCari.setText("");
        loadTable();
        clearForm();
    }

    // Cetak seluruh data peserta (tampilkan semua dulu, lalu buka dialog print)
    private void btnCetakActionPerformed() {
        refreshData();
        try {
            tblPeserta.print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("Data Peserta SIKURSUS"),
                    new MessageFormat("Halaman {0}"));
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mencetak data:\n" + e.getMessage(),
                    "Cetak Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Jika form ini dijalankan langsung (Run File), tetap lewat Login dulu
     * supaya alur aplikasi konsisten: Login -> Dashboard -> Master.
     */
    public static void main(String[] args) {
        app.Main.main(args);
    }
}
