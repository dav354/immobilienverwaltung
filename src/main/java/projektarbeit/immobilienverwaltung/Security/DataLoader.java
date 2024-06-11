package projektarbeit.immobilienverwaltung.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import java.util.Collections;

/**
 * Configuration class responsible for loading default admin user
 * into the database if it does not already exist.
 */
@Configuration
public class DataLoader {

    // Injecting the admin username from the environment variable or application properties
    @Value("${admin.username}")
    private String adminUsername;

    // Injecting the admin password from the environment variable or application properties
    @Value("${admin.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for DataLoader.
     *
     * @param userRepository The repository to interact with User data in the database.
     * @param passwordEncoder The encoder to encrypt user passwords.
     */
    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Initializes the default admin user in the database.
     * This method is executed after the bean is constructed.
     * If the admin user does not exist in the database, it creates one with the given credentials.
     */
    @PostConstruct
    public void loadAdminUser() {
        // Check if the admin user already exists in the database
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            // Create a new User object for the admin
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRoles(Collections.singleton("ROLE_ADMIN"));

            // Save the admin user to the database
            userRepository.save(admin);
        }
    }
}