package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

/**
 * Represents a property (Wohnung) entity with details about the property such as address, size, number of rooms, and amenities.
 * This entity is mapped to the database table 'wohnung'.
 */
@Entity
@Table(name = "wohnung")
public class Wohnung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wohnung_id;

    @OneToOne
    @JoinColumn(name = "adresse_id", referencedColumnName = "adresse_id", nullable = false)
    private Adresse adresse;

    @OneToMany(mappedBy = "wohnung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dokument> dokumente;

    @OneToMany(mappedBy = "wohnung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zaehlerstand> zaehlerstand;

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
     * Default constructor for JPA.
     */
    public Wohnung() {}

    /**
     * Constructs a new Wohnung instance with specified details.
     *
     * @param adresse           The address of the property.
     * @param gesamtQuadratmeter The total area of the property in square meters.
     * @param baujahr           The year the property was built.
     * @param anzahlBaeder      The number of bathrooms in the property.
     * @param anzahlSchlafzimmer The number of bedrooms in the property.
     * @param hatBalkon         Whether the property has a balcony.
     * @param hatTerrasse       Whether the property has a terrace.
     * @param hatGarten         Whether the property has a garden.
     * @param hatKlimaanlage    Whether the property has air conditioning.
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

    // Getter und Setter
    public Long getWohnung_id() {
        return wohnung_id;
    }

    public void setWohnungId(Long wohnung_id) {
        this.wohnung_id = wohnung_id;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public List<Dokument> getDokumente() {
        return dokumente;
    }

    public void setDokumente(List<Dokument> dokumente) {
        this.dokumente = dokumente;
    }

    public List<Zaehlerstand> getZaehlerstand() {
        return zaehlerstand;
    }

    public void setZaehlerstand(List<Zaehlerstand> zaehlerstand) {
        this.zaehlerstand = zaehlerstand;
    }

    public int getGesamtQuadratmeter() {
        return gesamtQuadratmeter;
    }

    public void setGesamtQuadratmeter(int gesamtQuadratmeter) {
        this.gesamtQuadratmeter = gesamtQuadratmeter;
    }

    public int getBaujahr() {
        return baujahr;
    }

    public void setBaujahr(int baujahr) {
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
        return "Wohnung[" +
                "wohnung_id='" + wohnung_id +
                "', adresse='" + adresse +
                "', gesamtQuadratmeter='" + gesamtQuadratmeter +
                "', baujahr='" + baujahr +
                "', anzahlBaeder='" + anzahlBaeder +
                "', anzahlSchlafzimmer='" + anzahlSchlafzimmer +
                "', hatBalkon='" + hatBalkon +
                "', hatTerrasse='" + hatTerrasse +
                "', hatGarten='" + hatGarten +
                "', hatKlimaanlage='" + hatKlimaanlage +
                "']";
    }
}