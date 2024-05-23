package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents a postal code entity with a city and country.
 * This entity is mapped to the database table 'postleitzahl'.
 */
@Entity
@Table(name = "postleitzahl")
public class Postleitzahl {

    @Id
    @Column(nullable = false)
    @NotBlank(message = "Postleitzahl cannot be blank")
    @Size(min = 4, max = 10, message = "Postleitzahl must be between 4 and 10 characters")
    private String postleitzahl; // String, da PLZ mit 0 beginnen kann

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Stadt cannot be blank")
    @Size(max = 100, message = "Stadt cannot exceed 100 characters")
    private String stadt;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Land cannot be null")
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

    /**
     * Returns the postal code.
     *
     * @return the postal code
     */
    public String getPostleitzahl() {
        return postleitzahl;
    }

    /**
     * Sets the postal code.
     *
     * @param postleitzahl the postal code to set
     * @throws IllegalArgumentException if the postleitzahl is null, empty, or its length is not between 4 and 10 characters.
     */
    public void setPostleitzahl(String postleitzahl) {
        if (postleitzahl == null || postleitzahl.isEmpty() || postleitzahl.length() < 4 || postleitzahl.length() > 10) {
            throw new IllegalArgumentException("Postleitzahl must be between 4 and 10 characters");
        }
        this.postleitzahl = postleitzahl;
    }

    /**
     * Returns the city associated with the postal code.
     *
     * @return the city
     */
    public String getStadt() {
        return stadt;
    }

    /**
     * Sets the city associated with the postal code.
     *
     * @param stadt the city to set
     * @throws IllegalArgumentException if the stadt is null, empty, or its length exceeds 100 characters.
     */
    public void setStadt(String stadt) {
        if (stadt == null || stadt.isEmpty() || stadt.length() > 100) {
            throw new IllegalArgumentException("Stadt cannot be null, empty, or exceed 100 characters");
        }
        this.stadt = stadt;
    }

    /**
     * Returns the country associated with the postal code.
     *
     * @return the country
     */
    public Land getLand() {
        return land;
    }

    /**
     * Sets the country associated with the postal code.
     *
     * @param land the country to set
     * @throws IllegalArgumentException if the land is null.
     */
    public void setLand(Land land) {
        if (land == null) {
            throw new IllegalArgumentException("Land cannot be null");
        }
        this.land = land;
    }

    /**
     * Returns a string representation of this Postleitzahl.
     *
     * @return a string representation of this Postleitzahl.
     */
    @Override
    public String toString() {
        return "Postleitzahl[" +
                "postleitzahl='" + postleitzahl +
                "', stadt='" + stadt +
                "', land='" + land +
                "']";
    }
}