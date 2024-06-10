package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import projektarbeit.immobilienverwaltung.validation.ValidMietPeriod;

import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Represents a rental agreement (Mietvertrag) entity.
 * This entity is mapped to the database table 'mietvertrag'.
 */
@ValidMietPeriod
@Entity
@Table(name = "mietvertrag")
public class Mietvertrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mietvertrag_id;

    @ManyToOne
    @JoinColumn(name = "mieterId", nullable = false)
    private Mieter mieter;

    @ManyToOne
    @JoinColumn(name = "wohnung_id", nullable = false)
    private Wohnung wohnung;

    @Column(nullable = false)
    @NotNull(message = "Mietbeginn cannot be null")
    private LocalDate mietbeginn;

    @Column
    private LocalDate mietende;

    @Column(nullable = false)
    @Positive(message = "Kaution must be positive")
    private double kaution;

    @Column(nullable = false)
    @Positive(message = "Miete must be positive")
    private double miete;

    @Column(nullable = false)
    @Min(value = 1, message = "AnzahlBewohner must be at least 1")
    private int anzahlBewohner;

    /**
     * Default constructor for JPA.
     */
    public Mietvertrag() {
    }

    /**
     * Constructs a new Mietvertrag with specified details.
     *
     * @param mieter     The tenant (Mieter) associated with the rental agreement.
     * @param wohnung    The property (Wohnung) associated with the rental agreement.
     * @param mietbeginn The start date of the rental agreement.
     * @param mietende   The end date of the rental agreement.
     * @param kaution    The deposit amount for the rental agreement.
     * @param miete      The monthly rent for the rental agreement.
     */
    public Mietvertrag(Mieter mieter,
                       Wohnung wohnung,
                       LocalDate mietbeginn,
                       LocalDate mietende,
                       double kaution,
                       double miete) {
        this.mieter = mieter;
        this.wohnung = wohnung;
        this.mietbeginn = mietbeginn;
        this.mietende = mietende;
        this.kaution = kaution;
        this.miete = miete;
    }

    /**
     * Returns the ID of the Mietvertrag (rental contract).
     *
     * @return the ID of the Mietvertrag
     */
    public Long getMietvertrag_id() {
        return mietvertrag_id;
    }

    /**
     * Sets the ID of the Mietvertrag (rental contract).
     *
     * @param mietvertrag_id the ID to set
     */
    public void setMietvertrag_id(Long mietvertrag_id) {
        this.mietvertrag_id = mietvertrag_id;
    }

    /**
     * Returns the Mieter (tenant) associated with this Mietvertrag.
     *
     * @return the Mieter associated with this Mietvertrag
     */
    public Mieter getMieter() {
        return mieter;
    }

    /**
     * Sets the Mieter (tenant) associated with this Mietvertrag.
     *
     * @param mieter the Mieter to set
     */
    public void setMieter(Mieter mieter) {
        this.mieter = mieter;
    }

    /**
     * Returns the Wohnung (apartment) associated with this Mietvertrag.
     *
     * @return the Wohnung associated with this Mietvertrag
     */
    public Wohnung getWohnung() {
        return wohnung;
    }

    /**
     * Sets the Wohnung (apartment) associated with this Mietvertrag.
     *
     * @param wohnung the Wohnung to set
     */
    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }

    /**
     * Returns the Miete (rent) for this Mietvertrag.
     *
     * @return the Miete for this Mietvertrag
     */
    public double getMiete() {
        return miete;
    }

    /**
     * Sets the Miete (rent) for this Mietvertrag.
     *
     * @param miete the Miete to set
     */
    public void setMiete(double miete) {
        this.miete = miete;
    }

    /**
     * Returns the start date of the tenancy.
     *
     * @return the start date of the tenancy
     */
    public LocalDate getMietbeginn() {
        return mietbeginn;
    }

    /**
     * Sets the start date of the tenancy.
     * The start date must be before the end date, if it is set.
     *
     * @param mietbeginn the start date to set
     * @throws IllegalArgumentException if the start date is after the end date
     */
    public void setMietbeginn(LocalDate mietbeginn) {
        if (mietende != null && mietbeginn != null && (mietbeginn.isAfter(mietende) || mietbeginn.isEqual(mietende))) throw new IllegalArgumentException("Mietbeginn must be before Mietende");
        this.mietbeginn = mietbeginn;
    }

    /**
     * Returns the end date of the tenancy.
     *
     * @return the end date of the tenancy
     */
    public LocalDate getMietende() {
        return mietende;
    }

    /**
     * Sets the end date of the tenancy.
     * The end date must be after the start date.
     *
     * @param mietende the end date to set
     * @throws IllegalArgumentException if the end date is before the start date
     */
    public void setMietende(LocalDate mietende) {
        if (mietbeginn != null && mietende != null && (mietende.isBefore(mietbeginn) || mietende.isEqual(mietbeginn))) throw new IllegalArgumentException("Mietbeginn must be before Mietende");
        this.mietende = mietende;
    }

    /**
     * Returns the security deposit amount.
     *
     * @return the security deposit amount
     */
    public double getKaution() {
        return kaution;
    }

    /**
     * Sets the security deposit amount.
     *
     * @param kaution the security deposit amount to set
     * @throws IllegalArgumentException if the security deposit is not positive
     */
    public void setKaution(double kaution) {
        if (kaution <= 0) throw new IllegalArgumentException("Kaution must be positive");
        this.kaution = kaution;
    }

    /**
     * Returns the number of residents.
     *
     * @return the number of residents
     */
    public int getAnzahlBewohner() {
        return anzahlBewohner;
    }

    /**
     * Sets the number of residents.
     *
     * @param anzahlBewohner the number of residents to set
     * @throws IllegalArgumentException if the number of residents is less than 1
     */
    public void setAnzahlBewohner(int anzahlBewohner) {
        if (anzahlBewohner < 1) throw new IllegalArgumentException("AnzahlBewohner must be at least 1");
        this.anzahlBewohner = anzahlBewohner;
    }

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
                '}';
    }
}