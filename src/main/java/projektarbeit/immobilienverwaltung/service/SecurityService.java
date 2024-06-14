package projektarbeit.immobilienverwaltung.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Sicherheitsdienstklasse für die Verwaltung der Authentifizierung und Autorisierung.
 */
@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Konstruktor für die SecurityService-Klasse.
     *
     * @param authenticationContext Der Kontext zur Authentifizierung.
     * @param userRepository        Das Repository für Benutzer.
     * @param passwordEncoder       Der Encoder für Passwörter.
     */
    @Autowired
    public SecurityService(AuthenticationContext authenticationContext, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationContext = authenticationContext;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gibt den authentifizierten Benutzer zurück.
     *
     * @return Die Details des authentifizierten Benutzers.
     */
    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
    }

    /**
     * Meldet den aktuellen Benutzer ab.
     */
    public void logout() {
        authenticationContext.logout();
    }

    /**
     * Speichert einen Benutzer im Repository.
     *
     * @param user Der zu speichernde Benutzer.
     */
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * Verschlüsselt ein Passwort.
     *
     * @param password Das zu verschlüsselnde Passwort.
     * @return Das verschlüsselte Passwort.
     */
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Gibt die Rollen des authentifizierten Benutzers zurück.
     *
     * @return Ein Set von Rollen des authentifizierten Benutzers.
     */
    public Set<String> getAuthenticatedUserRoles() {
        return getAuthenticatedUser().getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());
    }
}
