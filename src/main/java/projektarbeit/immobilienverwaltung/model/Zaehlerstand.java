package projektarbeit.immobilienverwaltung.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "zaehlerstand")
public class Zaehlerstand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zaehlerstandId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eigentumID", nullable = false)
    private Wohnung wohnung;

    @Temporal(TemporalType.DATE)
    @PastOrPresent
    @Column(nullable = false)
    private Date ablesedatum;

    @DecimalMin("0.0")
    @Column(nullable = false)
    private double ablesewert;

    /**
     * Constructs a new meter reading record.
     *
     * @param zaehlerstandId the unique identifier for the meter reading
     * @param wohnung the ID of the property to which the reading belongs
     * @param ablesedatum the date when the meter reading was taken
     * @param ablesewert the value of the meter reading
     */
    public Zaehlerstand(Long zaehlerstandId,
                        Wohnung wohnung,
                        Date ablesedatum,
                        double ablesewert) {
        this.zaehlerstandId = zaehlerstandId;
        this.wohnung = wohnung;
        this.ablesedatum = ablesedatum;
        this.ablesewert = ablesewert;
    }

    public Zaehlerstand() {}

    public Long getZaehlerstandId() {
        return zaehlerstandId;
    }

    public void setZaehlerstandId(Long zaehlerstandId) {
        this.zaehlerstandId = zaehlerstandId;
    }

    public Wohnung getEigentumID() {
        return wohnung;
    }

    public void setEigentumID(Wohnung wohnung) {
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
        if(ablesewert < 0) throw new IllegalArgumentException("Ablesewert must be positive.");
        this.ablesewert = ablesewert;
    }

    @Override
    public String toString() {
        return "Zaehlerstand[" +
                "zaehlerstandId=" + zaehlerstandId +
                ", eigentumID=" + wohnung +
                ", ablesedatum=" + ablesedatum +
                ", ablesewert=" + ablesewert +
                "]";
    }
}