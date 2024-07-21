package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert eine Mieter-Entität mit Details zum Mieter und deren Mietvertrag.
 * Diese Entität wird der Datenbanktabelle 'mieter' zugeordnet.
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
@Entity
@Table(name = "mieter", indexes = {
        @Index(name = "idx_mieter_email", columnList = "email")
})
public class Mieter implements Comparable<Mieter>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mieterId;

    @OneToMany(mappedBy = "mieter", fetch = FetchType.LAZY)
    private List<Dokument> dokument = new ArrayList<>();

    @Column
    @NotBlank(message = "Name darf nicht leer sein")
    @Size(max = 100, message = "Name darf nicht länger als 100 Zeichen sein")
    @Pattern(regexp = "^[a-zA-ZäöüÄÖÜß\\s'-]+$", message = "Name darf nur Buchstaben enthalten")
    private String name;

    @Column
    @NotBlank(message = "Vorname darf nicht leer sein")
    @Size(max = 100, message = "Vorname darf nicht länger als 100 Zeichen sein")
    @Pattern(regexp = "^[a-zA-ZäöüÄÖÜß\\s'-]+$", message = "Vorname darf nur Buchstaben enthalten")
    private String vorname;

    @Column
    @NotBlank(message = "Telefonnummer darf nicht leer sein")
    @Pattern(regexp = "^\\d{6,12}$", message = "Ungültiges Telefonnummer-Format")
    private String telefonnummer; // can start with 0

    @OneToMany(mappedBy = "mieter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Mietvertrag> mietvertraege = new ArrayList<>();

    @Column
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Ungültige E-Mail-Adresse")
    @NotBlank(message = "E-Mail darf nicht leer sein")
    private String email;

    @Column
    @Positive(message = "Einkommen muss positiv sein")
    @NotNull(message = "Einkommen darf nicht null sein")
    private double einkommen;

    /**
     * Standardkonstruktor für JPA.
     */
    public Mieter() {
    }

    /**
     * Erstellt einen neuen Mieter mit den angegebenen Details.
     *
     * @param name          Nachname des Mieters.
     * @param vorname       Vorname des Mieters.
     * @param telefonnummer Telefonnummer des Mieters.
     * @param email         E-Mail-Adresse des Mieters.
     * @param einkommen     Monatliches Einkommen des Mieters.
     */
    public Mieter(String name,
                  String vorname,
                  String telefonnummer,
                  String email,
                  double einkommen) {
        this.name = name;
        this.vorname = vorname;
        this.telefonnummer = telefonnummer;
        this.email = email;
        this.einkommen = einkommen;
    }

    /**
     * Gibt die ID des Mieters zurück.
     *
     * @return die Mieter-ID.
     */
    public Long getMieter_id() {
        return mieterId;
    }

    /**
     * Setzt die ID des Mieters.
     *
     * @param mieter_id die Mieter-ID.
     */
    public void setMieter_id(Long mieter_id) {
        this.mieterId = mieter_id;
    }

    /**
     * Gibt die Liste der Dokumente zurück, die mit dem Mieter verknüpft sind.
     *
     * @return die Liste der Dokumente.
     */
    public List<Dokument> getDokument() {
        return dokument;
    }

    /**
     * Setzt die Liste der Dokumente für den Mieter.
     *
     * @param dokument die zu setzende Liste der Dokumente.
     */
    public void setDokument(List<Dokument> dokument) {
        this.dokument = dokument;
    }

    /**
     * Gibt den Nachnamen des Mieters zurück.
     *
     * @return der Nachname des Mieters.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Nachnamen des Mieters.
     *
     * @param name der zu setzende Nachname.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Vornamen des Mieters zurück.
     *
     * @return der Vorname des Mieters.
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * Setzt den Vornamen des Mieters.
     *
     * @param vorname der zu setzende Vorname.
     */
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * Gibt die Telefonnummer des Mieters zurück.
     *
     * @return die Telefonnummer des Mieters.
     */
    public String getTelefonnummer() {
        return telefonnummer;
    }

    /**
     * Setzt die Telefonnummer des Mieters.
     *
     * @param telefonnummer die zu setzende Telefonnummer.
     */
    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    /**
     * Gibt die E-Mail-Adresse des Mieters zurück.
     *
     * @return die E-Mail-Adresse des Mieters.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die E-Mail-Adresse des Mieters.
     *
     * @param email die zu setzende E-Mail-Adresse.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gibt das Einkommen des Mieters zurück.
     *
     * @return das Einkommen des Mieters.
     */
    public double getEinkommen() {
        return einkommen;
    }

    /**
     * Setzt das Einkommen des Mieters.
     *
     * @param einkommen das zu setzende Einkommen.
     */
    public void setEinkommen(double einkommen) {
        this.einkommen = einkommen;
    }

    /**
     * Gibt die Liste der Mietverträge zurück, die mit dem Mieter verknüpft sind.
     *
     * @return die Liste der Mietverträge.
     */
    public List<Mietvertrag> getMietvertraege() {
        return mietvertraege;
    }

    /**
     * Setzt die Liste der Mietverträge für den Mieter.
     *
     * @param mietvertraege die zu setzende Liste der Mietverträge.
     */
    public void setMietvertraege(List<Mietvertrag> mietvertraege) {
        this.mietvertraege = mietvertraege;
    }

    /**
     * Gibt den vollständigen Namen des Mieters zurück, indem der Vorname und Nachname kombiniert werden.
     *
     * @return Der vollständige Name des Mieters im Format "Vorname Nachname".
     */
    public String getFullName() {
        return vorname + " " + name;
    }

    @Override
    public int compareTo(Mieter other) {
        return this.getFullName().compareTo(other.getFullName());
    }

    /**
     * Gibt eine String-Darstellung dieses Mieters zurück.
     *
     * @return eine String-Darstellung dieses Mieters.
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