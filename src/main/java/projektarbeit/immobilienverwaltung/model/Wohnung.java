package projektarbeit.immobilienverwaltung.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "eigentum", indexes = {
        @Index(name = "idx_eigentum_adresse", columnList = "address_id"),
        @Index(name = "idx_eigentum_baujahr", columnList = "baujahr")
})

public class Wohnung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wohnungId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Adresse adresse;

    @Min(1)
    @Column(nullable = false)
    private int gesamtQuadratmeter;

    @Min(0)
    private int baujahr;

    @Min(0)
    @Max(10)
    private int anzahlBaeder;

    @Min(0)
    @Max(10)
    private int anzahlSchlafzimmer;

    private boolean hatBalkon;

    private boolean hatTerrasse;

    private boolean hatGarten;

    private boolean hatKlimaanlage;

    /**
     * Constructs a new Eigentum with specified details.
     *
     * @param adresse the address of the property
     * @param gesamtQuadratmeter the total area of the property in square meters
     * @param baujahr the year the property was built
     * @param anzahlBaeder the number of bathrooms in the property
     * @param anzahlSchlafzimmer the number of bedrooms in the property
     * @param hatBalkon whether the property has a balcony
     * @param hatTerrasse whether the property has a terrace
     * @param hatGarten whether the property has a garden
     * @param hatKlimaanlage whether the property has air conditioning
     */

    public Wohnung(Adresse adresse,
                   int gesamtQuadratmeter,
                   int baujahr,
                   int anzahlBaeder,
                   int anzahlSchlafzimmer,
                   boolean hatBalkon,
                   boolean hatTerrasse,
                   boolean hatGarten,
                   boolean hatKlimaanlage) {
        this.adresse = adresse;
        this.gesamtQuadratmeter = gesamtQuadratmeter;
        this.baujahr = baujahr;
        this.anzahlBaeder = anzahlBaeder;
        this.anzahlSchlafzimmer = anzahlSchlafzimmer;
        this.hatBalkon = hatBalkon;
        this.hatTerrasse = hatTerrasse;
        this.hatGarten = hatGarten;
        this.hatKlimaanlage = hatKlimaanlage;
    }

    public Wohnung() {}

    // Getter und Setter
    public Long getWohnungId() {
        return wohnungId;
    }

    public void setWohnungId(Long wohnungId) {
        this.wohnungId = wohnungId;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public int getGesamtQuadratmeter() {
        return gesamtQuadratmeter;
    }

    public void setGesamtQuadratmeter(int gesamtQuadratmeter) {
        if (gesamtQuadratmeter <= 0) {
            throw new IllegalArgumentException("GesamtQuadratmeter must be greater than zero.");
        }
        this.gesamtQuadratmeter = gesamtQuadratmeter;
    }
    public int getBaujahr() {
        return baujahr;
    }

    public void setBaujahr(int baujahr) {
        if (baujahr > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Baujahr cannot be in the future.");
        }
        this.baujahr = baujahr;
    }
    public int getAnzahlBaeder() {
        return anzahlBaeder;
    }

    public void setAnzahlBaeder(int anzahlBaeder) {
        this.anzahlBaeder = anzahlBaeder;
    }

    public int getAnzahlSchlafzimmer() {
        return anzahlSchlafzimmer;
    }

    public void setAnzahlSchlafzimmer(int anzahlSchlafzimmer) {
        this.anzahlSchlafzimmer = anzahlSchlafzimmer;
    }

    public boolean isHatBalkon() {
        return hatBalkon;
    }

    public void setHatBalkon(boolean hatBalkon) {
        this.hatBalkon = hatBalkon;
    }

    public boolean isHatTerrasse() {
        return hatTerrasse;
    }

    public void setHatTerrasse(boolean hatTerrasse) {
        this.hatTerrasse = hatTerrasse;
    }

    public boolean isHatGarten() {
        return hatGarten;
    }

    public void setHatGarten(boolean hatGarten) {
        this.hatGarten = hatGarten;
    }

    public boolean isHatKlimaanlage() {
        return hatKlimaanlage;
    }

    public void setHatKlimaanlage(boolean hatKlimaanlage) {
        this.hatKlimaanlage = hatKlimaanlage;
    }

    @Override
    public String toString() {
        return String.format("Eigentum[Adresse='%s', " +
                "GesamtQuadratmeter=%d" +
                "Baujahr=%d" +
                "AnzahlBaeder=%d" +
                "AnzahlSchlafzimmer=%d" +
                "Balkon=%b" +
                "Terrasse=%b" +
                "Garten=%b" +
                "Klimaanlage=%b]",
                getAdresse().toString(),
                getGesamtQuadratmeter(),
                getBaujahr(),
                getAnzahlBaeder(),
                getAnzahlSchlafzimmer(),
                isHatBalkon(),
                isHatTerrasse(),
                isHatGarten(),
                isHatKlimaanlage()
                );
    }
}