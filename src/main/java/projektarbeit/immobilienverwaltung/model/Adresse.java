package projektarbeit.immobilienverwaltung.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import javax.persistence.*;

@Entity
@Table(name = "adresse", indexes = {
        @Index(name = "idx_adresse_postleitzahl", columnList = "postleitzahl")
})

public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adresseID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postleitzahl", nullable = false)
    private Postleitzahl postleitzahl;

    @Column(nullable = false, length = 100)
    private String strasse;

    @Column(nullable = false, length = 6)
    private String hausnummer;  // String da, z.B. 4b

    /**
     * Constructs a new Adresse instance with specified details.
     *
     * @param postleitzahl The postal code of the address.
     * @param strasse The street of the address.
     * @param hausnummer The house number, which may include letters.
     */

    public Adresse(Postleitzahl postleitzahl,
                   String strasse,
                   String hausnummer) {
        this.postleitzahl = postleitzahl;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
    }

    public Adresse(){}

    public Postleitzahl getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(Postleitzahl postleitzahl) {
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
        return String.format("Adresse[%s, " +
                        "Hausnummer='%s'" +
                        "Strasse='%s'" +
                        "]",
                        getPostleitzahl(),
                        getHausnummer(),
                        getStrasse());
    }
}