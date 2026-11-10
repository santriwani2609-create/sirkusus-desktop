package view;

import Model.User;
import dao.UserDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import util.UserSession;

/**
 * Pintu masuk aplikasi. Login sukses -> UserSession diisi -> DashboardFrame.
 */
public class LoginFrame extends JFrame {

    private static final Color KUNING_HEADER = new Color(247, 207, 95);
    private static final Color KUNING_TOMBOL = new Color(244, 193, 62);
    private static final Color KREM_LATAR = new Color(255, 251, 235);
    private static final Color KREM_PANEL = new Color(255, 253, 244);
    private static final Color GARIS = new Color(230, 200, 110);
    private static final Color TEKS_GELAP = new Color(90, 64, 0);
    private static final Color ABU = new Color(161, 136, 127);

    private final JLabel lblTitle = new JLabel("SIKURSUS Desktop", SwingConstants.CENTER);
    private final JLabel lblInfo = new JLabel("Masukkan akun SIKURSUS", SwingConstants.CENTER);
    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField pwdPassword = new JPasswordField(18);
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnKeluar = new JButton("Keluar");

    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        setTitle("Login - SIKURSUS Desktop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(KREM_LATAR);
        setLayout(new BorderLayout());

        // ---- Header ----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(KUNING_HEADER);
        header.setBorder(BorderFactory.createEmptyBorder(18, 10, 14, 10));
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(TEKS_GELAP);
        JLabel lblSub = new JLabel("Sistem Informasi Manajemen Kursus & Pelatihan",
                SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(TEKS_GELAP);
        header.add(lblTitle, BorderLayout.CENTER);
        header.add(lblSub, BorderLayout.SOUTH);

        // ---- Form ----
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(KREM_PANEL);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(16, 24, 8, 24),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GARIS),
                        BorderFactory.createEmptyBorder(16, 18, 16, 18))));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        lblInfo.setForeground(ABU);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        form.add(lblInfo, c);

        c.gridwidth = 1;
        c.gridy = 1; c.gridx = 0; c.weightx = 0;
        form.add(new JLabel("Username"), c);
        c.gridx = 1; c.weightx = 1;
        form.add(txtUsername, c);

        c.gridy = 2; c.gridx = 0; c.weightx = 0;
        form.add(new JLabel("Password"), c);
        c.gridx = 1; c.weightx = 1;
        form.add(pwdPassword, c);

        styleTombol(btnLogin, KUNING_TOMBOL, TEKS_GELAP);
        styleTombol(btnKeluar, new Color(161, 136, 127), Color.WHITE);
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelTombol.setOpaque(false);
        panelTombol.add(btnLogin);
        panelTombol.add(btnKeluar);
        c.gridy = 3; c.gridx = 0; c.gridwidth = 2; c.weightx = 0;
        c.insets = new Insets(14, 6, 0, 6);
        form.add(panelTombol, c);

        add(header, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> btnLoginActionPerformed());
        btnKeluar.addActionListener(e -> btnKeluarActionPerformed());

        pack();
        setMinimumSize(new Dimension(460, getHeight()));
        setLocationRelativeTo(null);
        // Enter = klik Login
        getRootPane().setDefaultButton(btnLogin);
    }

    private void styleTombol(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(95, 32));
    }

    private void btnLoginActionPerformed() {
        String username = txtUsername.getText().trim();
        char[] password = pwdPassword.getPassword();

        if (username.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Username dan password wajib diisi.");
            Arrays.fill(password, '\0');
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btnLogin.setEnabled(false);

        try {
            User user = userDAO.authenticate(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Login gagal. Periksa username, password, atau status user.",
                        "Login Gagal",
                        JOptionPane.WARNING_MESSAGE);
                pwdPassword.setText("");
                pwdPassword.requestFocus();
                return;
            }

            UserSession.setCurrentUser(user);
            dispose();
            SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Tidak dapat mengakses database: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(password, '\0');
            btnLogin.setEnabled(true);
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void btnKeluarActionPerformed() {
        System.exit(0);
    }
}
