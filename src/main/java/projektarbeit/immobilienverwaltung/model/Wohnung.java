package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import projektarbeit.immobilienverwaltung.validation.ValidYear;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a property (Wohnung) entity with details about the property such as address, size, number of rooms, and amenities.
 * This entity is mapped to the database table 'wohnung'.
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
@Entity
@Table(name = "wohnung")
public class Wohnung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wohnung_id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Illegal Strasse")
    @Size(max = 100, message = "Illegal Strasse")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Illegal Strasse")
    private String strasse;

    @Column(nullable = false, length = 6)
    @NotBlank(message = "Illegal Hausnummer")
    @Size(max = 6, message = "Illegal Hausnummer")
    @Pattern(regexp = "^\\d+[a-zA-Z]?$", message = "Illegal Hausnummer")
    private String hausnummer;

    @Column(nullable = false)
    @NotNull(message = "Postleitzahl cannot be blank")
    @Pattern(regexp = "^\\d{4,10}$", message = "Postleitzahl must contain only numbers")
    private String postleitzahl;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Illegal Stadt")
    @Size(max = 100, message = "Illegal Stadt")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Illegal Stadt")
    private String stadt;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Land cannot be null")
    private Land land;

    @OneToMany(mappedBy = "wohnung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dokument> dokumente;

    @OneToMany(mappedBy = "wohnung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zaehlerstand> zaehlerstand;

    @OneToMany(mappedBy = "wohnung", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Mietvertrag> mietvertraege = new ArrayList<>();

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

    @Transient
    private List<Wohnung> subWohnungen = new ArrayList<>();

    @Transient
    private boolean isHeader = false;

    @Column()
    @Pattern(regexp = "^[0-9]*$", message = "Illegal Stockwerk")
    private String stockwerk;

    @Column()
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Illegal Wohnungsnummer")
    private String wohnungsnummer;

    /**
     * Default constructor for JPA.
     */
    public Wohnung() {
    }

    /**
     * Constructs a new Wohnung instance with specified details.
     *
     * @param strasse            The street name of the property.
     * @param hausnummer         The house number of the property.
     * @param postleitzahl       The postal code of the property.
     * @param stadt              The city of the property.
     * @param land               The country of the property.
     * @param gesamtQuadratmeter The total area of the property in square meters.
     * @param baujahr            The year the property was built.
     * @param anzahlBaeder       The number of bathrooms in the property.
     * @param anzahlSchlafzimmer The number of bedrooms in the property.
     * @param hatBalkon          Whether the property has a balcony.
     * @param hatTerrasse        Whether the property has a terrace.
     * @param hatGarten          Whether the property has a garden.
     * @param hatKlimaanlage     Whether the property has air conditioning.
     * @param stockwerk          The floor on which the property is located.
     * @param wohnungsnummer     The apartment number of the property.
     */
    public Wohnung(String strasse,
                   String hausnummer,
                   String postleitzahl,
                   String stadt,
                   Land land,
                   int gesamtQuadratmeter,
                   int baujahr,
                   int anzahlBaeder,
                   int anzahlSchlafzimmer,
                   boolean hatBalkon,
                   boolean hatTerrasse,
                   boolean hatGarten,
                   boolean hatKlimaanlage,
                   String stockwerk,
                   String wohnungsnummer) {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.postleitzahl = postleitzahl;
        this.stadt = stadt;
        this.land = land;
        this.gesamtQuadratmeter = gesamtQuadratmeter;
        this.baujahr = baujahr;
        this.anzahlBaeder = anzahlBaeder;
        this.anzahlSchlafzimmer = anzahlSchlafzimmer;
        this.hatBalkon = hatBalkon;
        this.hatTerrasse = hatTerrasse;
        this.hatGarten = hatGarten;
        this.hatKlimaanlage = hatKlimaanlage;
        this.stockwerk = stockwerk;
        this.wohnungsnummer = wohnungsnummer;
    }

    /**
     * Returns the ID of the Wohnung (apartment).
     *
     * @return the ID of the Wohnung
     */
    public Long getWohnung_id() {
        return wohnung_id;
    }

    /**
     * Sets the ID of the Wohnung (apartment).
     *
     * @param wohnung_id the ID to set
     */
    public void setWohnung_id(Long wohnung_id) {
        this.wohnung_id = wohnung_id;
    }

    /**
     * Returns the street name of the Wohnung (apartment).
     *
     * @return the street name of the Wohnung
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Sets the street name of the Wohnung (apartment).
     *
     * @param strasse the street name to set
     */
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    /**
     * Returns the house number of the Wohnung (apartment).
     *
     * @return the house number of the Wohnung
     */
    public String getHausnummer() {
        return hausnummer;
    }

    /**
     * Sets the house number of the Wohnung (apartment).
     *
     * @param hausnummer the house number to set
     */
    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    /**
     * Returns the postal code of the Wohnung (apartment).
     *
     * @return the postal code of the Wohnung
     */
    public String getPostleitzahl() {
        return postleitzahl;
    }

    /**
     * Sets the postal code of the Wohnung (apartment).
     *
     * @param postleitzahl the postal code to set
     */
    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    /**
     * Returns the city of the Wohnung (apartment).
     *
     * @return the city of the Wohnung
     */
    public String getStadt() {
        return stadt;
    }

    /**
     * Sets the city of the Wohnung (apartment).
     *
     * @param stadt the city to set
     */
    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    /**
     * Returns the country of the Wohnung (apartment).
     *
     * @return the country of the Wohnung
     */
    public Land getLand() {
        return land;
    }

    /**
     * Sets the country of the Wohnung (apartment).
     *
     * @param land the country to set
     */
    public void setLand(Land land) {
        this.land = land;
    }

    /**
     * Returns the list of Dokument (documents) associated with the Wohnung (apartment).
     *
     * @return the list of Dokumente
     */
    public List<Dokument> getDokumente() {
        return dokumente;
    }

    /**
     * Sets the list of Dokument (documents) associated with the Wohnung (apartment).
     *
     * @param dokumente the list of Dokumente to set
     */
    public void setDokumente(List<Dokument> dokumente) {
        this.dokumente = dokumente;
    }

    /**
     * Returns the list of Zaehlerstand (meter readings) associated with the Wohnung (apartment).
     *
     * @return the list of Zaehlerstande
     */
    public List<Zaehlerstand> getZaehlerstand() {
        return zaehlerstand;
    }

    /**
     * Sets the list of Zaehlerstand (meter readings) associated with the Wohnung (apartment).
     *
     * @param zaehlerstand the list of Zaehlerstande to set
     */
    public void setZaehlerstand(List<Zaehlerstand> zaehlerstand) {
        this.zaehlerstand = zaehlerstand;
    }

    /**
     * Returns the total square meters of the Wohnung (apartment).
     * If the Wohnung is a header, returns 0.
     *
     * @return the total square meters of the Wohnung, or 0 if it is a header
     */
    public int getGesamtQuadratmeter() {
        return isHeader ? 0 : gesamtQuadratmeter;
    }

    /**
     * Sets the total square meters of the Wohnung (apartment).
     *
     * @param gesamtQuadratmeter the total square meters to set
     */
    public void setGesamtQuadratmeter(int gesamtQuadratmeter) {
        this.gesamtQuadratmeter = gesamtQuadratmeter;
    }

    /**
     * Returns the year of construction of the Wohnung (apartment).
     * If the Wohnung is a header, returns 0.
     *
     * @return the year of construction of the Wohnung, or 0 if it is a header
     */
    public int getBaujahr() {
        return isHeader ? 0 : baujahr;
    }

    /**
     * Sets the year of construction of the Wohnung (apartment).
     *
     * @param baujahr the year of construction to set
     * @throws IllegalArgumentException if the year is not a four-digit year or is in the future
     */
    public void setBaujahr(int baujahr) {
        if (baujahr < 1000 || baujahr > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Baujahr must be a four-digit year and not in the future");
        }
        this.baujahr = baujahr;
    }

    /**
     * Returns the number of bathrooms in the Wohnung (apartment).
     * If the Wohnung is a header, returns 0.
     *
     * @return the number of bathrooms, or 0 if it is a header
     */
    public int getAnzahlBaeder() {
        return isHeader ? 0 : anzahlBaeder;
    }

    /**
     * Sets the number of bathrooms in the Wohnung (apartment).
     *
     * @param anzahlBaeder the number of bathrooms to set
     * @throws IllegalArgumentException if the number of bathrooms is not positive
     */
    public void setAnzahlBaeder(int anzahlBaeder) {
        if (anzahlBaeder <= 0) {
            throw new IllegalArgumentException("AnzahlBaeder must be positive");
        }
        this.anzahlBaeder = anzahlBaeder;
    }

    /**
     * Returns the number of bedrooms in the Wohnung (apartment).
     * If the Wohnung is a header, returns 0.
     *
     * @return the number of bedrooms, or 0 if it is a header
     */
    public int getAnzahlSchlafzimmer() {
        return isHeader ? 0 : anzahlSchlafzimmer;
    }

    /**
     * Sets the number of bedrooms in the Wohnung (apartment).
     *
     * @param anzahlSchlafzimmer the number of bedrooms to set
     * @throws IllegalArgumentException if the number of bedrooms is negative
     */
    public void setAnzahlSchlafzimmer(int anzahlSchlafzimmer) {
        if (anzahlSchlafzimmer < 0) {
            throw new IllegalArgumentException("AnzahlSchlafzimmer must be zero or positive");
        }
        this.anzahlSchlafzimmer = anzahlSchlafzimmer;
    }

    /**
     * Returns the list of Mietvertrag (rental contracts) associated with the Wohnung (apartment).
     *
     * @return the list of Mietvertraege
     */
    public List<Mietvertrag> getMietvertraege() {
        return mietvertraege;
    }

    /**
     * Sets the list of Mietvertrag (rental contracts) associated with the Wohnung (apartment).
     *
     * @param mietvertraege the list of Mietvertraege to set
     */
    public void setMietvertraege(List<Mietvertrag> mietvertraege) {
        this.mietvertraege = mietvertraege;
    }

    /**
     * Returns whether the Wohnung (apartment) has a balcony.
     * If the Wohnung is a header, returns false.
     *
     * @return true if the Wohnung has a balcony and is not a header, false otherwise
     */
    public boolean isHatBalkon() {
        return !isHeader && hatBalkon;
    }

    /**
     * Sets whether the Wohnung (apartment) has a balcony.
     *
     * @param hatBalkon true if the Wohnung has a balcony, false otherwise
     */
    public void setHatBalkon(boolean hatBalkon) {
        this.hatBalkon = hatBalkon;
    }

    /**
     * Returns whether the Wohnung (apartment) has a terrace.
     * If the Wohnung is a header, returns false.
     *
     * @return true if the Wohnung has a terrace and is not a header, false otherwise
     */
    public boolean isHatTerrasse() {
        return !isHeader && hatTerrasse;
    }

    /**
     * Sets whether the Wohnung (apartment) has a terrace.
     *
     * @param hatTerrasse true if the Wohnung has a terrace, false otherwise
     */
    public void setHatTerrasse(boolean hatTerrasse) {
        this.hatTerrasse = hatTerrasse;
    }

    /**
     * Returns whether the Wohnung (apartment) has a garden.
     * If the Wohnung is a header, returns false.
     *
     * @return true if the Wohnung has a garden and is not a header, false otherwise
     */
    public boolean isHatGarten() {
        return !isHeader && hatGarten;
    }

    /**
     * Sets whether the Wohnung (apartment) has a garden.
     *
     * @param hatGarten true if the Wohnung has a garden, false otherwise
     */
    public void setHatGarten(boolean hatGarten) {
        this.hatGarten = hatGarten;
    }

    /**
     * Returns whether the Wohnung (apartment) has air conditioning.
     * If the Wohnung is a header, returns false.
     *
     * @return true if the Wohnung has air conditioning and is not a header, false otherwise
     */
    public boolean isHatKlimaanlage() {
        return !isHeader && hatKlimaanlage;
    }

    /**
     * Sets whether the Wohnung (apartment) has air conditioning.
     *
     * @param hatKlimaanlage true if the Wohnung has air conditioning, false otherwise
     */
    public void setHatKlimaanlage(boolean hatKlimaanlage) {
        this.hatKlimaanlage = hatKlimaanlage;
    }

    /**
     * Returns the floor number of the Wohnung (apartment).
     *
     * @return the floor number of the Wohnung
     */
    public String getStockwerk() {
        return stockwerk;
    }

    /**
     * Sets the floor number of the Wohnung (apartment).
     *
     * @param stockwerk the floor number to set
     */
    public void setStockwerk(String stockwerk) {
        this.stockwerk = stockwerk;
    }

    /**
     * Returns the apartment number of the Wohnung (apartment).
     *
     * @return the apartment number of the Wohnung
     */
    public String getWohnungsnummer() {
        return wohnungsnummer;
    }

    /**
     * Sets the apartment number of the Wohnung (apartment).
     *
     * @param wohnungsnummer the apartment number to set
     */
    public void setWohnungsnummer(String wohnungsnummer) {
        this.wohnungsnummer = wohnungsnummer;
    }

    /**
     * Returns the list of sub-Wohnungen (sub-apartments) for this Wohnung (apartment).
     *
     * @return the list of sub-Wohnungen
     */
    public List<Wohnung> getSubWohnungen() {
        return subWohnungen;
    }

    /**
     * Sets the list of sub-Wohnungen (sub-apartments) for this Wohnung (apartment).
     *
     * @param subWohnungen the list of sub-Wohnungen to set
     */
    public void setSubWohnungen(List<Wohnung> subWohnungen) {
        this.subWohnungen = subWohnungen;
    }

    /**
     * Returns whether this Wohnung (apartment) is a header.
     *
     * @return true if this Wohnung is a header, false otherwise
     */
    public boolean isHeader() {
        return isHeader;
    }

    /**
     * Sets whether this Wohnung (apartment) is a header.
     *
     * @param header true if this Wohnung is a header, false otherwise
     */
    public void setHeader(boolean header) {
        isHeader = header;
    }

    /**
     * Returns the street address with the house number.
     * If the Wohnung is a header, only the street name is returned.
     *
     * @return the street address with the house number, or just the street name if it is a header
     */
    public String getStrasseMitHausnummer() {
        return isHeader ? strasse : strasse + " " + hausnummer;
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
        return String.format("%s %s %s %s %s %s %s",
                land.name(),
                postleitzahl,
                stadt,
                strasse,
                hausnummer,
                stockwerk != null ? "(" + stockwerk + "ter Stock, " : "",
                wohnungsnummer != null ? wohnungsnummer + "te Wohnung)" : "");
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
                "', strasse='" + strasse +
                "', hausnummer='" + hausnummer +
                "', postleitzahl='" + postleitzahl +
                "', stadt='" + stadt +
                "', land='" + land +
                "', gesamtQuadratmeter='" + gesamtQuadratmeter +
                "', baujahr='" + baujahr +
                "', anzahlBaeder='" + anzahlBaeder +
                "', anzahlSchlafzimmer='" + anzahlSchlafzimmer +
                "', hatBalkon='" + hatBalkon +
                "', hatTerrasse='" + hatTerrasse +
                "', hatGarten='" + hatGarten +
                "', hatKlimaanlage='" + hatKlimaanlage +
                "', stockwerk='" + stockwerk +
                "', wohnungsnummer='" + wohnungsnummer +
                "']";
    }
}