package app;

import java.util.ArrayList;
import model.Instruktur;
import model.Orang;
import model.Peserta;

/**
 * Pertemuan 5 - Tugas 2: membangun kasus inheritance.
 * Hierarchy: Orang -> Peserta, Orang -> Instruktur.
 */
public class DemoInheritance {

    public static void main(String[] args) {
        // List bertipe parent (Orang) bisa menampung child (Peserta & Instruktur)
        ArrayList<Orang> daftarOrang = new ArrayList<>();

        // 3 Peserta (constructor child -> super(...) -> constructor Orang)
        daftarOrang.add(new Peserta(
                1, "Alya Rahma", "081234567890",
                "252001", "Informatika"));
        daftarOrang.add(new Peserta(
                2, "Rafi Akbar", "081298765432",
                "252002", "Informatika"));
        daftarOrang.add(new Peserta(
                3, "Nadia Putri", "081377770003",
                "252003", "Sistem Informasi"));

        // 2 Instruktur
        daftarOrang.add(new Instruktur(
                101, "Dina Pratama", "081211110001",
                "Java Desktop"));
        daftarOrang.add(new Instruktur(
                102, "Rizal Maulana", "081211110002",
                "Data Science"));

        // Loop seluruh object dan tampilkan getInfo()
        System.out.println("=== DATA SIKURSUS ===");
        for (Orang orang : daftarOrang) {
            System.out.println(orang.getInfo());
        }
        System.out.println("Jumlah object: " + daftarOrang.size());

        // Ubah data memakai setter milik parent (diwarisi child)
        System.out.println();
        System.out.println("=== SETELAH setNama() DAN setNoHp() ===");
        daftarOrang.get(1).setNama("Rafi Akbar Pratama");
        daftarOrang.get(3).setNoHp("081299990101");
        System.out.println(daftarOrang.get(1).getInfo());
        System.out.println(daftarOrang.get(3).getInfo());

        // Pengujian wajib no. 3 (panduan bagian 14)
        System.out.println();
        System.out.println("=== UJI SETTER PARENT ===");
        Peserta pesertaUji = new Peserta(
                4, "Nama Lama", "080000000000",
                "252004", "Informatika");
        pesertaUji.setNama("Nama Baru");
        System.out.println(pesertaUji.getInfo());
    }
}