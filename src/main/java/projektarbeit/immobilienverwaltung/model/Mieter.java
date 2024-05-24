package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import projektarbeit.immobilienverwaltung.validation.ValidMietPeriod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a tenant entity with details about the tenant and their rental agreement.
 * This entity is mapped to the database table 'mieter'.
 */
@Entity
@ValidMietPeriod
@Table(name = "mieter")
public class Mieter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mieter_id;

    @OneToMany(mappedBy = "mieter", fetch = FetchType.EAGER)
    private List<Wohnung> wohnung = new ArrayList<>();

    @OneToMany(mappedBy = "mieter", fetch = FetchType.EAGER)
    private List<Dokument> dokument = new ArrayList<>();

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Illegal Name")
    @NotNull(message = "Illegal Name")
    @Size(max = 100, message = "Illegal Name")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Illegal Name")
    private String name;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Illegal Vorname")
    @NotNull(message = "Illegal Vorname")
    @Size(max = 100, message = "Illegal Vorname")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Illegal Vorname")
    private String vorname;

    @Column(nullable = false)
    @NotBlank(message = "Illegal Telefonnummer")
    @NotNull(message = "Illegal Telefonnummer")
    @Pattern(regexp = "^\\d{6,12}$", message = "Illegal Telefonnummer")
    private String telefonnummer; // can start with 0

    @Column(nullable = false)
    @Positive(message = "Einkommen must be positive")
    private double einkommen;

    @Column(nullable = true)
    private LocalDate mietbeginn;

    @Column(nullable = true)
    private LocalDate mietende;

    @Column(nullable = false)
    @Positive(message = "Kaution must be positive")
    private double kaution;

    @Column(nullable = false)
    @Min(value = 1, message = "AnzahlBewohner must be at least 1")
    private int anzahlBewohner;

    /**
     * Constructs a new Mieter (tenant) with the specified details.
     *
     * @param name           Last name of the tenant.
     * @param vorname        First name of the tenant.
     * @param telefonnummer  Contact number of the tenant.
     * @param einkommen      Monthly income of the tenant.
     * @param mietbeginn     The start date of the tenancy.
     * @param mietende       The end date of the tenancy.
     * @param kaution        Deposit amount paid by the tenant.
     * @param anzahlBewohner Number of inhabitants living in the rented property.
     */
    public Mieter(String name,
                  String vorname,
                  String telefonnummer,
                  double einkommen,
                  LocalDate mietbeginn,
                  LocalDate mietende,
                  double kaution,
                  int anzahlBewohner) {
        this.name = name;
        this.vorname = vorname;
        this.telefonnummer = telefonnummer;
        this.einkommen = einkommen;
        this.mietbeginn = mietbeginn;
        this.mietende = mietende;
        this.kaution = kaution;
        this.anzahlBewohner = anzahlBewohner;
    }

    /**
     * Default constructor for JPA.
     */
    public Mieter() {
    }

    /**
     * Returns the ID of the tenant.
     *
     * @return the tenant's ID
     */
    public Long getMieter_id() {
        return mieter_id;
    }

    /**
     * Set the ID of the tenant.
     *
     * @param mieter_id the tenant's ID
     */
    public void setMieter_id(Long mieter_id) {
        this.mieter_id = mieter_id;
    }

    /**
     * Returns the list of properties (Wohnungen) associated with the tenant.
     *
     * @return the list of properties
     */
    public List<Wohnung> getWohnung() {
        return wohnung;
    }

    /**
     * Sets the list of properties (Wohnungen) for the tenant.
     *
     * @param wohnung the list of properties to set
     */
    public void setWohnung(List<Wohnung> wohnung) {
        this.wohnung = wohnung;
    }

    /**
     * Returns the list of documents (Dokumente) associated with the tenant.
     *
     * @return the list of documents
     */
    public List<Dokument> getDokument() {
        return dokument;
    }

    /**
     * Sets the list of documents (Dokumente) for the tenant.
     *
     * @param dokument the list of documents to set
     */
    public void setDokument(List<Dokument> dokument) {
        this.dokument = dokument;
    }

    /**
     * Returns the last name of the tenant.
     *
     * @return the tenant's last name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the last name of the tenant.
     *
     * @param name the last name to set
     * @throws IllegalArgumentException if the name is null, empty, or exceeds 100 characters
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be null or empty");
        if (name.length() > 100) throw new IllegalArgumentException("Name cannot exceed 100 characters");
        if (!name.matches("^[a-zA-Z\\s]+$")) throw new IllegalArgumentException("Name must contain only letters");
        this.name = name;
    }

    /**
     * Returns the first name of the tenant.
     *
     * @return the tenant's first name
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * Sets the first name of the tenant.
     *
     * @param vorname the first name to set
     * @throws IllegalArgumentException if the first name is null, empty, or exceeds 100 characters
     */
    public void setVorname(String vorname) {
        if (vorname == null || vorname.isEmpty()) throw new IllegalArgumentException("Vorname cannot be null or empty");
        if (vorname.length() > 100) throw new IllegalArgumentException("Vorname cannot exceed 100 characters");
        if (!vorname.matches("^[a-zA-Z\\s]+$")) throw new IllegalArgumentException("Vorname must contain only letters");
        this.vorname = vorname;
    }

    /**
     * Returns the phone number of the tenant.
     *
     * @return the tenant's phone number
     */
    public String getTelefonnummer() {
        return telefonnummer;
    }

    /**
     * Sets the phone number of the tenant.
     *
     * @param telefonnummer the phone number to set
     * @throws IllegalArgumentException if the phone number format is invalid
     */
    public void setTelefonnummer(String telefonnummer) {
        if (telefonnummer == null || !telefonnummer.matches("^\\d{6,12}$")) throw new IllegalArgumentException("Invalid Telefonnummer format");
        this.telefonnummer = telefonnummer;
    }

    /**
     * Returns the income of the tenant.
     *
     * @return the tenant's income
     */
    public double getEinkommen() {
        return einkommen;
    }

    /**
     * Sets the income of the tenant.
     * The income must be a positive value.
     *
     * @param einkommen the income to set
     * @throws IllegalArgumentException if the income is negative
     */
    public void setEinkommen(double einkommen) {
        if (einkommen <= 0) throw new IllegalArgumentException("Einkommen must be positive");
        this.einkommen = einkommen;
    }

    /**
     * Returns the start date of the tenancy.
     *
     * @return the start date of the tenancy
     */
    public LocalDate getMietbeginn() {
        return mietbeginn;
    }

    /**
     * Sets the start date of the tenancy.
     * The start date must be before the end date, if it is set.
     *
     * @param mietbeginn the start date to set
     * @throws IllegalArgumentException if the start date is after the end date
     */
    public void setMietbeginn(LocalDate mietbeginn) {
        if (mietende != null && mietbeginn != null && (mietbeginn.isAfter(mietende) || mietbeginn.isEqual(mietende))) throw new IllegalArgumentException("Mietbeginn must be before Mietende");
        this.mietbeginn = mietbeginn;
    }

    /**
     * Returns the end date of the tenancy.
     *
     * @return the end date of the tenancy
     */
    public LocalDate getMietende() {
        return mietende;
    }

    /**
     * Sets the end date of the tenancy.
     * The end date must be after the start date.
     *
     * @param mietende the end date to set
     * @throws IllegalArgumentException if the end date is before the start date
     */
    public void setMietende(LocalDate mietende) {
        if (mietbeginn != null && mietende != null && (mietende.isBefore(mietbeginn) || mietende.isEqual(mietbeginn))) throw new IllegalArgumentException("Mietbeginn must be before Mietende");
        this.mietende = mietende;
    }

    /**
     * Returns the security deposit amount.
     *
     * @return the security deposit amount
     */
    public double getKaution() {
        return kaution;
    }

    /**
     * Sets the security deposit amount.
     *
     * @param kaution the security deposit amount to set
     * @throws IllegalArgumentException if the security deposit is not positive
     */
    public void setKaution(double kaution) {
        if (kaution <= 0) throw new IllegalArgumentException("Kaution must be positive");
        this.kaution = kaution;
    }

    /**
     * Returns the number of residents.
     *
     * @return the number of residents
     */
    public int getAnzahlBewohner() {
        return anzahlBewohner;
    }

    /**
     * Sets the number of residents.
     *
     * @param anzahlBewohner the number of residents to set
     * @throws IllegalArgumentException if the number of residents is less than 1
     */
    public void setAnzahlBewohner(int anzahlBewohner) {
        if (anzahlBewohner < 1) throw new IllegalArgumentException("AnzahlBewohner must be at least 1");
        this.anzahlBewohner = anzahlBewohner;
    }

    /**
     * Returns the full name of the tenant by combining the first name and last name.
     *
     * @return The full name of the tenant in the format "FirstName LastName".
     */
    public String getFullName() {
        return vorname + " " + name;
    }

    /**
     * Returns a formatted string of all the apartments associated with the tenant.
     * If no apartments are associated, it returns "Keine Wohnung".
     * Each apartment's address is formatted and separated by a line break.
     *
     * @return A formatted string of the tenant's apartments or "Keine Wohnung" if none are associated.
     */
    public String getFormattedWohnung() {
        if (wohnung.isEmpty()) {
            return "Keine Wohnung";
        } else {
            return wohnung.stream()
                    .map(Wohnung::getFormattedAddress)
                    .collect(Collectors.joining("<br>"));
        }
    }

    /**
     * Returns a string representation of this Mieter.
     *
     * @return a string representation of this Mieter.
     */
    @Override
    public String toString() {
        return "Mieter[" +
                "mieter_id='" + mieter_id +
                "', name='" + name +
                "', vorname='" + vorname +
                "', telefonnummer='" + telefonnummer +
                "', einkommen='" + einkommen +
                "', mietbeginn='" + mietbeginn +
                "', wohnungen='" + getFormattedWohnung() +
                "']";
    }
}