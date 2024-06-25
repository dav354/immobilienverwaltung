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
 * Configuration class responsible for loading default admin user
 * into the database if it does not already exist.
 */
@Configuration
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    // Injecting the admin username from the environment variable or application properties
    @Value("${admin.username}")
    private String adminUsername;

    // Injecting the admin password from the environment variable or application properties
    @Value("${admin.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for DataLoader.
     *
     * @param userRepository The repository to interact with User data in the database.
     * @param roleRepository The repository to interact with Role data in the database.
     * @param passwordEncoder The encoder to encrypt user passwords.
     */
    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Initializes the default admin user in the database.
     * This method is executed after the bean is constructed.
     * If the admin user does not exist in the database, it creates one with the given credentials.
     */
    @PostConstruct
    public void loadAdminUser() {
        logger.info("Starting DataLoader...");

        // Check if the admin user already exists in the database
        Optional<User> existingAdmin = userRepository.findByUsername(adminUsername);
        if (existingAdmin.isEmpty()) {
            logger.info("Admin user not found, creating a new one.");

            // Create roles if not present
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
                logger.info("Role 'ADMIN' not found, creating a new one.");
                Role role = new Role();
                role.setName("ADMIN");
                return roleRepository.save(role);
            });

            Role userRole = roleRepository.findByName("USER").orElseGet(() -> {
                logger.info("Role 'USER' not found, creating a new one.");
                Role role = new Role();
                role.setName("USER");
                return roleRepository.save(role);
            });

            // Create a new User object for the admin
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRoles(new HashSet<>(Collections.singletonList(adminRole)));

            // Save the admin user to the database
            userRepository.save(admin);
            logger.info("Admin user created with username: {} and password: ****", adminUsername);
        } else {
            logger.info("Admin user already exists with username: {}", adminUsername);
        }
    }
}