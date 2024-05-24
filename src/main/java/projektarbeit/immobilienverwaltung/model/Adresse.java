package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Represents an address entity with a street, house number, and postal code.
 * This entity is mapped to the database table 'adresse'.
 */
@Entity
@Table(name = "adresse")
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adresse_id;

    @ManyToOne
    @JoinColumn(name = "postleitzahl", referencedColumnName = "postleitzahl", nullable = false)
    @NotNull(message = "Postleitzahl cannot be null")
    private Postleitzahl postleitzahl;

    @Column(nullable = false, length = 100)
    @Size(max = 100, message = "Illegal Strasse")
    @NotEmpty(message = "Illegal Strasse")
    @Pattern(regexp = "^[\\p{L}\\s]+$", message = "Illegal Strasse")
    private String strasse;

    @Column(nullable = false, length = 6)
    @Size(max = 6, message = "Illegal Hausnummer")
    @NotEmpty(message = "Illegal Hausnummer")
    @Pattern(regexp = "^\\d+[a-zA-Z]?$", message = "Illegal Hausnummer")
    private String hausnummer;  // String da, z.B. 4b

    /**
     * Constructs a new Adresse instance with specified details.
     *
     * @param postleitzahl The postal code associated with this address.
     * @param strasse      The street name of this address.
     * @param hausnummer   The house number, which may include letters.
     */
    public Adresse(Postleitzahl postleitzahl,
                   String strasse,
                   String hausnummer) {
        this.postleitzahl = postleitzahl;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
    }


    /**
     * Default constructor for JPA.
     */
    public Adresse() {
    }

    /**
     * Gets the ID of this address.
     *
     * @return the address ID.
     */
    public Long getAdresse_id() {
        return adresse_id;
    }

    /**
     * Sets the ID of this address.
     *
     * @param adresse_id the new adresse_Id
     */
    public void setAdresse_id(Long adresse_id) {
        this.adresse_id = adresse_id;
    }

    /**
     * Gets the postal code associated with this address.
     *
     * @return the postal code.
     */
    public Postleitzahl getPostleitzahlObj() {
        return postleitzahl;
    }

    /**
     * Sets the postal code for this address.
     *
     * @param postleitzahl the new postal code.
     * @throws IllegalArgumentException Postleitzahl cannot be null
     */
    public void setPostleitzahlObj(Postleitzahl postleitzahl) {
        if (postleitzahl == null) throw new IllegalArgumentException("Postleitzahl cannot be null");
        this.postleitzahl = postleitzahl;
    }

    /**
     * Gets the street name of this address.
     *
     * @return the street name.
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Sets the street name for this address.
     *
     * @param strasse the new street name.
     * @throws IllegalArgumentException if the street name is null, empty or longer than 100 characters, or contains invalid characters.
     */
    public void setStrasse(String strasse) {
        if (strasse == null || strasse.isEmpty()) throw new IllegalArgumentException("Strasse cannot be null");
        if (strasse.length() > 100)
            throw new IllegalArgumentException("Strasse must be less than or equal to 100 characters");
        if (!strasse.matches("^[\\p{L}\\s]+$"))
            throw new IllegalArgumentException("Strasse must contain only letters");
        this.strasse = strasse;
    }

    /**
     * Gets the house number of this address.
     *
     * @return the house number.
     */
    public String getHausnummer() {
        return hausnummer;
    }

    /**
     * Sets the house number for this address.
     *
     * @param hausnummer the new house number.
     * @throws IllegalArgumentException if the number is null, empty or longer than 6 characters, or does not match the required pattern of digits optionally followed by one letter.
     */
    public void setHausnummer(String hausnummer) {
        if (hausnummer == null || hausnummer.isEmpty() ) throw new IllegalArgumentException("Hausnummer cannot be null");
        if (hausnummer.length() > 6)
            throw new IllegalArgumentException("Hausnummer must be less than or equal to 6 characters");
        if (!hausnummer.matches("^\\d+[a-zA-Z]?$"))
            throw new IllegalArgumentException("Hausnummer must be numeric with an optional letter at the end");
        this.hausnummer = hausnummer;
    }

    /**
     * Returns a string representation of this address.
     *
     * @return a string representation of this address.
     */
    @Override
    public String toString() {
        return String.format("Adresse['%s', " +
                        "Hausnummer='%s'," +
                        "Strasse='%s']",
                getPostleitzahlObj(),
                getHausnummer(),
                getStrasse());
    }
}