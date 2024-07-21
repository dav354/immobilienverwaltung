package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Repräsentiert eine Dokument-Entität, die einer Immobilie (Wohnung) oder einem Mieter zugeordnet ist.
 * Diese Entität wird der Datenbanktabelle 'dokument' zugeordnet.
 */
@Entity
@Table(name = "dokument", indexes = {
        @Index(name = "idx_dokument_wohnung_id", columnList = "wohnung_id"),
        @Index(name = "idx_dokument_mieter_id", columnList = "mieter_id")
})
public class Dokument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dokument_id;

    @ManyToOne()
    @JoinColumn(name = "wohnung_id")
    private Wohnung wohnung;

    @ManyToOne()
    @JoinColumn(name = "mieter_id")
    private Mieter mieter;

    @Column
    @NotBlank(message = "Dokumententyp darf nicht leer sein")
    private String dokumententyp;

    @Column
    @NotBlank(message = "Dateipfad darf nicht leer sein")
    private String dateipfad;

    @Column
    private String mimeType;

    /**
     * Standardkonstruktor für JPA.
     */
    public Dokument() {
    }

    /**
     * Erstellt ein neues Dokument mit den angegebenen Details.
     *
     * @param wohnung       Die Immobilie (Wohnung), mit der dieses Dokument verknüpft ist.
     * @param mieter        Der Mieter (Mieter), mit dem dieses Dokument verknüpft ist.
     * @param dokumententyp Der Typ des Dokuments.
     * @param dateipfad     Der Dateipfad, wo das Dokument gespeichert ist.
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
     * Gibt die ID dieses Dokuments zurück.
     *
     * @return die Dokument-ID.
     */
    public Long getDokument_id() {
        return dokument_id;
    }

    /**
     * Setzt die ID dieses Dokuments.
     *
     * @param dokument_id die Dokument-ID.
     */
    public void setDokument_id(Long dokument_id) {
        this.dokument_id = dokument_id;
    }

    /**
     * Gibt die Wohnung zurück, die mit diesem Dokument verknüpft ist.
     *
     * @return die verknüpfte Wohnung.
     */
    public Wohnung getWohnung() {
        return wohnung;
    }

    /**
     * Setzt die Wohnung, die mit diesem Dokument verknüpft ist.
     *
     * @param wohnung die zu verknüpfende Wohnung.
     */
    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }

    /**
     * Gibt den Mieter zurück, der mit diesem Dokument verknüpft ist.
     *
     * @return der verknüpfte Mieter.
     */
    public Mieter getMieter() {
        return mieter;
    }

    /**
     * Setzt den Mieter, der mit diesem Dokument verknüpft ist.
     *
     * @param mieter der zu verknüpfende Mieter.
     */
    public void setMieter(Mieter mieter) {
        this.mieter = mieter;
    }

    /**
     * Gibt den Dokumententyp zurück.
     *
     * @return der Dokumententyp.
     */
    public String getDokumententyp() {
        return dokumententyp;
    }

    /**
     * Setzt den Dokumententyp.
     *
     * @param dokumententyp der neue Dokumententyp.
     */
    public void setDokumententyp(String dokumententyp) {
        this.dokumententyp = dokumententyp;
    }

    /**
     * Gibt den Dateipfad zurück, in dem dieses Dokument gespeichert ist.
     *
     * @return der Dateipfad.
     */
    public String getDateipfad() {
        return dateipfad;
    }

    /**
     * Setzt den Dateipfad, in dem dieses Dokument gespeichert ist.
     *
     * @param dateipfad der neue Dateipfad.
     */
    public void setDateipfad(String dateipfad) {
        this.dateipfad = dateipfad;
    }


    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Gibt eine String-Darstellung dieses Dokuments zurück.
     *
     * @return eine String-Darstellung dieses Dokuments.
     */
    @Override
    public String toString() {
        return "Dokument[" +
                "dokumentId='" + dokument_id +
                "', wohnung='" + (wohnung != null ? wohnung.getWohnung_id() : "Keine") +
                "', mieter='" + (mieter != null ? mieter.getMieter_id() : "Keine") +
                "', dokumententyp='" + dokumententyp +
                "', dateipfad='" + dateipfad +
                "', mimeType='" + mimeType +
                "']";
    }
}