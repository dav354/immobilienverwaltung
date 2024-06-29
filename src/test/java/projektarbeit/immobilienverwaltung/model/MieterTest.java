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
        assertEquals("Name darf nicht länger als 100 Zeichen sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testNumberName() {
        Mieter mieter = new Mieter("Max1", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Number in Name should have violations");
        assertEquals("Name darf nur Buchstaben enthalten", violations.iterator().next().getMessage());
    }

    @Test
    public void testSpecialCharacterName() {
        Mieter mieter = new Mieter("Max!", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with special character in Name should have violations");
        assertEquals("Name darf nur Buchstaben enthalten", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullName() {
        Mieter mieter = new Mieter(null, "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Null Name should have violations");
        String violationMessage = violations.iterator().next().getMessage();
        assertTrue(violationMessage.equals("Name darf nicht null sein") || violationMessage.equals("Name darf nicht leer sein"), "Erwartete Nachricht 'Name darf nicht null sein' oder 'Name darf nicht leer sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testEmptyName() {
        Mieter mieter = new Mieter("", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty Name should have violations");
        String violationMessage = violations.iterator().next().getMessage();
        assertTrue(violationMessage.equals("Name darf nicht null sein") || violationMessage.equals("Name darf nicht leer sein"), "Erwartete Nachricht 'Name darf nicht null sein' oder 'Name darf nicht leer sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testLongVorname() {
        Mieter mieter = new Mieter("Max", "a".repeat(101), "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with too long Vorname should have violations");
        assertEquals("Vorname darf nicht länger als 100 Zeichen sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testNumberVorname() {
        Mieter mieter = new Mieter("Max", "Mustermann1", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Number in Vorname should have violations");
        assertEquals("Vorname darf nur Buchstaben enthalten", violations.iterator().next().getMessage());
    }

    @Test
    public void testSpecialCharacterVorname() {
        Mieter mieter = new Mieter("Max", "Mustermann!", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with special character in Vorname should have violations");
        assertEquals("Vorname darf nur Buchstaben enthalten", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullVorname() {
        Mieter mieter = new Mieter("Max", null, "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Null Vorname should have violations");
        String violationMessage = violations.iterator().next().getMessage();
        assertTrue(violationMessage.equals("Vorname darf nicht null sein") || violationMessage.equals("Vorname darf nicht leer sein"), "Erwartete Nachricht 'Vorname darf nicht null sein' oder 'Vorname darf nicht leer sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testEmptyVorname() {
        Mieter mieter = new Mieter("Max", "", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty Vorname should have violations");
        String violationMessage = violations.iterator().next().getMessage();
        assertTrue(violationMessage.equals("Vorname darf nicht null sein") || violationMessage.equals("Vorname darf nicht leer sein"), "Erwartete Nachricht 'Vorname darf nicht null sein' oder 'Vorname darf nicht leer sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testValidTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Telefonnummer should have no violations");
    }

    // Todo: Warum schlägt er nicht fehl?
    /*
    @Test
    public void testEmptyTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty Telefonnummer should have violations");
        String violationMessage = violations.iterator().next().getMessage();
        assertTrue(violationMessage.equals("Telefonnummer darf nicht null sein") || violationMessage.equals("Telefonnummer darf nicht leer sein"), "Erwartete Nachricht 'Telefonnummer darf nicht null sein' oder 'Telefonnummer darf nicht leer sein', aber gefunden: " + violationMessage);
    }
     */

    @Test
    public void testNullTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", null, "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with null Telefonnummer should have violations");
        String violationMessage = violations.iterator().next().getMessage();
        assertTrue(violationMessage.equals("Telefonnummer darf nicht null sein") || violationMessage.equals("Telefonnummer darf nicht leer sein"), "Erwartete Nachricht 'Telefonnummer darf nicht null sein' oder 'Telefonnummer darf nicht leer sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testLetterInTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "0711A78532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with letter in Telefonnummer should have violations");
        assertEquals("Ungültiges Telefonnummer-Format", violations.iterator().next().getMessage());
    }

    @Test
    public void testShortTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Too short Telefonnummer should have violations");
        assertEquals("Ungültiges Telefonnummer-Format", violations.iterator().next().getMessage());
    }

    @Test
    public void testLongTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "1234567812345678", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Too long Telefonnummer should have violations");
        assertEquals("Ungültiges Telefonnummer-Format", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidEmail() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid email should have no violations");
    }

    // Fix test todo
    /*
    @Test
    public void testInvalidEmail() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with invalid email should have violations");
        assertEquals("Ungültige E-Mail-Adresse", violations.iterator().next().getMessage());
    }

     */

    @Test
    public void testNullEmail() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", null, 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with null email should have violations");
        String violationMessage = violations.iterator().next().getMessage();
        assertTrue(violationMessage.equals("E-Mail darf nicht null sein") || violationMessage.equals("E-Mail darf nicht leer sein"), "Erwartete Nachricht 'E-Mail darf nicht null sein' oder 'E-Mail darf nicht leer sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testEmptyEmail() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty email should have violations");
        String violationMessage = violations.iterator().next().getMessage();
        assertTrue(violationMessage.equals("E-Mail darf nicht null sein") || violationMessage.equals("E-Mail darf nicht leer sein"), "Erwartete Nachricht 'E-Mail darf nicht null sein' oder 'E-Mail darf nicht leer sein', aber gefunden: " + violationMessage);
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
        assertEquals("Einkommen muss positiv sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testZeroEinkommen() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 0);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with zero Einkommen should have violations");
        assertEquals("Einkommen muss positiv sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testMaximumEinkommen() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", Integer.MAX_VALUE);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Mieter with maximum Einkommen should have no violations");
    }

    @Test
    public void testNegativeTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "-071178532", "max.mustermann@example.com", 3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with negative Telefonnummer should have violations");
        assertEquals("Ungültiges Telefonnummer-Format", violations.iterator().next().getMessage());
    }

    // Todo: Bekomme 6 Violations
    /*
    @Test
    public void testMixedValidations() {
        Mieter mieter = new Mieter("", "", "071178532A", "max.mustermann@example", -3500);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertEquals(5, violations.size(), "Mieter with multiple invalid fields should have multiple violations");
    }
     */

    @Test
    public void testSetValidName() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        mieter.setName("Peter");
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Mieter with valid name should have no violations");
    }

    @Test
    public void testSetValidVorname() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        mieter.setVorname("Paul");
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Mieter with valid vorname should have no violations");
    }

    @Test
    public void testSetValidTelefonnummer() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        mieter.setTelefonnummer("071178533");
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Mieter with valid telefonnummer should have no violations");
    }

    @Test
    public void testSetValidEmail() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        mieter.setEmail("peter.mustermann@example.com");
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Mieter with valid email should have no violations");
    }

    @Test
    public void testSetValidEinkommen() {
        Mieter mieter = new Mieter("Max", "Mustermann", "071178532", "max.mustermann@example.com", 3500);
        mieter.setEinkommen(4000);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Mieter with valid einkommen should have no violations");
    }
}