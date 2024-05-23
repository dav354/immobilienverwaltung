package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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
    @NotNull(message = "Dokumententyp cannot be null")
    private String dokumententyp;

    @Column(nullable = false)
    @NotNull(message = "Dateipfad cannot be null")
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

    /**
     * Gets the ID of this document.
     *
     * @return the document ID.
     */
    public Long getDokument_id() {
        return dokument_id;
    }

    /**
     * Gets the property (Wohnung) associated with this document.
     *
     * @return the associated Wohnung.
     */
    public Wohnung getWohnung() {
        return wohnung;
    }

    /**
     * Sets the property (Wohnung) associated with this document.
     *
     * @param wohnung the Wohnung to associate.
     */
    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }

    /**
     * Gets the tenant (Mieter) associated with this document.
     *
     * @return the associated Mieter.
     */
    public Mieter getMieter() {
        return mieter;
    }

    /**
     * Sets the tenant (Mieter) associated with this document.
     *
     * @param mieter the Mieter to associate.
     */
    public void setMieter(Mieter mieter) {
        this.mieter = mieter;
    }

    /**
     * Gets the type of this document.
     *
     * @return the document type.
     */
    public String getDokumententyp() {
        return dokumententyp;
    }

    /**
     * Sets the type of this document.
     *
     * @param dokumententyp the new document type.
     * @throws IllegalArgumentException if the document type is null or empty.
     */
    public void setDokumententyp(String dokumententyp) {
        if (dokumententyp == null || dokumententyp.isEmpty()) {
            throw new IllegalArgumentException("Dokumententyp cannot be null or empty");
        }
        this.dokumententyp = dokumententyp;
    }

    /**
     * Gets the file path where this document is stored.
     *
     * @return the file path.
     */
    public String getDateipfad() {
        return dateipfad;
    }

    /**
     * Sets the file path where this document is stored.
     *
     * @param dateipfad the new file path.
     * @throws IllegalArgumentException if the file path is null or empty.
     */
    public void setDateipfad(String dateipfad) {
        if (dokumententyp == null || dokumententyp.isEmpty()) {
            throw new IllegalArgumentException("Dokumententyp cannot be null or empty");
        }
        this.dateipfad = dateipfad;
    }

    /**
     * Returns a string representation of this document.
     *
     * @return a string representation of this document.
     */
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