package projektarbeit.immobilienverwaltung.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import projektarbeit.immobilienverwaltung.model.Role;
import projektarbeit.immobilienverwaltung.model.User;
import projektarbeit.immobilienverwaltung.repository.RoleRepository;
import projektarbeit.immobilienverwaltung.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        role = new Role();
        role.setName("USER");
    }

    @Test
    public void testInit() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        userService.init();

        verify(roleRepository, times(2)).save(any(Role.class));
    }

    @Test
    public void testSaveUser() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.saveUser(user, user);

        verify(userRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(user, user.getCreatedByAdmin());
    }

    @Test
    public void testUpdatePassword() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.updatePassword(user, "newPassword");

        verify(userRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    public void testFindByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testFindAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> result = userService.findAllUsers();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    public void testFindUsersCreatedByAdmin() {
        when(userRepository.findByCreatedByAdmin(any(User.class))).thenReturn(Collections.singletonList(user));

        List<User> result = userService.findUsersCreatedByAdmin(user);

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }

    @Test
    public void testDeleteUser() {
        Role adminRole = new Role();
        adminRole.setName("ADMIN");

        user.setRoles(Collections.singleton(adminRole));
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        boolean result = userService.deleteUser(user);

        assertFalse(result);
        verify(userRepository, never()).delete(user);
    }

    @Test
    public void testValidateUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Verwenden Sie einen gültigen Benutzernamen zwischen 3 und 10 Zeichen
        assertDoesNotThrow(() -> userService.validateUsername("validUser"));
    }

    @Test
    public void testValidateUsernameThrowsException() {
        // Test ohne Mocking für findByUsername, um die Validierung von Benutzernamen zu testen
        assertThrows(IllegalArgumentException.class, () -> userService.validateUsername("in"));
        assertThrows(IllegalArgumentException.class, () -> userService.validateUsername("invalid-username-"));
        assertThrows(IllegalArgumentException.class, () -> userService.validateUsername("valid-username_"));
    }

    @Test
    public void testValidatePassword() {
        assertDoesNotThrow(() -> userService.validatePassword("ValidPass123@"));
    }

    @Test
    public void testValidatePasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.validatePassword("short"));
    }
}