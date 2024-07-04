package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Repräsentiert eine Dokument-Entität, die einer Immobilie (Wohnung) oder einem Mieter zugeordnet ist.
 * Diese Entität wird der Datenbanktabelle 'dokument' zugeordnet.
 */
@Entity
@Table(name = "dokument")
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

    // Getter und Setter

    public Long getDokument_id() {
        return dokument_id;
    }

    public void setDokument_id(Long dokument_id) {
        this.dokument_id = dokument_id;
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

    public void setMieter(Mieter mieter) {
        this.mieter = mieter;
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

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

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