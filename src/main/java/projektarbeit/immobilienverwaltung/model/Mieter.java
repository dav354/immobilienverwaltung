package projektarbeit.immobilienverwaltung.model;

import javax.persistence.*;
import java.sql.Date;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "mieter")
public class Mieter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mieterID;

    @ManyToOne
    @JoinColumn(name = "wohnungId", nullable = false)
    private Wohnung wohnung;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String vorname;

    @Column(nullable = false)
    @Min(value = 6)
    private long telefonnummer;

    @Column(nullable = false)
    @Min(value = 0, message = "Einkommen cannot be negative")
    private int einkommen;

    @Column(nullable = false)
    @Min(value = 0, message = "Ausgaben cannot be negative")
    private int ausgaben;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @PastOrPresent
    private Date mietbeginn;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @FutureOrPresent
    private Date mietende;

    @Column(nullable = false)
    @Min(value = 0, message = "Kaution cannot be negative")
    private int kaution;

    @Column(nullable = false)
    @Min(value = 1, message = "Must have at least one inhabitant")
    private int anzahlBewohner;

    /**
     * Constructs a new Mieter (tenant) with the specified details.
     *
     * @param mieterID Unique identifier for the tenant.
     * @param wohnung The property associated with this tenant.
     * @param name Last name of the tenant.
     * @param vorname First name of the tenant.
     * @param telefonnummer Contact number of the tenant.
     * @param einkommen Monthly income of the tenant.
     * @param ausgaben Monthly expenses of the tenant.
     * @param mietbeginn The start date of the tenancy.
     * @param mietende The end date of the tenancy.
     * @param kaution Deposit amount paid by the tenant.
     * @param anzahlBewohner Number of inhabitants living in the rented property.
     */

    public Mieter(Long mieterID,
                  Wohnung wohnung,
                  String name,
                  String vorname,
                  long telefonnummer,
                  int einkommen,
                  int ausgaben,
                  Date mietbeginn,
                  Date mietende,
                  int kaution,
                  int anzahlBewohner) {
        this.mieterID = mieterID;
        this.wohnung = wohnung;
        this.name = name;
        this.vorname = vorname;
        this.telefonnummer = telefonnummer;
        this.einkommen = einkommen;
        this.ausgaben = ausgaben;
        this.mietbeginn = mietbeginn;
        this.mietende = mietende;
        this.kaution = kaution;
        this.anzahlBewohner = anzahlBewohner;
    }

    public Long getMieterID() {
        return mieterID;
    }

    public void setMieterID(Long mieterID) {
        this.mieterID = mieterID;
    }

    public Wohnung getWohnung() {
        return wohnung;
    }

    public void setWohnung(Wohnung wohnung) {
        this.wohnung = wohnung;
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

    public long getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(long telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public int getEinkommen() {
        return einkommen;
    }

    public void setEinkommen(int einkommen) {
        if (einkommen < 0) throw new IllegalArgumentException("Einkommen must be positive.");
        this.einkommen = einkommen;
    }

    public int getAusgaben() {
        return ausgaben;
    }

    public void setAusgaben(int ausgaben) {
        if (ausgaben < 0) throw new IllegalArgumentException("Ausgaben must be positive.");
        this.ausgaben = ausgaben;
    }

    public Date getMietbeginn() {
        return mietbeginn;
    }

    public void setMietbeginn(Date mietbeginn) {
        this.mietbeginn = mietbeginn;
    }

    public Date getMietende() {
        return mietende;
    }

    public void setMietende(Date mietende) {
        this.mietende = mietende;
    }

    public int getKaution() {
        return kaution;
    }

    public void setKaution(int kaution) {
        this.kaution = kaution;
    }

    public int getAnzahlBewohner() {
        return anzahlBewohner;
    }

    public void setAnzahlBewohner(int anzahlBewohner) {
        this.anzahlBewohner = anzahlBewohner;
    }

    @Override
    public String toString() {
        return "Mieter[" +
                "mieterID=" + mieterID +
                ", eigentumID=" + (wohnung != null ? wohnung.getWohnungId() : "None") +
                ", name='" + name + '\'' +
                ", vorname='" + vorname + '\'' +
                ", telefonnummer=" + telefonnummer +
                ", einkommen=" + einkommen +
                ", ausgaben=" + ausgaben +
                ", mietbeginn=" + mietbeginn +
                ", mietende=" + mietende +
                ", kaution=" + kaution +
                ", anzahlBewohner=" + anzahlBewohner +
                ']';
    }
}