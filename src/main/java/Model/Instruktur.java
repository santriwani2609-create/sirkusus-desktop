package model;

/**
 * Child class: Instruktur is-a Orang.
 * INHERITANCE: id, nama, noHp diwarisi dari Orang, tidak ditulis ulang di sini.
 */
public class Instruktur extends Orang {

    private String keahlian;

    public Instruktur(int id, String nama, String noHp,
                      String keahlian) {
        // SUPER: teruskan data umum ke constructor parent (Orang)
        super(id, nama, noHp);
        this.keahlian = keahlian;
    }

    public String getKeahlian() {
        return keahlian;
    }

    public void setKeahlian(String keahlian) {
        this.keahlian = keahlian;
    }

    @Override
    public String getInfo() {
        // super.getInfo(): pakai format data umum dari Orang, lalu tambah data khusus
        return "[Instruktur] " + super.getInfo()
                + " | Keahlian: " + keahlian;
    }
}