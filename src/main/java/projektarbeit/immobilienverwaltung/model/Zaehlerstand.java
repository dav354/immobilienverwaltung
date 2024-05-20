package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import java.sql.Date;

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
    private Wohnung wohnung;

    @Temporal(TemporalType.DATE)
    @PastOrPresent
    @Column(nullable = false)
    private Date ablesedatum;

    @DecimalMin("0.0")
    @Column(nullable = false)
    private double ablesewert;

    /**
     * Constructs a new Zaehlerstand instance with specified details.
     *
     * @param wohnung      The property (Wohnung) to which the reading belongs.
     * @param ablesedatum  The date when the meter reading was taken.
     * @param ablesewert   The value of the meter reading.
     */
    public Zaehlerstand(Wohnung wohnung, Date ablesedatum, double ablesewert) {
        this.wohnung = wohnung;
        this.ablesedatum = ablesedatum;
        this.ablesewert = ablesewert;
    }

    /**
     * Default constructor for JPA.
     */
    public Zaehlerstand() {
    }

    public Long getZaehlerstandId() {
        return zaehlerstandId;
    }

    public void setZaehlerstandId(Long zaehlerstandId) {
        this.zaehlerstandId = zaehlerstandId;
    }

    public Wohnung getWohnung() {
        return wohnung;
    }

    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
    }

    public Date getAblesedatum() {
        return ablesedatum;
    }

    public void setAblesedatum(Date ablesedatum) {
        this.ablesedatum = ablesedatum;
    }

    public double getAblesewert() {
        return ablesewert;
    }

    public void setAblesewert(double ablesewert) {
        if (ablesewert < 0) throw new IllegalArgumentException("Ablesewert must be positive.");
        this.ablesewert = ablesewert;
    }

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