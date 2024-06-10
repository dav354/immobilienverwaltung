package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import projektarbeit.immobilienverwaltung.validation.UniqueEmail;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tenant entity with details about the tenant and their rental agreement.
 * This entity is mapped to the database table 'mieter'.
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
@Entity
public class Mieter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mieterId;

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

    @OneToMany(mappedBy = "mieter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Mietvertrag> mietvertraege = new ArrayList<>();

    @Column(nullable = false)
    @Email(message = "Email should be valid")
    @NotNull
    //@UniqueEmail TODO: kp warum das mit derValidation nicht geht, bekomme immer eine Nullpointer Exception
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Illegal Mail")
    private String email;

    @Column(nullable = false)
    @Positive(message = "Einkommen must be positive")
    private double einkommen;

    /**
     * Constructs a new Mieter with the specified details.
     *
     * @param name           Last name of the tenant.
     * @param vorname        First name of the tenant.
     * @param telefonnummer  Contact number of the tenant.
     * @param email Mail of the tenant
     * @param einkommen      Monthly income of the tenant.
     */
    public Mieter(String name,
                  String vorname,
                  String telefonnummer,
                  String email,
                  double einkommen
    ) {
        this.name = name;
        this.vorname = vorname;
        this.telefonnummer = telefonnummer;
        this.email = email;
        this.einkommen = einkommen;
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
        return mieterId;
    }

    /**
     * Set the ID of the tenant.
     *
     * @param mieter_id the tenant's ID
     */
    public void setMieter_id(Long mieter_id) {
        this.mieterId = mieter_id;
    }


    /**
     * Returns the list of documents associated with the tenant.
     *
     * @return the list of documents
     */
    public List<Dokument> getDokument() {
        return dokument;
    }

    /**
     * Sets the list of documents for the tenant.
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
     * Gets the email address of the Mieter.
     *
     * @return the email address of the Mieter.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the Mieter.
     *
     * @param email the email address to set.
     * @throws IllegalArgumentException if the mail format is invalid
     */
    public void setEmail(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) throw new IllegalArgumentException("Invalid email format");
        this.email = email;
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

    public List<Mietvertrag> getMietvertraege() {
        return mietvertraege;
    }

    public void setMietvertraege(List<Mietvertrag> mietvertraege) {
        this.mietvertraege = mietvertraege;
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
     * Returns a string representation of this Mieter.
     *
     * @return a string representation of this Mieter.
     */
    @Override
    public String toString() {
        return "Mieter[" +
                "mieter_id='" + mieterId +
                "', name='" + name +
                "', vorname='" + vorname +
                "', telefonnummer='" + telefonnummer +
                "', email='" + email +
                "', einkommen='" + einkommen +
                "']";
    }
}