package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * Kerangka form master untuk modul yang CRUD-nya belum dibuat
 * (Instruktur, Kursus, Kelas). Cukup agar navigasi Dashboard bisa diuji.
 * Ganti dengan pola DAO + JTable (seperti FormPeserta) pada tahap berikutnya.
 */
public abstract class ModulKerangkaFrame extends JFrame {

    protected ModulKerangkaFrame(String judul) {
        setTitle(judul + " - SIKURSUS Desktop");
        // WAJIB DISPOSE_ON_CLOSE: tombol X hanya menutup form ini, bukan seluruh aplikasi
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 251, 235));
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(247, 207, 95));
        header.setBorder(BorderFactory.createEmptyBorder(18, 10, 18, 10));
        JLabel lblJudul = new JLabel(judul, SwingConstants.CENTER);
        lblJudul.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblJudul.setForeground(new Color(90, 64, 0));
        header.add(lblJudul, BorderLayout.CENTER);

        JLabel lblIsi = new JLabel(
                "<html><center>Modul " + judul + " masih berupa kerangka.<br>"
                + "CRUD lengkap dikembangkan pada tahap project berikutnya.</center></html>",
                SwingConstants.CENTER);
        lblIsi.setForeground(new Color(161, 136, 127));

        add(header, BorderLayout.NORTH);
        add(lblIsi, BorderLayout.CENTER);

        setSize(520, 340);
        setLocationRelativeTo(null);
    }
}
