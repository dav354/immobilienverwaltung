package projektarbeit.immobilienverwaltung.model;

import javax.persistence.*;

@Entity
@Table(name = "postleitzahl")
public class Postleitzahl {

    @Id
    @Column(nullable = false)
    private int postleitzahl;

    @Column(nullable = false, length = 100)
    private String stadt;

    @Column(nullable = false, length = 50)
    private String land;

    /**
     * Constructs a new Stadt with specified postal code, city name, and country.
     *
     * @param postleitzahl The postal code of the city.
     * @param stadt The name of the city.
     * @param land The country in which the city is located.
     */

    public Postleitzahl(int postleitzahl, String stadt, String land) {
        this.postleitzahl = postleitzahl;
        this.stadt = stadt;
        this.land = land;
    }

    public Postleitzahl() {}

    public int getPostleitzahl() {
        return postleitzahl;
    }

    public void setPostleitzahl(int postleitzahl) {
        this.postleitzahl = postleitzahl;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    @Override
    public String toString() {
        return "Postleitzahl[" +
                "postleitzahl=" + postleitzahl +
                ", stadt=\"" + stadt + "/" +
                ", land=\"" + land + "/" +
                "]";
    }
}