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

@Component
public class SecurityService {

    private final AuthenticationContext authenticationContext;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityService(AuthenticationContext authenticationContext, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationContext = authenticationContext;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
    }

    public void logout() {
        authenticationContext.logout();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Set<String> getAuthenticatedUserRoles() {
        return getAuthenticatedUser().getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toSet());
    }
}
