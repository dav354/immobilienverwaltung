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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "adresse_id", nullable = false)
    private Adresse adresse;

    @OneToMany(mappedBy = "wohnung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dokument> dokumente;

    @OneToMany(mappedBy = "wohnung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zaehlerstand> zaehlerstand;

    @ManyToOne
    @JoinColumn(name = "mieter_id")
    private Mieter mieter;

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
    public Wohnung() {
    }

    /**
     * Constructs a new Wohnung instance with specified details.
     *
     * @param adresse            The address of the property.
     * @param gesamtQuadratmeter The total area of the property in square meters.
     * @param baujahr            The year the property was built.
     * @param anzahlBaeder       The number of bathrooms in the property.
     * @param anzahlSchlafzimmer The number of bedrooms in the property.
     * @param hatBalkon          Whether the property has a balcony.
     * @param hatTerrasse        Whether the property has a terrace.
     * @param hatGarten          Whether the property has a garden.
     * @param hatKlimaanlage     Whether the property has air conditioning.
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

    public Mieter getMieter() {
        return mieter;
    }

    /**
     * Sets the Mieter for this Wohnung.
     * <p>
     * If the Wohnung is already associated with a Mieter, it will be removed from the current Mieter's list of Wohnungen.
     * Then, the new Mieter will be assigned to this Wohnung, and this Wohnung will be added to the new Mieter's list of Wohnungen.
     *
     * @param mieter The new Mieter to be assigned to this Wohnung. If null, the Wohnung will be disassociated from any Mieter.
     */
    public void setMieter(Mieter mieter) {
        if (this.mieter != null) {
            this.mieter.getWohnung().remove(this);  // Remove this Wohnung from the current Mieter's list
        }

        this.mieter = mieter;

        if (mieter != null) {
            mieter.getWohnung().add(this);  // Add this Wohnung to the new Mieter's list
        }
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

    /**
     * Returns the formatted address of the Wohnung (apartment).
     * <p>
     * The address format includes the country, postal code, city, street, and house number.
     * If the address or postal code object is null, it returns "Keine Adresse".
     *
     * @return A formatted string representing the address of the Wohnung, or "Keine Adresse" if the address or postal code object is null.
     */
    public String getFormattedAddress() {
        if (adresse != null && adresse.getPostleitzahlObj() != null) {
            Postleitzahl postleitzahlObj = adresse.getPostleitzahlObj();
            return String.format("%s %s %s %s %s",
                    postleitzahlObj.getLand().name(),
                    postleitzahlObj.getPostleitzahl(),
                    postleitzahlObj.getStadt(),
                    adresse.getStrasse(),
                    adresse.getHausnummer());
        }
        return "Keine Adresse";
    }

    /**
     * Returns a string representation of the Wohnung (apartment).
     */
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
                "', mieter='" + (mieter != null ? mieter.getFullName() : "keinen") +
                "']";
    }
}