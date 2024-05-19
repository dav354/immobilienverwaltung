package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;

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
    private Postleitzahl postleitzahl;

    @Column(nullable = false, length = 100)
    private String strasse;

    @Column(nullable = false, length = 6)
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

    public Long getAdresse_id() {
        return adresse_id;
    }

    public Postleitzahl getPostleitzahlObj() {
        return postleitzahl;
    }

    public void setPostleitzahlObj(Postleitzahl postleitzahl) {
        this.postleitzahl = postleitzahl;
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