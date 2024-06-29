package projektarbeit.immobilienverwaltung.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MieterTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidMieter() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Mieter should have no violations");
    }

    @Test
    public void testLongName() {
        Mieter mieter = new Mieter("a".repeat(101), "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with too long Name should have violations");
        assertEquals("Illegal Name", violations.iterator().next().getMessage());
    }

    @Test
    public void testNumberName() {
        Mieter mieter = new Mieter("Max1", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Number in Name should have violations");
        assertEquals("Illegal Name", violations.iterator().next().getMessage());
    }

    @Test
    public void testSpecialCharacterName() {
        Mieter mieter = new Mieter("Max!", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with special character in Name should have violations");
        assertEquals("Illegal Name", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullName() {
        Mieter mieter = new Mieter(null, "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Null Name should have violations");
        assertEquals("Illegal Name", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyName() {
        Mieter mieter = new Mieter("", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty Name should have violations");
        assertEquals("Illegal Name", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidVorname() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Mieter should have no violations");
    }

    @Test
    public void testLongVorname() {
        Mieter mieter = new Mieter("Max", "a".repeat(101), "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with too long Vorname should have violations");
        assertEquals("Illegal Vorname", violations.iterator().next().getMessage());
    }

    @Test
    public void testNumberVorname() {
        Mieter mieter = new Mieter("Max", "Mustermann1", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Number in Vorname should have violations");
        assertEquals("Illegal Vorname", violations.iterator().next().getMessage());
    }

    @Test
    public void testSpecialCharacterVorname() {
        Mieter mieter = new Mieter("Max", "Mustermann!", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with special character in Vorname should have violations");
        assertEquals("Illegal Vorname", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullVorname() {
        Mieter mieter = new Mieter("Max", null, "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Null Vorname should have violations");
        assertEquals("Illegal Vorname", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyVorname() {
        Mieter mieter = new Mieter("Max", "", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty Vorname should have violations");
        assertEquals("Illegal Vorname", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Telefonnummer should have no violations");
    }

    @Test
    public void testEmptyTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty Telefonnummer should have violations");
        assertEquals("Illegal Telefonnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", null, "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with null Telefonnummer should have violations");
        assertEquals("Illegal Telefonnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testLetterInTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "0711A78532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with letter in Telefonnummer should have violations");
        assertEquals("Illegal Telefonnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testShortTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Too short Telefonnummer should have violations");
        assertEquals("Illegal Telefonnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testLongTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "1234567812345678", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Too long Telefonnummer should have violations");
        assertEquals("Illegal Telefonnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidEmail() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid email should have no violations");
    }

    @Test
    public void testInvalidEmail() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with invalid email should have violations");
        assertEquals("Illegal Mail", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullEmail() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", null, 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with null email should have violations");
        assertEquals("Illegal Mail", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyEmail() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty email should have violations");
        assertEquals("Illegal Mail", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidEinkommen() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Einkommen should have no violations");
    }

    @Test
    public void testNegativeEinkommen() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", -3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with negative Einkommen should have violations");
        assertEquals("Einkommen must be positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testLongSetName() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setName("a".repeat(101)),
                "Expected setName to throw, but it didn't"
        );
        assertEquals("Name cannot exceed 100 characters", thrown.getMessage());
    }

    @Test
    public void testNumberSetName() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setName("P3ter"),
                "Expected setName to throw, but it didn't"
        );
        assertEquals("Name must contain only letters", thrown.getMessage());
    }

    @Test
    public void testNullSetName() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setName(null),
                "Expected setName to throw, but it didn't"
        );
        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testEmptySetName() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setName(""),
                "Expected setName to throw, but it didn't"
        );
        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testLongSetVorname() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setVorname("a".repeat(101)),
                "Expected setVorname to throw, but it didn't"
        );
        assertEquals("Vorname cannot exceed 100 characters", thrown.getMessage());
    }

    @Test
    public void testNumberSetVorname() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setVorname("P3ter"),
                "Expected setVorname to throw, but it didn't"
        );
        assertEquals("Vorname must contain only letters", thrown.getMessage());
    }

    @Test
    public void testNullSetVorname() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setVorname(null),
                "Expected setVorname to throw, but it didn't"
        );
        assertEquals("Vorname cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testEmptySetVorname() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setVorname(""),
                "Expected setVorname to throw, but it didn't"
        );
        assertEquals("Vorname cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testEmptySetTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setTelefonnummer(""),
                "Expected setTelefonnummer to throw, but it didn't"
        );
        assertEquals("Invalid Telefonnummer format", thrown.getMessage());
    }

    @Test
    public void testNullSetTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setTelefonnummer(null),
                "Expected setTelefonnummer to throw, but it didn't"
        );
        assertEquals("Invalid Telefonnummer format", thrown.getMessage());
    }

    @Test
    public void testLetterInSetTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setTelefonnummer("0711A78532"),
                "Expected setTelefonnummer to throw, but it didn't"
        );
        assertEquals("Invalid Telefonnummer format", thrown.getMessage());
    }

    @Test
    public void testShortSetTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setTelefonnummer("123"),
                "Expected setTelefonnummer to throw, but it didn't"
        );
        assertEquals("Invalid Telefonnummer format", thrown.getMessage());
    }

    @Test
    public void testLongSetTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setTelefonnummer("1234567812345678"),
                "Expected setTelefonnummer to throw, but it didn't"
        );
        assertEquals("Invalid Telefonnummer format", thrown.getMessage());
    }

    @Test
    public void testNegativeSetEinkommen() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieter.setEinkommen(-1),
                "Expected setEinkommen to throw, but it didn't"
        );
        assertEquals("Einkommen must be positive", thrown.getMessage());
    }
}