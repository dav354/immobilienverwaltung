package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;

/**
 * Represents a postal code entity with a city and country.
 * This entity is mapped to the database table 'postleitzahl'.
 */
@Entity
@Table(name = "postleitzahl")
public class Postleitzahl {

    @Id
    @Column(nullable = false)
    private String postleitzahl; // String, da PLZ mit 0 beginnen kann

    @Column(nullable = false, length = 100)
    private String stadt;

    @Enumerated(EnumType.STRING)
    private Land land;

    /**
     * Default constructor for JPA.
     */
    public Postleitzahl() {}

    /**
     * Constructs a new Postleitzahl instance with specified details.
     *
     * @param postleitzahl The postal code.
     * @param stadt        The city associated with the postal code.
     * @param land         The country associated with the postal code.
     */
    public Postleitzahl(String postleitzahl,
                        String stadt,
                        Land land) {
        this.postleitzahl = postleitzahl;
        this.stadt = stadt;
        this.land = land;
    }

    // Getter und Setter
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

    @Override
    public String toString() {
        return "Postleitzahl[" +
                "postleitzahl='" + postleitzahl +
                "', stadt='" + stadt +
                "', land='" + land +
                "']";
    }
}