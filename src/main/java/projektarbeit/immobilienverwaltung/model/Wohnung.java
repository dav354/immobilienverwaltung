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
                   boolean hatKlimaanlage) {
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
    }

    // Getter und Setter für alle Felder

    public Long getWohnung_id() {
        return wohnung_id;
    }

    public void setWohnung_id(Long wohnung_id) {
        this.wohnung_id = wohnung_id;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
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

    public Mieter getMieter() {
        return mieter;
    }

    public void setMieter(Mieter mieter) {
        this.mieter = mieter;
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
        if (baujahr < 1000 || baujahr > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Baujahr must be a four-digit year and not in the future");
        }
        this.baujahr = baujahr;
    }

    public int getAnzahlBaeder() {
        return anzahlBaeder;
    }

    public void setAnzahlBaeder(int anzahlBaeder) {
        if (anzahlBaeder <= 0) {
            throw new IllegalArgumentException("AnzahlBaeder must be positive");
        }
        this.anzahlBaeder = anzahlBaeder;
    }

    public int getAnzahlSchlafzimmer() {
        return anzahlSchlafzimmer;
    }

    public void setAnzahlSchlafzimmer(int anzahlSchlafzimmer) {
        if (anzahlSchlafzimmer < 0) {
            throw new IllegalArgumentException("AnzahlSchlafzimmer must be zero or positive");
        }
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
        return String.format("%s %s %s %s %s",
                land.name(),
                postleitzahl,
                stadt,
                strasse,
                hausnummer);
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
                "', mieter='" + (mieter != null ? mieter.getFullName() : "keinen") +
                "']";
    }
}