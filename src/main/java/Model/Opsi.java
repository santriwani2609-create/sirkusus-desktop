package Model;

/**
 * Pasangan id + teks untuk isi JComboBox (yang tampil label, yang dipakai id).
 */
public class Opsi {
    private final int id;
    private final String label;

    public Opsi(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
