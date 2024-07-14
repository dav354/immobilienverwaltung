package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import projektarbeit.immobilienverwaltung.validation.ValidYear;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert eine Immobilien-Entität (Wohnung) mit Details zur Immobilie wie Adresse, Größe, Anzahl der Zimmer und Ausstattung.
 * Diese Entität wird der Datenbanktabelle 'wohnung' zugeordnet.
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
@Entity
@Table(name = "wohnung", indexes = {
        @Index(name = "idx_wohnung_strasse", columnList = "strasse"),
        @Index(name = "idx_wohnung_postleitzahl", columnList = "postleitzahl"),
        @Index(name = "idx_wohnung_stadt", columnList = "stadt")
})
public class Wohnung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wohnung_id;

    @Column
    @NotBlank(message = "Illegal Strasse")
    @Size(max = 100, message = "Illegal Strasse")
    @Pattern(regexp = "^[\\p{L}äöüÄÖÜß\\s\\-]+$", message = "Illegal Strasse")
    private String strasse;

    @Column
    @NotBlank(message = "Illegal Hausnummer")
    @Size(max = 6, message = "Illegal Hausnummer")
    @Pattern(regexp = "^\\d+[a-zA-Z]?$", message = "Illegal Hausnummer")
    private String hausnummer;

    @Column
    @NotNull(message = "Illegal Postleitzahl")
    @Pattern(regexp = "^\\d{4,10}$", message = "Illegal Postleitzahl")
    private String postleitzahl;

    @Column
    @NotBlank(message = "Illegal Stadt")
    @Size(max = 100, message = "Illegal Stadt")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Illegal Stadt")
    private String stadt;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Land darf nicht null sein")
    private Land land;

    @OneToMany(mappedBy = "wohnung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dokument> dokumente = new ArrayList<>();

    @OneToMany(mappedBy = "wohnung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zaehlerstand> zaehlerstand = new ArrayList<>();

    @OneToOne(mappedBy = "wohnung", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Mietvertrag mietvertrag;

    @Column
    @Min(value = 1, message = "GesamtQuadratmeter muss mindestens 1 sein")
    private int gesamtQuadratmeter;

    @Positive(message = "Baujahr muss eine vierstellige Zahl sein und darf nicht in der Zukunft liegen")
    @ValidYear(message = "Baujahr muss eine vierstellige Zahl sein und darf nicht in der Zukunft liegen")
    private int baujahr;

    @Positive(message = "AnzahlBäder muss positiv sein")
    private int anzahlBaeder;

    @PositiveOrZero(message = "AnzahlSchlafzimmer muss null oder positiv sein")
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

    private Double latitude;

    private Double longitude;

    /**
     * Standardkonstruktor für JPA.
     */
    public Wohnung() {
    }

    /**
     * Erstellt eine neue Wohnung-Instanz mit den angegebenen Details.
     *
     * @param strasse            Der Straßenname der Immobilie.
     * @param hausnummer         Die Hausnummer der Immobilie.
     * @param postleitzahl       Die Postleitzahl der Immobilie.
     * @param stadt              Die Stadt der Immobilie.
     * @param land               Das Land der Immobilie.
     * @param gesamtQuadratmeter Die Gesamtfläche der Immobilie in Quadratmetern.
     * @param baujahr            Das Baujahr der Immobilie.
     * @param anzahlBaeder       Die Anzahl der Badezimmer in der Immobilie.
     * @param anzahlSchlafzimmer Die Anzahl der Schlafzimmer in der Immobilie.
     * @param hatBalkon          Ob die Immobilie einen Balkon hat.
     * @param hatTerrasse        Ob die Immobilie eine Terrasse hat.
     * @param hatGarten          Ob die Immobilie einen Garten hat.
     * @param hatKlimaanlage     Ob die Immobilie eine Klimaanlage hat.
     * @param stockwerk          Das Stockwerk, in dem sich die Immobilie befindet.
     * @param wohnungsnummer     Die Wohnungsnummer der Immobilie.
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
     * Gibt die ID der Wohnung zurück.
     *
     * @return die Wohnungs-ID
     */
    public Long getWohnung_id() {
        return wohnung_id;
    }

    /**
     * Setzt die ID der Wohnung.
     *
     * @param wohnung_id die Wohnungs-ID
     */
    public void setWohnung_id(Long wohnung_id) {
        this.wohnung_id = wohnung_id;
    }

    /**
     * Gibt die Straße der Wohnung zurück.
     *
     * @return die Straße der Wohnung
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Setzt die Straße der Wohnung.
     *
     * @param strasse die Straße der Wohnung
     */
    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    /**
     * Gibt die Hausnummer der Wohnung zurück.
     *
     * @return die Hausnummer der Wohnung
     */
    public String getHausnummer() {
        return hausnummer;
    }

    /**
     * Setzt die Hausnummer der Wohnung.
     *
     * @param hausnummer die Hausnummer der Wohnung
     */
    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    /**
     * Gibt die Postleitzahl der Wohnung zurück.
     *
     * @return die Postleitzahl der Wohnung
     */
    public String getPostleitzahl() {
        return postleitzahl;
    }

    /**
     * Setzt die Postleitzahl der Wohnung.
     *
     * @param postleitzahl die Postleitzahl der Wohnung
     */
    public void setPostleitzahl(String postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    /**
     * Gibt die Stadt der Wohnung zurück.
     *
     * @return die Stadt der Wohnung
     */
    public String getStadt() {
        return stadt;
    }

    /**
     * Setzt die Stadt der Wohnung.
     *
     * @param stadt die Stadt der Wohnung
     */
    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    /**
     * Gibt das Land der Wohnung zurück.
     *
     * @return das Land der Wohnung
     */
    public Land getLand() {
        return land;
    }

    /**
     * Setzt das Land der Wohnung.
     *
     * @param land das Land der Wohnung
     */
    public void setLand(Land land) {
        this.land = land;
    }

    /**
     * Gibt die Liste der Dokumente zurück, die mit der Wohnung verknüpft sind.
     *
     * @return die Liste der Dokumente
     */
    public List<Dokument> getDokumente() {
        return dokumente;
    }

    /**
     * Setzt die Liste der Dokumente für die Wohnung.
     *
     * @param dokumente die Liste der Dokumente
     */
    public void setDokumente(List<Dokument> dokumente) {
        this.dokumente = dokumente;
    }

    /**
     * Gibt die Liste der Zählerstände zurück, die mit der Wohnung verknüpft sind.
     *
     * @return die Liste der Zählerstände
     */
    public List<Zaehlerstand> getZaehlerstand() {
        return zaehlerstand;
    }

    /**
     * Setzt die Liste der Zählerstände für die Wohnung.
     *
     * @param zaehlerstand die Liste der Zählerstände
     */
    public void setZaehlerstand(List<Zaehlerstand> zaehlerstand) {
        this.zaehlerstand = zaehlerstand;
    }

    /**
     * Gibt die Gesamtquadratmeter der Wohnung zurück.
     *
     * @return die Gesamtquadratmeter der Wohnung
     */
    public int getGesamtQuadratmeter() {
        return gesamtQuadratmeter;
    }

    /**
     * Setzt die Gesamtquadratmeter der Wohnung.
     *
     * @param gesamtQuadratmeter die Gesamtquadratmeter der Wohnung
     */
    public void setGesamtQuadratmeter(int gesamtQuadratmeter) {
        this.gesamtQuadratmeter = gesamtQuadratmeter;
    }

    /**
     * Gibt das Baujahr der Wohnung zurück.
     *
     * @return das Baujahr der Wohnung
     */
    public int getBaujahr() {
        return baujahr;
    }

    /**
     * Setzt das Baujahr der Wohnung.
     *
     * @param baujahr das Baujahr der Wohnung
     */
    public void setBaujahr(int baujahr) {
        this.baujahr = baujahr;
    }

    /**
     * Gibt die Anzahl der Bäder in der Wohnung zurück.
     *
     * @return die Anzahl der Bäder in der Wohnung
     */
    public int getAnzahlBaeder() {
        return anzahlBaeder;
    }

    /**
     * Setzt die Anzahl der Bäder in der Wohnung.
     *
     * @param anzahlBaeder die Anzahl der Bäder in der Wohnung
     */
    public void setAnzahlBaeder(int anzahlBaeder) {
        this.anzahlBaeder = anzahlBaeder;
    }

    /**
     * Gibt die Anzahl der Schlafzimmer in der Wohnung zurück.
     *
     * @return die Anzahl der Schlafzimmer in der Wohnung
     */
    public int getAnzahlSchlafzimmer() {
        return anzahlSchlafzimmer;
    }

    /**
     * Setzt die Anzahl der Schlafzimmer in der Wohnung.
     *
     * @param anzahlSchlafzimmer die Anzahl der Schlafzimmer in der Wohnung
     */
    public void setAnzahlSchlafzimmer(int anzahlSchlafzimmer) {
        this.anzahlSchlafzimmer = anzahlSchlafzimmer;
    }

    /**
     * Gibt den Mietvertrag zurück, der mit der Wohnung verknüpft ist.
     *
     * @return der Mietvertrag
     */
    public Mietvertrag getMietvertrag() {
        return mietvertrag;
    }

    /**
     * Setzt den Mietvertrag für die Wohnung.
     *
     * @param mietvertrag der Mietvertrag
     */
    public void setMietvertraege(Mietvertrag mietvertrag) {
        this.mietvertrag = mietvertrag;
    }

    /**
     * Gibt zurück, ob die Wohnung einen Balkon hat.
     *
     * @return true, wenn die Wohnung einen Balkon hat, sonst false
     */
    public boolean isHatBalkon() {
        return hatBalkon;
    }

    /**
     * Setzt, ob die Wohnung einen Balkon hat.
     *
     * @param hatBalkon true, wenn die Wohnung einen Balkon hat, sonst false
     */
    public void setHatBalkon(boolean hatBalkon) {
        this.hatBalkon = hatBalkon;
    }

    /**
     * Gibt zurück, ob die Wohnung eine Terrasse hat.
     *
     * @return true, wenn die Wohnung eine Terrasse hat, sonst false
     */
    public boolean isHatTerrasse() {
        return hatTerrasse;
    }

    /**
     * Setzt, ob die Wohnung eine Terrasse hat.
     *
     * @param hatTerrasse true, wenn die Wohnung eine Terrasse hat, sonst false
     */
    public void setHatTerrasse(boolean hatTerrasse) {
        this.hatTerrasse = hatTerrasse;
    }

    /**
     * Gibt zurück, ob die Wohnung einen Garten hat.
     *
     * @return true, wenn die Wohnung einen Garten hat, sonst false
     */
    public boolean isHatGarten() {
        return hatGarten;
    }

    /**
     * Setzt, ob die Wohnung einen Garten hat.
     *
     * @param hatGarten true, wenn die Wohnung einen Garten hat, sonst false
     */
    public void setHatGarten(boolean hatGarten) {
        this.hatGarten = hatGarten;
    }

    /**
     * Gibt zurück, ob die Wohnung eine Klimaanlage hat.
     *
     * @return true, wenn die Wohnung eine Klimaanlage hat, sonst false
     */
    public boolean isHatKlimaanlage() {
        return hatKlimaanlage;
    }

    /**
     * Setzt, ob die Wohnung eine Klimaanlage hat.
     *
     * @param hatKlimaanlage true, wenn die Wohnung eine Klimaanlage hat, sonst false
     */
    public void setHatKlimaanlage(boolean hatKlimaanlage) {
        this.hatKlimaanlage = hatKlimaanlage;
    }

    /**
     * Gibt die Etage der Wohnung zurück.
     *
     * @return die Etage der Wohnung
     */
    public String getStockwerk() {
        return stockwerk;
    }

    /**
     * Setzt die Etage der Wohnung.
     *
     * @param stockwerk die Etage der Wohnung
     */
    public void setStockwerk(String stockwerk) {
        this.stockwerk = stockwerk;
    }

    /**
     * Gibt die Wohnungsnummer der Wohnung zurück.
     *
     * @return die Wohnungsnummer der Wohnung
     */
    public String getWohnungsnummer() {
        return wohnungsnummer;
    }

    /**
     * Setzt die Wohnungsnummer der Wohnung.
     *
     * @param wohnungsnummer die Wohnungsnummer der Wohnung
     */
    public void setWohnungsnummer(String wohnungsnummer) {
        this.wohnungsnummer = wohnungsnummer;
    }

    /**
     * Gibt die Liste der Subwohnungen für diese Wohnung zurück.
     *
     * @return die Liste der Subwohnungen
     */
    public List<Wohnung> getSubWohnungen() {
        return subWohnungen;
    }

    /**
     * Setzt die Liste der Subwohnungen für diese Wohnung.
     *
     * @param subWohnungen die Liste der Subwohnungen
     */
    public void setSubWohnungen(List<Wohnung> subWohnungen) {
        this.subWohnungen = subWohnungen;
    }

    /**
     * Gibt zurück, ob diese Wohnung ein Header ist.
     *
     * @return true, wenn diese Wohnung ein Header ist, sonst false
     */
    public boolean isHeader() {
        return isHeader;
    }

    /**
     * Setzt, ob diese Wohnung ein Header ist.
     *
     * @param header true, wenn diese Wohnung ein Header ist, sonst false
     */
    public void setHeader(boolean header) {
        isHeader = header;
    }

    /**
     * Gibt die Breite der Wohnung zurück.
     *
     * @return die Breite der Wohnung
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Setzt die Breite der Wohnung.
     *
     * @param latitude die Breite der Wohnung
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gibt die Länge der Wohnung zurück.
     *
     * @return die Länge der Wohnung
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Setzt die Länge der Wohnung.
     *
     * @param longitude die Länge der Wohnung
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Überprüft, ob die Wohnung vermietet ist.
     *
     * @return true, wenn die Wohnung vermietet ist, sonst false
     */
    public boolean isVermietet() {
        return mietvertrag != null;
    }

    /**
     * Gibt die Straße mit Hausnummer der Wohnung zurück.
     *
     * @return die Straße mit Hausnummer der Wohnung
     */
    public String getStrasseMitHausnummer() {
        return strasse + " " + hausnummer;
    }

    /**
     * Gibt die formatierte Adresse der Wohnung zurück.
     * <p>
     * Das Adressformat umfasst das Land, die Postleitzahl, die Stadt, die Straße und die Hausnummer.
     * Wenn die Adresse oder die Postleitzahl null ist, wird "Keine Adresse" zurückgegeben.
     *
     * @return eine formatierte Zeichenkette, die die Adresse der Wohnung darstellt, oder "Keine Adresse", wenn die Adresse oder die Postleitzahl null ist.
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
     * Gibt einen eindeutigen Bezeichner für die Wohnung zurück.
     * Der Bezeichner basiert auf der Straße, Hausnummer, Postleitzahl und Stadt.
     * Wenn eine der Eigenschaften null ist, wird der String "null" verwendet.
     *
     * @return Ein eindeutiger Bezeichner für die Wohnung als String.
     */
    public String getUniqueIdentifier() {
        return String.format("%s_%s_%s_%s",
                this.strasse != null ? this.strasse : "null",
                this.hausnummer != null ? this.hausnummer : "null",
                this.postleitzahl != null ? this.postleitzahl : "null",
                this.stadt != null ? this.stadt : "null");
    }

    /**
     * Gibt eine String-Darstellung dieser Wohnung zurück.
     *
     * @return eine String-Darstellung dieser Wohnung
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