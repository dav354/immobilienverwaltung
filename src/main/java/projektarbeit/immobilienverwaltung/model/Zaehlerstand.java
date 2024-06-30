package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * Repräsentiert eine Zählerstand-Entität, die einer Immobilie (Wohnung) zugeordnet ist.
 * Diese Entität wird der Datenbanktabelle 'zaehlerstand' zugeordnet.
 */
@SuppressWarnings("SpellCheckingInspection")
@Entity
@Table(name = "zaehlerstand")
public class Zaehlerstand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zaehlerstandId;

    @ManyToOne
    @JoinColumn(name = "wohnung_id", nullable = false)
    @NotNull(message = "Wohnung darf nicht null sein")
    private Wohnung wohnung;

    @Column
    @NotNull(message = "Ablesedatum darf nicht null sein")
    private LocalDate ablesedatum;

    @Column
    @Positive(message = "Ablesewert muss positiv sein")
    @NotNull(message = "Ablesewert darf nicht null sein")
    private double ablesewert;

    @Column
    @NotNull(message = "Name darf nicht null sein")
    @NotBlank(message = "Name darf nicht null sein")
    private String name;

    /**
     * Standardkonstruktor für JPA.
     */
    public Zaehlerstand() {
    }

    /**
     * Erstellt eine neue Zaehlerstand-Instanz mit den angegebenen Details.
     *
     * @param wohnung     Die Immobilie (Wohnung), zu der die Ablesung gehört.
     * @param ablesedatum Das Datum, an dem die Zählerstandablesung durchgeführt wurde.
     * @param ablesewert  Der Wert der Zählerstandablesung.
     * @param name        Der Name des Zählers.
     */
    public Zaehlerstand(Wohnung wohnung,
                        LocalDate ablesedatum,
                        double ablesewert,
                        String name) {
        this.wohnung = wohnung;
        this.ablesedatum = ablesedatum;
        this.ablesewert = ablesewert;
        this.name = name;
    }

    /**
     * Gibt die ID des Zählerstands zurück.
     *
     * @return die Zählerstand-ID
     */
    public Long getZaehlerstandId() {
        return zaehlerstandId;
    }

    /**
     * Setzt die ID des Zählerstands.
     *
     * @param zaehlerstandId die Zählerstand-ID
     */
    public void setZaehlerstandId(Long zaehlerstandId) {
        this.zaehlerstandId = zaehlerstandId;
    }

    /**
     * Gibt die Wohnung zurück, die mit diesem Zählerstand verknüpft ist.
     *
     * @return die verknüpfte Wohnung
     */
    public Wohnung getWohnung() {
        return wohnung;
    }

    /**
     * Setzt die Wohnung, die mit diesem Zählerstand verknüpft ist.
     *
     * @param wohnung die zu verknüpfende Wohnung
     */
    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }

    /**
     * Gibt das Ablesedatum zurück.
     *
     * @return das Ablesedatum
     */
    public LocalDate getAblesedatum() {
        return ablesedatum;
    }

    /**
     * Setzt das Ablesedatum.
     *
     * @param ablesedatum das zu setzende Ablesedatum
     */
    public void setAblesedatum(LocalDate ablesedatum) {
        this.ablesedatum = ablesedatum;
    }

    /**
     * Gibt den Ablesewert zurück.
     *
     * @return der Ablesewert
     */
    public double getAblesewert() {
        return ablesewert;
    }

    /**
     * Setzt den Ablesewert.
     *
     * @param ablesewert der zu setzende Ablesewert
     */
    public void setAblesewert(double ablesewert) {
        this.ablesewert = ablesewert;
    }

    /**
     * Gibt den Namen des Zählers zurück.
     *
     * @return der Name des Zählers
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Zählers.
     *
     * @param name der zu setzende Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt eine String-Darstellung dieses Zählerstands zurück.
     *
     * @return eine String-Darstellung dieses Zählerstands.
     */
    @Override
    public String toString() {
        return "Zaehlerstand[" +
                "zaehlerstandId='" + zaehlerstandId +
                "', wohnung='" + wohnung +
                "', ablesedatum='" + ablesedatum +
                "', ablesewert='" + ablesewert +
                "', name='" + name +
                "']";
    }
}