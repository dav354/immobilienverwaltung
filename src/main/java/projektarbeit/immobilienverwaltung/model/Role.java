package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Repräsentiert eine Rolle in der Anwendung.
 * Diese Entität wird der Tabelle 'role' zugeordnet.
 */
@Entity
public class Role {

    /**
     * Eindeutige Kennung der Rolle.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name der Rolle. Muss eindeutig und darf nicht null sein.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Benutzer, die dieser Rolle zugeordnet sind.
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    /**
     * Gibt die ID der Rolle zurück.
     *
     * @return die ID der Rolle.
     */
    public long getId() {
        return id;
    }

    /**
     * Setzt die ID der Rolle.
     *
     * @param id die ID der Rolle.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Namen der Rolle zurück.
     *
     * @return der Name der Rolle.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Rolle.
     *
     * @param name der Name der Rolle.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die Benutzer zurück, die dieser Rolle zugeordnet sind.
     *
     * @return die Benutzer, die dieser Rolle zugeordnet sind.
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Setzt die Benutzer, die dieser Rolle zugeordnet sind.
     *
     * @param users die Benutzer, die dieser Rolle zugeordnet sind.
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
