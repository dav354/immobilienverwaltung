package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @Size(max = 100, message = "Strasse cannot exceed 100 characters")
    private String strasse;

    @Column(nullable = false, length = 6)
    @Size(max = 6, message = "Hausnummer cannot exceed 6 characters")
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
     * @throws
     */
    public void setPostleitzahlObj(Postleitzahl postleitzahl) {
        if (postleitzahl == null) {
            throw new IllegalArgumentException("Postleitzahl cannot be null");
        }
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
     */
    public void setStrasse(String strasse) {
        if (strasse == null || strasse.length() > 100) {
            throw new IllegalArgumentException("Strasse cannot be null and must be less than or equal to 100 characters");
        }
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
     */
    public void setHausnummer(String hausnummer) {
        if (hausnummer == null || hausnummer.length() > 6) {
            throw new IllegalArgumentException("Hausnummer cannot be null and must be less than or equal to 6 characters");
        }
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