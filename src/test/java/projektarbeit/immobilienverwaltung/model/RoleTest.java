package projektarbeit.immobilienverwaltung.model;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import projektarbeit.immobilienverwaltung.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@DataJpaTest
public class RoleTest {

    @Test
    public void testRoleSettersAndGetters() {
        Role role = new Role();
        role.setId(1L);
        role.setName("Administrator");

        Set<User> users = new HashSet<>();
        User user = new User();
        users.add(user);
        role.setUsers(users);

        assertEquals(1L, role.getId());
        assertEquals("Administrator", role.getName());
        assertTrue(role.getUsers().contains(user));
    }


    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testRolePersistence() {
        Role role = new Role();
        role.setName("Administrator");
        Role savedRole = roleRepository.save(role);
        Role foundRole = roleRepository.findById(savedRole.getId()).orElseThrow();

        assertNotNull(foundRole);
        assertEquals("Administrator", foundRole.getName());
    }

}
