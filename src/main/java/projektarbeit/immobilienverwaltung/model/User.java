package projektarbeit.immobilienverwaltung.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Repräsentiert einen Benutzer in der Anwendung.
 * Diese Entität wird der Tabelle 'app_user' zugeordnet.
 */
@Entity
@Table(name = "app_user")
public class User {

    /**
     * Eindeutige Kennung des Benutzers.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Benutzername des Benutzers. Muss eindeutig und darf nicht null sein.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Passwort des Benutzers. Darf nicht null sein.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Rollen des Benutzers. Wird eager geladen.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Admin, der diesen Benutzer erstellt hat.
     */
    @ManyToOne
    @JoinColumn(name = "created_by_admin_id")
    private User createdByAdmin;

    /**
     * Gibt die ID des Benutzers zurück.
     *
     * @return die ID des Benutzers.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID des Benutzers.
     *
     * @param id die ID des Benutzers.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Benutzernamen des Benutzers zurück.
     *
     * @return der Benutzername des Benutzers.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setzt den Benutzernamen des Benutzers.
     *
     * @param username der Benutzername des Benutzers.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gibt das Passwort des Benutzers zurück.
     *
     * @return das Passwort des Benutzers.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setzt das Passwort des Benutzers.
     *
     * @param password das Passwort des Benutzers.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gibt die Rollen des Benutzers zurück.
     *
     * @return die Rollen des Benutzers.
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Setzt die Rollen des Benutzers.
     *
     * @param roles die Rollen des Benutzers.
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * Gibt den Admin zurück, der diesen Benutzer erstellt hat.
     *
     * @return der Admin, der diesen Benutzer erstellt hat.
     */
    public User getCreatedByAdmin() {
        return createdByAdmin;
    }

    /**
     * Setzt den Admin, der diesen Benutzer erstellt hat.
     *
     * @param createdByAdmin der Admin, der diesen Benutzer erstellt hat.
     */
    public void setCreatedByAdmin(User createdByAdmin) {
        this.createdByAdmin = createdByAdmin;
    }
}