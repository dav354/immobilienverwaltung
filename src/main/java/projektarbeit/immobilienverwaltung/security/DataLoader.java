package projektarbeit.immobilienverwaltung.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import projektarbeit.immobilienverwaltung.model.Role;
import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.repository.RoleRepository;
import projektarbeit.immobilienverwaltung.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

/**
 * Konfigurationsklasse, die für das Laden des Standard-Admin-Benutzers
 * in die Datenbank verantwortlich ist, falls dieser noch nicht existiert.
 */
@Configuration
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    // Injection des Admin-Benutzernamens aus der Umgebungsvariable oder den Anwendungseigenschaften
    @Value("${admin.username}")
    private String adminUsername;

    // Injection des Admin-Passworts aus der Umgebungsvariable oder den Anwendungseigenschaften
    @Value("${admin.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Konstruktor für DataLoader.
     *
     * @param userRepository Das Repository zur Interaktion mit Benutzerdaten in der Datenbank.
     * @param roleRepository Das Repository zur Interaktion mit Rollendaten in der Datenbank.
     * @param passwordEncoder Der Encoder zum Verschlüsseln von Benutzerpasswörtern.
     */
    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Initialisiert den Standard-Admin-Benutzer in der Datenbank.
     * Diese Methode wird nach der Konstruktion des Beans ausgeführt.
     * Wenn der Admin-Benutzer nicht in der Datenbank existiert, wird einer mit den angegebenen Anmeldedaten erstellt.
     */
    @PostConstruct
    public void loadAdminUser() {
        logger.info("Starte DataLoader...");

        // Überprüfen, ob der Admin-Benutzer bereits in der Datenbank existiert
        Optional<User> existingAdmin = userRepository.findByUsername(adminUsername);
        if (existingAdmin.isEmpty()) {
            logger.info("Admin-Benutzer nicht gefunden, erstelle einen neuen.");

            // Rollen erstellen, falls nicht vorhanden
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
                logger.info("Rolle 'ADMIN' nicht gefunden, erstelle eine neue.");
                Role role = new Role();
                role.setName("ADMIN");
                return roleRepository.save(role);
            });

            Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
                logger.info("Rolle 'USER' nicht gefunden, erstelle eine neue.");
                Role role = new Role();
                role.setName("USER");
                return roleRepository.save(role);
            });

            // Ein neues User-Objekt für den Admin erstellen
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));

            // Admin-Benutzer in der Datenbank speichern
            userRepository.save(admin);
            logger.info("Admin-Benutzer erstellt mit Benutzername: {} und Passwort: ****", adminUsername);
        } else {
            logger.info("Admin-Benutzer existiert bereits mit Benutzername: {}", adminUsername);
        }
    }
}