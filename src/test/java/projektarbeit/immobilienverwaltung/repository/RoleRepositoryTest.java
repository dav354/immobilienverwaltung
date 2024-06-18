package projektarbeit.immobilienverwaltung.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import projektarbeit.immobilienverwaltung.model.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setup() {
        Role role = new Role();
        role.setName("ADMIN");
        entityManager.persist(role);
        entityManager.flush();
    }

    @Test
    public void whenFindByName_thenReturnRole() {
        Optional<Role> found = roleRepository.findByName("ADMIN");
        assertTrue(found.isPresent(), "Role should be found with the name ADMIN");
        assertEquals("ADMIN", found.get().getName(), "Role name should match");
    }

    @Test
    public void whenFindByName_notFound_thenEmptyOptional() {
        Optional<Role> found = roleRepository.findByName("NON_EXISTENT_ROLE");
        assertFalse(found.isPresent(), "Role should not be found");
    }

    @Test
    public void testRolePersistence() {
        Role newRole = new Role();
        newRole.setName("USER");
        Role savedRole = roleRepository.save(newRole);
        Role foundRole = entityManager.find(Role.class, savedRole.getId());
        assertNotNull(foundRole, "Role should be successfully saved and retrieved");
        assertEquals("USER", foundRole.getName(), "Role name should be USER");
    }

}
