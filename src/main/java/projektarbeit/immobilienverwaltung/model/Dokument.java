package projektarbeit.immobilienverwaltung.model;

import javax.persistence.*;

@Entity
@Table(name = "dokument", indexes = {
        @Index(name = "idx_dokument_eigentumId", columnList = "eigentumId"),
        @Index(name = "idx_dokument_mieterId", columnList = "mieterId")
})

public class Dokument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dokumentId;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "eigentumId", nullable = true)
    private Wohnung wohnung;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "mieterId", nullable = true)
    private Mieter mieter;

    @Column(nullable = false)
    private String dokumententyp;

    @Column(nullable = false)
    private String dateipfad;

    /**
     * Constructs a new Dokument with specified details.
     *
     * @param dokumentId   The unique identifier for the document.
     * @param wohnung     The Eigentum entity this document is associated with.
     * @param mieter       The Mieter entity this document is associated with.
     * @param dokumententyp The type of the document.
     * @param dateipfad    The file path where the document is stored.
     */

    public Dokument(Long dokumentId,
                    Wohnung wohnung,
                    Mieter mieter,
                    String dokumententyp,
                    String dateipfad) {
        this.dokumentId = dokumentId;
        this.wohnung = wohnung;
        this.mieter = mieter;
        this.dokumententyp = dokumententyp;
        this.dateipfad = dateipfad;
    }

    public Dokument() {
    }
    public Long getDokumentId() {
        return dokumentId;
    }

    public void setDokumentId(Long dokumentId) {
        this.dokumentId = dokumentId;
    }

    public Wohnung getEigentumId() {
        return wohnung;
    }

    public void setEigentumId(Wohnung wohnungId) {
        this.wohnung = wohnungId;
    }

    public Mieter getMieterId() {
        return mieter;
    }

    public void setMieterId(Mieter mieterId) {
        this.mieter = mieterId;
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
                "dokumentId=" + dokumentId +
                ", eigentum=" + (wohnung != null ? wohnung.getWohnungId() : "None") +
                ", mieter=" + (mieter != null ? mieter.getMieterID() : "None") +
                ", dokumententyp='" + dokumententyp + "/" +
                ", dateipfad='" + dateipfad + "/" +
                "]";
    }
}