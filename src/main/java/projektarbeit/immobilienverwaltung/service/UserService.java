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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public void saveUser(User user, User createdByAdmin) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedByAdmin(createdByAdmin);
        userRepository.save(user);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findUsersCreatedByAdmin(User admin) {
        return userRepository.findByCreatedByAdmin(admin);
    }

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
