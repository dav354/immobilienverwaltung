package projektarbeit.immobilienverwaltung.model;

import javax.persistence.*;

@Entity
@Table(name = "eigentum")
public class Eigentum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String adresse;

    @Column(nullable = false)
    private String eigentumstyp;

    @Column(nullable = false)
    private int anzahlEinheiten;

    @Column(nullable = false)
    private int gesamtQuadratmeter;

    @Column(nullable = false)
    private int etagen;

    @Column(nullable = false)
    private int baujahr;

    @Column(nullable = false)
    private int anzahlBaeder;

    @Column(nullable = false)
    private int anzahlSchlafzimmer;

    @Column(nullable = false)
    private boolean balkon;

    @Column(nullable = false)
    private boolean terrasse;

    @Column(nullable = false)
    private boolean garten;

    @Column(nullable = false)
    private boolean klimaanlage;

    // Konstruktoren
    public Eigentum() {}

    public Eigentum(String adresse, 
                    String eigentumstyp, 
                    int anzahlEinheiten, 
                    int gesamtQuadratmeter, 
                    int etagen,
                    int baujahr,
                    int anzahlBaeder, 
                    int anzahlSchlafzimmer, 
                    boolean balkon, 
                    boolean terrasse,
                    boolean garten, 
                    boolean klimaanlage) {
        this.adresse = adresse;
        this.eigentumstyp = eigentumstyp;
        this.anzahlEinheiten = anzahlEinheiten;
        this.gesamtQuadratmeter = gesamtQuadratmeter;
        this.etagen = etagen;
        this.baujahr = baujahr;
        this.anzahlBaeder = anzahlBaeder;
        this.anzahlSchlafzimmer = anzahlSchlafzimmer;
        this.balkon = balkon;
        this.terrasse = terrasse;
        this.garten = garten;
        this.klimaanlage = klimaanlage;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEigentumstyp() {
        return eigentumstyp;
    }

    public void setEigentumstyp(String eigentumstyp) {
        this.eigentumstyp = eigentumstyp;
    }

    public int getAnzahlEinheiten() {
        return anzahlEinheiten;
    }

    public void setAnzahlEinheiten(int anzahlEinheiten) {
        this.anzahlEinheiten = anzahlEinheiten;
    }

    public int getGesamtQuadratmeter() {
        return gesamtQuadratmeter;
    }

    public void setGesamtQuadratmeter(int gesamtQuadratmeter) {
        this.gesamtQuadratmeter = gesamtQuadratmeter;
    }

    public int getEtagen() {
        return etagen;
    }

    public void setEtagen(int etagen) {
        this.etagen = etagen;
    }

    public int getBaujahr() {
        return baujahr;
    }

    public void setBaujahr(int baujahr) {
        this.baujahr = baujahr;
    }

    public int getAnzahlBaeder() {
        return anzahlBaeder;
    }

    public void setAnzahlBaeder(int anzahlBaeder) {
        this.anzahlBaeder = anzahlBaeder;
    }

    public int getAnzahlSchlafzimmer() {
        return anzahlSchlafzimmer;
    }

    public void setAnzahlSchlafzimmer(int anzahlSchlafzimmer) {
        this.anzahlSchlafzimmer = anzahlSchlafzimmer;
    }

    public boolean isBalkon() {
        return balkon;
    }

    public void setBalkon(boolean balkon) {
        this.balkon = balkon;
    }

    public boolean isTerrasse() {
        return terrasse;
    }

    public void setTerrasse(boolean terrasse) {
        this.terrasse = terrasse;
    }

    public boolean isGarten() {
        return garten;
    }

    public void setGarten(boolean garten) {
        this.garten = garten;
    }

    public boolean isKlimaanlage() {
        return klimaanlage;
    }

    public void setKlimaanlage(boolean klimaanlage) {
        this.klimaanlage = klimaanlage;
    }
}