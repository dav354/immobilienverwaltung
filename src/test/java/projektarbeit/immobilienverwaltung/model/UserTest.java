package projektarbeit.immobilienverwaltung.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.model.Role;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenSaveUser_thenFindById() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword123");
        user = entityManager.persistFlushFind(user);

        User foundUser = entityManager.find(User.class, user.getId());
        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    public void whenSaveUserWithRole_thenCheckAssociation() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword123");

        Role role = new Role();
        role.setName("USER");
        role = entityManager.persistFlushFind(role);

        user.getRoles().add(role);
        user = entityManager.persistFlushFind(user);

        User foundUser = entityManager.find(User.class, user.getId());
        assertNotNull(foundUser);
        assertTrue(foundUser.getRoles().contains(role));
    }

    @Test
    public void whenAddRoleToUser_thenRoleIsPresent() {
        User user = new User();
        user.setUsername("roleUser");
        user.setPassword("safePass123");

        Role newRole = new Role();
        newRole.setName("ADMIN");
        entityManager.persist(newRole);

        user.getRoles().add(newRole);
        user = entityManager.persistFlushFind(user);

        User retrievedUser = entityManager.find(User.class, user.getId());
        assertTrue(retrievedUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN")));
    }

    @Test
    public void testGetAndSetId() {
        User user = new User();
        user.setId(1L);
        assertEquals(1L, user.getId(), "The ID should match the set value.");
    }

    @Test
    public void testGetAndSetUsername() {
        User user = new User();
        user.setUsername("testUser");
        assertEquals("testUser", user.getUsername(), "The username should match the set value.");
    }

    @Test
    public void testGetAndSetPassword() {
        User user = new User();
        user.setPassword("password123");
        assertEquals("password123", user.getPassword(), "The password should match the set value.");
    }

    @Test
    public void testGetAndSetRoles() {
        User user = new User();
        Role role = new Role();
        role.setName("ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);
        assertTrue(user.getRoles().contains(role), "The roles should include the added role.");
    }

    @Test
    public void testGetAndSetCreatedByAdmin() {
        User user = new User();
        User admin = new User();
        admin.setUsername("adminUser");

        user.setCreatedByAdmin(admin);
        assertEquals(admin, user.getCreatedByAdmin(), "The createdByAdmin should match the set user.");
    }

}
