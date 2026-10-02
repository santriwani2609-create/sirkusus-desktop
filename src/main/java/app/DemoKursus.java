package app;

import model.Kursus;

/**
 */
public class DemoKursus {

    public static void main(String[] args) {
        // Praktikum E: satu object dengan constructor lengkap
        Kursus k1 = new Kursus(
                "JAVA-BSC",
                "Java Desktop Fundamental",
                "BASIC",
                500000
        );
        double hasil = k1.hitungBiayaSetelahDiskon(10);
        System.out.println("Biaya setelah diskon: " + hasil);

        // Praktikum F: 5 skenario uji (biaya, diskon, expected)
        double[][] kasus = {
            {500000, 0, 500000},
            {500000, 10, 450000},
            {500000, 25, 375000},
            {0, 10, 0},
            {500000, 100, 0}
        };

        System.out.println();
        System.out.println("=== PENGUJIAN hitungBiayaSetelahDiskon ===");
        for (int i = 0; i < kasus.length; i++) {
            Kursus k = new Kursus("TES-" + (i + 1), "Uji", "BASIC", kasus[i][0]);
            double actual = k.hitungBiayaSetelahDiskon(kasus[i][1]);
            String status = (actual == kasus[i][2]) ? "PASS" : "FAIL";
            System.out.println((i + 1) + " | biaya=" + kasus[i][0]
                    + " diskon=" + kasus[i][1] + "%"
                    + " | expected=" + kasus[i][2]
                    + " actual=" + actual
                    + " | " + status);
        }
    }
}