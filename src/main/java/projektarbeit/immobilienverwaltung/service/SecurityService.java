package projektarbeit.immobilienverwaltung.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Sicherheitsdienstklasse für die Verwaltung der Authentifizierung und Autorisierung.
 */
@Service
public class SecurityService {

    private final AuthenticationContext authenticationContext;
    private final UserRepository userRepository;

    /**
     * Konstruktor für die SecurityService-Klasse.
     *
     * @param authenticationContext Der Kontext zur Authentifizierung.
     * @param userRepository        Das Repository für Benutzer.
     */
    @Autowired
    public SecurityService(AuthenticationContext authenticationContext, UserRepository userRepository) {
        this.authenticationContext = authenticationContext;
        this.userRepository = userRepository;
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
    public void save(@Valid User user) {
        userRepository.save(user);
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