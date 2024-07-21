package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import projektarbeit.immobilienverwaltung.validation.ValidMietPeriod;

import java.time.LocalDate;

/**
 * Repräsentiert eine Mietvertrags-Entität (Mietvertrag).
 * Diese Entität wird der Datenbanktabelle 'mietvertrag' zugeordnet.
 */
@ValidMietPeriod
@Entity
@Table(name = "mietvertrag", indexes = {
        @Index(name = "idx_mietvertrag_mieter_id", columnList = "mieter_id"),
        @Index(name = "idx_mietvertrag_wohnung_id", columnList = "wohnung_id")
})
public class Mietvertrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mietvertrag_id;

    @ManyToOne
    @JoinColumn(name = "mieterId", nullable = false)
    private Mieter mieter;

    @OneToOne
    @JoinColumn(name = "wohnung_id", nullable = false)
    private Wohnung wohnung;

    @Column
    @NotNull(message = "Mietbeginn darf nicht null sein")
    private LocalDate mietbeginn;

    @Column
    private LocalDate mietende;

    @Column
    @NotNull(message = "Kaution darf nicht leer sein")
    @Positive(message = "Kaution muss positiv sein")
    private double kaution;

    @Column
    @NotNull(message = "Miete darf nicht leer sein")
    @Positive(message = "Miete muss positiv sein")
    private double miete;

    @Column
    @NotNull(message = "AnzahlBewohner darf nicht leer sein")
    @Min(value = 1, message = "AnzahlBewohner muss mindestens 1 sein")
    private int anzahlBewohner;

    /**
     * Standardkonstruktor für JPA.
     */
    public Mietvertrag() {
    }

    /**
     * Erstellt einen neuen Mietvertrag mit den angegebenen Details.
     *
     * @param mieter         Der Mieter (Mieter), der mit dem Mietvertrag verknüpft ist.
     * @param wohnung        Die Immobilie (Wohnung), die mit dem Mietvertrag verknüpft ist.
     * @param mietbeginn     Das Startdatum des Mietvertrags.
     * @param mietende       Das Enddatum des Mietvertrags.
     * @param kaution        Die Kautionssumme für den Mietvertrag.
     * @param miete          Die monatliche Miete für den Mietvertrag.
     * @param anzahlBewohner Die Anzahl der Bewohner.
     */
    public Mietvertrag(Mieter mieter,
                       Wohnung wohnung,
                       LocalDate mietbeginn,
                       LocalDate mietende,
                       double kaution,
                       double miete,
                       int anzahlBewohner) {
        this.mieter = mieter;
        this.wohnung = wohnung;
        this.mietbeginn = mietbeginn;
        this.mietende = mietende;
        this.kaution = kaution;
        this.miete = miete;
        this.anzahlBewohner = anzahlBewohner;
    }

    /**
     * Gibt die ID des Mietvertrags zurück.
     *
     * @return die Mietvertrag-ID
     */
    public Long getMietvertrag_id() {
        return mietvertrag_id;
    }

    /**
     * Setzt die ID des Mietvertrags.
     *
     * @param mietvertrag_id die Mietvertrag-ID
     */
    public void setMietvertrag_id(Long mietvertrag_id) {
        this.mietvertrag_id = mietvertrag_id;
    }

    /**
     * Gibt den Mieter zurück, der mit diesem Mietvertrag verknüpft ist.
     *
     * @return der Mieter
     */
    public Mieter getMieter() {
        return mieter;
    }

    /**
     * Setzt den Mieter, der mit diesem Mietvertrag verknüpft ist.
     *
     * @param mieter der Mieter
     */
    public void setMieter(Mieter mieter) {
        this.mieter = mieter;
    }

    /**
     * Gibt die Wohnung zurück, die mit diesem Mietvertrag verknüpft ist.
     *
     * @return die Wohnung
     */
    public Wohnung getWohnung() {
        return wohnung;
    }

    /**
     * Setzt die Wohnung, die mit diesem Mietvertrag verknüpft ist.
     *
     * @param wohnung die Wohnung
     */
    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }

    /**
     * Gibt die Miete für diesen Mietvertrag zurück.
     *
     * @return die Miete
     */
    public double getMiete() {
        return miete;
    }

    /**
     * Setzt die Miete für diesen Mietvertrag.
     *
     * @param miete die Miete
     */
    public void setMiete(double miete) {
        this.miete = miete;
    }

    /**
     * Gibt den Mietbeginn zurück.
     *
     * @return der Mietbeginn
     */
    public LocalDate getMietbeginn() {
        return mietbeginn;
    }

    /**
     * Setzt den Mietbeginn.
     *
     * @param mietbeginn der Mietbeginn
     */
    public void setMietbeginn(LocalDate mietbeginn) {
        this.mietbeginn = mietbeginn;
    }

    /**
     * Gibt das Mietende zurück.
     *
     * @return das Mietende
     */
    public LocalDate getMietende() {
        return mietende;
    }

    /**
     * Setzt das Mietende.
     *
     * @param mietende das Mietende
     */
    public void setMietende(LocalDate mietende) {
        this.mietende = mietende;
    }

    /**
     * Gibt die Kaution zurück.
     *
     * @return die Kaution
     */
    public double getKaution() {
        return kaution;
    }

    /**
     * Setzt die Kaution.
     *
     * @param kaution die Kaution
     */
    public void setKaution(double kaution) {
        this.kaution = kaution;
    }

    /**
     * Gibt die Anzahl der Bewohner zurück.
     *
     * @return die Anzahl der Bewohner
     */
    public int getAnzahlBewohner() {
        return anzahlBewohner;
    }

    /**
     * Setzt die Anzahl der Bewohner.
     *
     * @param anzahlBewohner die Anzahl der Bewohner
     */
    public void setAnzahlBewohner(int anzahlBewohner) {
        this.anzahlBewohner = anzahlBewohner;
    }

    /**
     * Gibt eine String-Darstellung des Mietvertrags zurück.
     *
     * @return eine String-Darstellung dieses Mietvertrags
     */
    @Override
    public String toString() {
        return "Mietvertrag{" +
                "mietvertrag_id=" + mietvertrag_id +
                ", mieter=" + mieter.getFullName() +
                ", wohnung=" + wohnung.getFormattedAddress() +
                ", mietbeginn=" + mietbeginn +
                ", mietende=" + mietende +
                ", kaution=" + kaution +
                ", miete=" + miete +
                ", anzahlBewohner=" + anzahlBewohner +
                '}';
    }
}