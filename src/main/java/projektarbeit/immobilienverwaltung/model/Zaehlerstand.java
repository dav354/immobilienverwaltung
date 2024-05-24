package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * Represents a meter reading (Zaehlerstand) entity associated with a property (Wohnung).
 * This entity is mapped to the database table 'zaehlerstand'.
 */
@Entity
@Table(name = "zaehlerstand")
public class Zaehlerstand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zaehlerstandId;

    @ManyToOne
    @JoinColumn(name = "wohnung_id", nullable = false)
    @NotNull(message = "Wohnung cannot be null")
    private Wohnung wohnung;

    @Column(nullable = false)
    @NotNull(message = "Ablesedatum cannot be null")
    private LocalDate ablesedatum;

    @Column(nullable = false)
    @Positive(message = "Ablesewert cannot be null")
    private double ablesewert;

    /**
     * Constructs a new Zaehlerstand instance with specified details.
     *
     * @param wohnung      The property (Wohnung) to which the reading belongs.
     * @param ablesedatum  The date when the meter reading was taken.
     * @param ablesewert   The value of the meter reading.
     */
    public Zaehlerstand(Wohnung wohnung,
                        LocalDate ablesedatum,
                        double ablesewert) {
        this.wohnung = wohnung;
        this.ablesedatum = ablesedatum;
        this.ablesewert = ablesewert;
    }

    /**
     * Default constructor for JPA.
     */
    public Zaehlerstand() {
    }

    /**
     * Returns the ID of the meter reading.
     *
     * @return the meter reading ID
     */
    public Long getZaehlerstandId() {
        return zaehlerstandId;
    }

    /**
     * Sets the ID of the meter reading.
     *
     * @param zaehlerstandId the meter reading ID to set
     */
    public void setZaehlerstandId(Long zaehlerstandId) {
        this.zaehlerstandId = zaehlerstandId;
    }

    /**
     * Returns the property (Wohnung) associated with the meter reading.
     *
     * @return the property associated with the meter reading
     */
    public Wohnung getWohnung() {
        return wohnung;
    }

    /**
     * Sets the property (Wohnung) associated with the meter reading.
     *
     * @param wohnung the property to set
     * @throws IllegalArgumentException if the Wohnung is null.
     */
    public void setWohnung(Wohnung wohnung) {
        if (wohnung == null) throw new IllegalArgumentException("Wohnung cannot be null");
        this.wohnung = wohnung;
    }

    /**
     * Returns the date of the meter reading.
     *
     * @return the date of the meter reading
     */
    public LocalDate getAblesedatum() {
        return ablesedatum;
    }

    /**
     * Sets the date of the meter reading.
     *
     * @param ablesedatum the date to set
     * @throws IllegalArgumentException if the date is null.
     */
    public void setAblesedatum(LocalDate ablesedatum) {
        if (ablesedatum == null) throw new IllegalArgumentException("Ablesedatum cannot be null");
        this.ablesedatum = ablesedatum;
    }

    /**
     * Returns the meter reading value.
     *
     * @return the meter reading value
     */
    public double getAblesewert() {
        return ablesewert;
    }

    /**
     * Sets the meter reading value.
     * Ensures the reading value is positive.
     *
     * @param ablesewert the meter reading value to set
     * @throws IllegalArgumentException if the value is not positive.
     */
    public void setAblesewert(double ablesewert) {
        if (ablesewert <= 0) throw new IllegalArgumentException("Ablesewert must be positive");
        this.ablesewert = ablesewert;
    }

    /**
     * Returns a string representation of this Zaehlerstand.
     *
     * @return a string representation of this Zaehlerstand.
     */
    @Override
    public String toString() {
        return "Zaehlerstand[" +
                "zaehlerstandId='" + zaehlerstandId +
                "', eigentumID='" + wohnung +
                "', ablesedatum='" + ablesedatum +
                "', ablesewert='" + ablesewert +
                "']";
    }
}