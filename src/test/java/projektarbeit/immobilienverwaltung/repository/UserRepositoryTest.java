package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.model.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        // Setup User with Roles
        user = new User();
        user.setUsername("john_doe");
        user.setPassword("password123");

        Role role = new Role();
        role.setName("USER");
        entityManager.persist(role);
        entityManager.flush();

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    public void whenFindByUsername_thenUserShouldBeFound() {
        Optional<User> foundUser = userRepository.findByUsername("john_doe");
        assertTrue(foundUser.isPresent(), "User should be found by username");
        assertEquals("john_doe", foundUser.get().getUsername(), "Username should match");
    }

    @Test
    public void whenFindByUsername_withNoUser_thenEmptyOptional() {
        Optional<User> foundUser = userRepository.findByUsername("non_existent_user");
        assertFalse(foundUser.isPresent(), "No user should be found");
    }

    @Test
    public void whenSaveNewUser_thenSuccess() {
        User newUser = new User();
        newUser.setUsername("jane_doe");
        newUser.setPassword("securePassword!123");
        userRepository.save(newUser);
        assertNotNull(newUser.getId(), "New user should have an id after saving");
    }

    @Test
    public void deleteUser_thenNoUserShouldBeFound() {
        userRepository.delete(user);
        Optional<User> foundUser = userRepository.findByUsername("john_doe");
        assertFalse(foundUser.isPresent(), "User should not be found after deletion");
    }

    @Test
    public void whenFindByCreatedByAdmin_thenUsersShouldBeFound() {
        User admin = new User();
        admin.setUsername("admin_user");
        admin.setPassword("admin123");
        entityManager.persist(admin);
        entityManager.flush();

        User newUser = new User();
        newUser.setUsername("jane_admin");
        newUser.setPassword("password123");
        newUser.setCreatedByAdmin(admin);
        entityManager.persist(newUser);
        entityManager.flush();

        List<User> usersCreatedByAdmin = userRepository.findByCreatedByAdmin(admin);
        assertEquals(1, usersCreatedByAdmin.size(), "Should find users created by admin");
        assertEquals("jane_admin", usersCreatedByAdmin.get(0).getUsername(), "Username should match the user created by admin");
    }

}
