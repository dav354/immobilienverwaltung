package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import projektarbeit.immobilienverwaltung.validation.ValidYear;

import java.time.LocalDate;
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
    @NotNull(message = "Adresse cannot be null")
    private Adresse adresse;

    @OneToMany(mappedBy = "wohnung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dokument> dokumente;

    @OneToMany(mappedBy = "wohnung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zaehlerstand> zaehlerstand;

    @ManyToOne
    @JoinColumn(name = "mieter_id")
    private Mieter mieter;

    @Column(nullable = false)
    @Min(value = 1, message = "GesamtQuadratmeter must be at least 1")
    private int gesamtQuadratmeter;

    @Positive(message = "Baujahr must be a four-digit year and not in the future")
    @ValidYear(message = "Baujahr must be a four-digit year and not in the future")
    private int baujahr;

    @Positive(message = "AnzahlBäder must be positive")
    private int anzahlBaeder;

    @PositiveOrZero(message = "AnzahlSchlafzimmer must be zero or positive")
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

    /**
     * Returns the unique identifier of the Wohnung.
     *
     * @return the unique identifier of the Wohnung
     */
    public Long getWohnung_id() {
        return wohnung_id;
    }

    /**
     * Sets the unique identifier of the Wohnung.
     *
     * @param wohnung_id the unique identifier of the Wohnung
     */
    public void setWohnung_id(Long wohnung_id) {
        this.wohnung_id = wohnung_id;
    }

    /**
     * Sets the unique identifier of the Wohnung.
     *
     * @param wohnung_id the unique identifier to set
     */
    public void setWohnungId(Long wohnung_id) {
        this.wohnung_id = wohnung_id;
    }

    /**
     * Returns the address associated with the Wohnung.
     *
     * @return the address of the Wohnung
     */
    public Adresse getAdresse() {
        return adresse;
    }

    /**
     * Sets the address associated with the Wohnung.
     *
     * @param adresse the address to set
     * @throws IllegalArgumentException if the address is null.
     */
    public void setAdresse(Adresse adresse) {
        if (adresse == null) {
            throw new IllegalArgumentException("Adresse cannot be null");
        }
        this.adresse = adresse;
    }

    /**
     * Returns the tenant (Mieter) associated with the Wohnung.
     *
     * @return the tenant of the Wohnung
     */
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

    /**
     * Returns the list of documents associated with the Wohnung.
     *
     * @return the list of documents
     */
    public List<Dokument> getDokumente() {
        return dokumente;
    }

    /**
     * Sets the list of documents associated with the Wohnung.
     *
     * @param dokumente the list of documents to set
     */
    public void setDokumente(List<Dokument> dokumente) {
        this.dokumente = dokumente;
    }

    /**
     * Returns the list of meter readings associated with the Wohnung.
     *
     * @return the list of meter readings
     */
    public List<Zaehlerstand> getZaehlerstand() {
        return zaehlerstand;
    }

    /**
     * Sets the list of meter readings associated with the Wohnung.
     *
     * @param zaehlerstand the list of meter readings to set
     */
    public void setZaehlerstand(List<Zaehlerstand> zaehlerstand) {
        this.zaehlerstand = zaehlerstand;
    }

    /**
     * Returns the total square meters of the Wohnung.
     *
     * @return the total square meters
     */
    public int getGesamtQuadratmeter() {
        return gesamtQuadratmeter;
    }

    /**
     * Sets the total square meters of the Wohnung.
     *
     * @param gesamtQuadratmeter the total square meters to set
     * @throws IllegalArgumentException if the total square meters is less than 1.
     */
    public void setGesamtQuadratmeter(int gesamtQuadratmeter) {
        if (gesamtQuadratmeter < 1) {
            throw new IllegalArgumentException("GesamtQuadratmeter must be at least 1");
        }
        this.gesamtQuadratmeter = gesamtQuadratmeter;
    }

    /**
     * Returns the year of construction of the Wohnung.
     *
     * @return the year of construction
     */
    public int getBaujahr() {
        return baujahr;
    }

    /**
     * Sets the year of construction of the Wohnung.
     *
     * @param baujahr the year of construction to set
     * @throws IllegalArgumentException if the construction year is not positive.
     */
    public void setBaujahr(int baujahr) {
        if (baujahr < 1000 || baujahr > LocalDate.now().getYear())  throw new IllegalArgumentException("Baujahr must be a four-digit year and not in the future");
        this.baujahr = baujahr;
    }

    /**
     * Returns the number of bathrooms in the Wohnung.
     *
     * @return the number of bathrooms
     */
    public int getAnzahlBaeder() {
        return anzahlBaeder;
    }

    /**
     * Sets the number of bathrooms in the Wohnung.
     *
     * @param anzahlBaeder the number of bathrooms to set
     * @throws IllegalArgumentException if the number of bathrooms is not positive.
     */
    public void setAnzahlBaeder(int anzahlBaeder) {
        if (anzahlBaeder <= 0) {
            throw new IllegalArgumentException("AnzahlBaeder must be positive");
        }
        this.anzahlBaeder = anzahlBaeder;
    }

    /**
     * Returns the number of bedrooms in the Wohnung.
     *
     * @return the number of bedrooms
     */
    public int getAnzahlSchlafzimmer() {
        return anzahlSchlafzimmer;
    }

    /**
     * Sets the number of bedrooms in the Wohnung.
     *
     * @param anzahlSchlafzimmer the number of bedrooms to set
     * @throws IllegalArgumentException if the number of bedrooms is negative.
     */
    public void setAnzahlSchlafzimmer(int anzahlSchlafzimmer) {
        if (anzahlSchlafzimmer < 0) {
            throw new IllegalArgumentException("AnzahlSchlafzimmer must be zero or positive");
        }
        this.anzahlSchlafzimmer = anzahlSchlafzimmer;
    }

    /**
     * Returns whether the Wohnung has a balcony.
     *
     * @return true if the Wohnung has a balcony, false otherwise
     */
    public boolean isHatBalkon() {
        return hatBalkon;
    }

    /**
     * Sets whether the Wohnung has a balcony.
     *
     * @param hatBalkon true if the Wohnung has a balcony, false otherwise
     */
    public void setHatBalkon(boolean hatBalkon) {
        this.hatBalkon = hatBalkon;
    }

    /**
     * Returns whether the Wohnung has a terrace.
     *
     * @return true if the Wohnung has a terrace, false otherwise
     */
    public boolean isHatTerrasse() {
        return hatTerrasse;
    }

    /**
     * Sets whether the Wohnung has a terrace.
     *
     * @param hatTerrasse true if the Wohnung has a terrace, false otherwise
     */
    public void setHatTerrasse(boolean hatTerrasse) {
        this.hatTerrasse = hatTerrasse;
    }

    /**
     * Returns whether the Wohnung has a garden.
     *
     * @return true if the Wohnung has a garden, false otherwise
     */
    public boolean isHatGarten() {
        return hatGarten;
    }

    /**
     * Sets whether the Wohnung has a garden.
     *
     * @param hatGarten true if the Wohnung has a garden, false otherwise
     */
    public void setHatGarten(boolean hatGarten) {
        this.hatGarten = hatGarten;
    }

    /**
     * Returns whether the Wohnung has air conditioning.
     *
     * @return true if the Wohnung has air conditioning, false otherwise
     */
    public boolean isHatKlimaanlage() {
        return hatKlimaanlage;
    }

    /**
     * Sets whether the Wohnung has air conditioning.
     *
     * @param hatKlimaanlage true if the Wohnung has air conditioning, false otherwise
     */
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
     * Returns a string representation of this Wohnung.
     *
     * @return a string representation of this Wohnung.
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