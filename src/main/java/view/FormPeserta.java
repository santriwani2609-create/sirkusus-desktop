package view;

import Model.Peserta;
import dao.PesertaDAO;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class FormPeserta extends JFrame {

    private final JTextField txtNim = new JTextField(20);
    private final JTextField txtNama = new JTextField(20);
    private final JRadioButton rbLaki = new JRadioButton("Laki-laki");
    private final JRadioButton rbPerempuan = new JRadioButton("Perempuan");
    private final ButtonGroup buttonGroupJenisKelamin = new ButtonGroup();
    private final JTextField txtProdi = new JTextField(20);
    private final JTextField txtNoHp = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JButton btnSimpan = new JButton("Simpan");
    private final JButton btnReset = new JButton("Reset");

    public FormPeserta() {
        setTitle("Form Peserta - SIKURSUS");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Radio button dimasukkan ke satu grup (hanya boleh pilih satu)
        buttonGroupJenisKelamin.add(rbLaki);
        buttonGroupJenisKelamin.add(rbPerempuan);

        // Susun tampilan
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        addRow(panel, 0, "NIM", txtNim);
        addRow(panel, 1, "Nama", txtNama);

        JPanel panelJk = new JPanel();
        panelJk.add(rbLaki);
        panelJk.add(rbPerempuan);
        addRow(panel, 2, "Jenis Kelamin", panelJk);

        addRow(panel, 3, "Prodi", txtProdi);
        addRow(panel, 4, "No. HP", txtNoHp);
        addRow(panel, 5, "Email", txtEmail);

        JPanel panelTombol = new JPanel();
        panelTombol.add(btnSimpan);
        panelTombol.add(btnReset);
        addRow(panel, 6, "", panelTombol);

        // Hubungkan tombol ke method
        btnSimpan.addActionListener(e -> btnSimpanActionPerformed());
        btnReset.addActionListener(e -> clearForm());

        add(panel);
        pack();
        setLocationRelativeTo(null);   // tampil di tengah layar
    }

    private void addRow(JPanel panel, int baris, String label, java.awt.Component komponen) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.gridy = baris;
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        panel.add(new JLabel(label), c);

        c.gridx = 1;
        panel.add(komponen, c);
    }

    private void clearForm() {
        txtNim.setText("");
        txtNama.setText("");
        buttonGroupJenisKelamin.clearSelection();
        txtProdi.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");
        txtNim.requestFocus();
    }

    private void btnSimpanActionPerformed() {
        // 1. Ambil input
        String nim = txtNim.getText().trim();
        String nama = txtNama.getText().trim();
        String prodi = txtProdi.getText().trim();
        String noHp = txtNoHp.getText().trim();
        String email = txtEmail.getText().trim();

        String jenisKelamin = "";
        if (rbLaki.isSelected()) {
            jenisKelamin = "L";
        } else if (rbPerempuan.isSelected()) {
            jenisKelamin = "P";
        }

        // 2. Validasi data wajib
        if (nim.isEmpty() || nama.isEmpty() || prodi.isEmpty() || jenisKelamin.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "NIM, nama, jenis kelamin, dan prodi wajib diisi.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2b. Validasi tambahan (latihan penguatan)
        // No. HP: boleh kosong, tapi kalau diisi hanya angka (boleh diawali +)
        if (!noHp.isEmpty() && !noHp.matches("\\+?[0-9]+")) {
            JOptionPane.showMessageDialog(this,
                    "No. HP hanya boleh berisi angka.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtNoHp.requestFocus();
            return;
        }

        // Email: boleh kosong, tapi kalau diisi formatnya harus benar
        if (!email.isEmpty() && !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            JOptionPane.showMessageDialog(this,
                    "Format email tidak valid. Contoh: nama@example.com",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        try {
            // 3. Buat objek Peserta (id = 0 karena diisi otomatis oleh MySQL)
            Peserta peserta = new Peserta(0, nama, noHp, nim, prodi);
            peserta.setJenisKelamin(jenisKelamin);
            peserta.setEmail(email);

            // 4. Panggil DAO
            PesertaDAO dao = new PesertaDAO();
            boolean berhasil = dao.insert(peserta);

            if (berhasil) {
                JOptionPane.showMessageDialog(this, "Data peserta berhasil disimpan.");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Data tidak tersimpan.", "Informasi",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (IllegalArgumentException e) {
            // Error validasi dari setter di class Peserta/Orang
            JOptionPane.showMessageDialog(this,
                    e.getMessage(), "Validasi", JOptionPane.WARNING_MESSAGE);

        } catch (java.sql.SQLException e) {
            // Error dari database (NIM duplikat, MySQL mati, dll.)
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan data:\n" + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FormPeserta().setVisible(true));
    }
}