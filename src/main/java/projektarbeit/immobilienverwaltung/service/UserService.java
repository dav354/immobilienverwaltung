package projektarbeit.immobilienverwaltung.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.Role;
import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.repository.RoleRepository;
import projektarbeit.immobilienverwaltung.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Service-Klasse für die Verwaltung von Benutzern und Rollen.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Initialisiert die Standardrollen "ADMIN" und "USER", wenn sie nicht vorhanden sind.
     */
    @PostConstruct
    public void init() {
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
    }

    /**
     * Speichert einen Benutzer mit dem angegebenen Ersteller (Admin).
     *
     * @param user           Der zu speichernde Benutzer.
     * @param createdByAdmin Der Admin, der den Benutzer erstellt hat.
     */
    public void saveUser(User user, User createdByAdmin) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedByAdmin(createdByAdmin);
        userRepository.save(user);
    }

    /**
     * Aktualisiert das Passwort eines Benutzers.
     *
     * @param user        Der Benutzer, dessen Passwort aktualisiert wird.
     * @param newPassword Das neue Passwort.
     */
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Findet einen Benutzer anhand des Benutzernamens.
     *
     * @param username Der Benutzername.
     * @return Ein Optional, das den Benutzer enthält, falls vorhanden.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Findet alle Benutzer.
     *
     * @return Eine Liste aller Benutzer.
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Findet alle Benutzer, die von einem bestimmten Admin erstellt wurden.
     *
     * @param admin Der Admin, der die Benutzer erstellt hat.
     * @return Eine Liste der Benutzer, die von diesem Admin erstellt wurden.
     */
    public List<User> findUsersCreatedByAdmin(User admin) {
        return userRepository.findByCreatedByAdmin(admin);
    }

    /**
     * Löscht einen Benutzer.
     *
     * @param user Der zu löschende Benutzer.
     * @return true, wenn der Benutzer gelöscht wurde, false, wenn der letzte Admin nicht gelöscht werden kann.
     */
    public boolean deleteUser(User user) {
        List<User> admins = userRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN")))
                .toList();

        if (admins.size() <= 1 && user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"))) {
            return false;
        }

        userRepository.delete(user);
        return true;
    }

    /**
     * Validiert einen Benutzernamen nach bestimmten Regeln.
     *
     * @param username Der zu validierende Benutzername.
     * @throws IllegalArgumentException Wenn der Benutzername ungültig ist.
     */
    public void validateUsername(String username) {
        if (username.length() < 3 || username.length() > 10) {
            throw new IllegalArgumentException("Username must be between 3 and 10 characters");
        }
        if (!Pattern.matches("^[a-zA-Z0-9_-]+$", username)) {
            throw new IllegalArgumentException("Username can only contain alphanumeric characters, underscores, and dashes");
        }
        if (Pattern.matches("^[_-].*|.*[_-]$", username)) {
            throw new IllegalArgumentException("Username cannot start or end with an underscore or dash");
        }
        if (Pattern.matches(".*[_-]{2,}.*", username)) {
            throw new IllegalArgumentException("Username cannot contain consecutive underscores or dashes");
        }
        if (findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    /**
     * Validiert ein Passwort nach bestimmten Regeln.
     *
     * @param password Das zu validierende Passwort.
     * @param username Der Benutzername, der im Passwort nicht enthalten sein darf.
     * @throws IllegalArgumentException Wenn das Passwort ungültig ist.
     */
    public void validatePassword(String password, String username) {
        if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("Password must be between 8 and 20 characters");
        }
        if (!Pattern.matches(".*[A-Z].*", password)) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        if (!Pattern.matches(".*[a-z].*", password)) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        if (!Pattern.matches(".*\\d.*", password)) {
            throw new IllegalArgumentException("Password must contain at least one digit");
        }
        if (!Pattern.matches(".*[@#$%^&+=!].*", password)) {
            throw new IllegalArgumentException("Password must contain at least one special character (@#$%^&+=!)");
        }
    }
}
