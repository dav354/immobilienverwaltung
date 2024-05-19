package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;

/**
 * Represents a document entity associated with a property (Wohnung) or a tenant (Mieter).
 * This entity is mapped to the database table 'dokument'.
 */
@Entity
@Table(name = "dokument")
public class Dokument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dokument_id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "wohnung_id", nullable = true)
    private Wohnung wohnung;

    @ManyToOne(optional = true)
    @JoinColumn(name = "mieter_id", nullable = true)
    private Mieter mieter;

    @Column(nullable = false)
    private String dokumententyp;

    @Column(nullable = false)
    private String dateipfad;

    /**
     * Constructs a new Dokument with specified details.
     *
     * @param wohnung       The property (Wohnung) this document is associated with.
     * @param mieter        The tenant (Mieter) this document is associated with.
     * @param dokumententyp The type of the document.
     * @param dateipfad     The file path where the document is stored.
     */
    public Dokument(Wohnung wohnung,
                    Mieter mieter,
                    String dokumententyp,
                    String dateipfad) {
        this.wohnung = wohnung;
        this.mieter = mieter;
        this.dokumententyp = dokumententyp;
        this.dateipfad = dateipfad;
    }

    /**
     * Default constructor for JPA.
     */
    public Dokument() {
    }

    public Long getDokument_id() {
        return dokument_id;
    }

    public Wohnung getWohnung() {
        return wohnung;
    }

    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }

    public Mieter getMieter() {
        return mieter;
    }

    public String getDokumententyp() {
        return dokumententyp;
    }

    public void setDokumententyp(String dokumententyp) {
        this.dokumententyp = dokumententyp;
    }

    public String getDateipfad() {
        return dateipfad;
    }

    public void setDateipfad(String dateipfad) {
        this.dateipfad = dateipfad;
    }

    @Override
    public String toString() {
        return "Dokument[" +
                "dokumentId='" + dokument_id +
                "', eigentum='" + (wohnung != null ? wohnung.getWohnung_id() : "None") +
                "', mieter='" + (mieter != null ? mieter.getMieter_id() : "None") +
                "', dokumententyp='" + dokumententyp +
                "', dateipfad='" + dateipfad +
                "']";
    }
}