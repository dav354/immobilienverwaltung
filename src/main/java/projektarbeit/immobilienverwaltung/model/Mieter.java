package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a tenant entity with details about the tenant and their rental agreement.
 * This entity is mapped to the database table 'mieter'.
 */
@Entity
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
    private String name;

    @Column(nullable = false, length = 100)
    private String vorname;

    @Column(nullable = false)
    @Min(value = 6)
    private String telefonnummer; // can start with 0 or +

    @Column(nullable = false)
    @Min(value = 0, message = "Einkommen cannot be negative")
    private double einkommen;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate mietbeginn;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate mietende;

    @Column(nullable = false)
    @Min(value = 0, message = "Kaution cannot be negative")
    private double kaution;

    @Column(nullable = false)
    @Min(value = 1, message = "Must have at least one inhabitant")
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
    public Mieter() {}

    public Long getMieter_id() {
        return mieter_id;
    }

    public List<Wohnung> getWohnung() {
        return wohnung;
    }

    public void setWohnung(List<Wohnung> wohnung) {
        this.wohnung = wohnung;
    }

    public List<Dokument> getDokument() {
        return dokument;
    }

    public void setDokument(List<Dokument> dokument) {
        this.dokument = dokument;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public double getEinkommen() {
        return einkommen;
    }

    public void setEinkommen(double einkommen) {
        if (einkommen < 0) throw new IllegalArgumentException("Einkommen must be positive.");
        this.einkommen = einkommen;
    }

    public LocalDate getMietbeginn() {
        return mietbeginn;
    }

    public void setMietbeginn(LocalDate mietbeginn) {
        if (mietende != null && mietbeginn.isAfter(mietende)) {
            throw new IllegalArgumentException("Mietbeginn must be before Mietende.");
        }
        this.mietbeginn = mietbeginn;
    }
    public LocalDate getMietende() {
        return mietende;
    }

    public void setMietende(LocalDate mietende) {
        if (mietbeginn != null && mietende.isBefore(mietbeginn)) {
            throw new IllegalArgumentException("Mietende must be after Mietbeginn.");
        }
        this.mietende = mietende;
    }

    public double getKaution() {
        return kaution;
    }

    public void setKaution(double kaution) {
        this.kaution = kaution;
    }

    public int getAnzahlBewohner() {
        return anzahlBewohner;
    }

    public void setAnzahlBewohner(int anzahlBewohner) {
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
     * Returns a string representation of the Mieter.
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