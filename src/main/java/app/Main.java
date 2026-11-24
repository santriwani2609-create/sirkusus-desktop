package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.LoginFrame;

/**
 * Titik masuk aplikasi: selalu mulai dari LoginFrame.
 */
public class Main {
    public static void main(String[] args) {
        // Look and feel Metal agar warna tombol (setBackground) konsisten di semua OS
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
            // pakai look and feel bawaan jika gagal
        }
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
