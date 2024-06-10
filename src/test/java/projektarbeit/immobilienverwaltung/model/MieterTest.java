package projektarbeit.immobilienverwaltung.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
/*
class MieterTest {

    private Validator validator;

    private Mieter mieter;

    private Mieter mieterTest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mieterTest = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
    }

    @Test
    public void testValidName() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Mieter should have no violations");
    }

    @Test
    public void testLongName() {
        String longName = "a".repeat(101);
        mieter = new Mieter(longName, "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with too long Name should have violations");
        assertEquals("Illegal Name", violations.iterator().next().getMessage());
    }

    @Test
    public void testNumberName() {
        mieter = new Mieter("Max1", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Number in Name should have violations");
        assertEquals("Illegal Name", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullName() {
        mieter = new Mieter(null, "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Null Name should have violations");
        assertEquals("Illegal Name", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyName() {
        mieter = new Mieter("", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty Name should have violations");
        assertEquals("Illegal Name", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidVorname() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Mieter should have no violations");
    }

    @Test
    public void testLongVorname() {
        String longVorname = "a".repeat(101);
        mieter = new Mieter("Max", longVorname, "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with too long Vorname should have violations");
        assertEquals("Illegal Vorname", violations.iterator().next().getMessage());
    }

    @Test
    public void testNumberVorname() {
        mieter = new Mieter("Max", "Mustermann1", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Number in Vorname should have violations");
        assertEquals("Illegal Vorname", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullVorname() {
        mieter = new Mieter("Max", null, "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Null Vorname should have violations");
        assertEquals("Illegal Vorname", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyVorname() {
        mieter = new Mieter("Max", "", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty Vorname should have violations");
        assertEquals("Illegal Vorname", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidTelefonnummer() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Telefonnummer should have no violations");
    }

    @Test
    public void testEmptyTelefonnummer() {
        mieter = new Mieter("Max", "Mustermann", "", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with empty Telefonnummer should have violations");
        assertEquals("Illegal Telefonnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullTelefonnummer() {
        mieter = new Mieter("Max", "Mustermann", null, 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with null Telefonnummer should have violations");
        assertEquals("Illegal Telefonnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testLetterInTelefonnummer() {
        mieter = new Mieter("Max", "Mustermann", "0711A78532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with letter in Telefonnummer should have violations");
        assertEquals("Illegal Telefonnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testShortTelefonnummer() {
        mieter = new Mieter("Max", "Mustermann", "071", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Too short Telefonnummer should have violations");
        assertEquals("Illegal Telefonnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testLongTelefonnummer() {
        mieter = new Mieter("Max", "Mustermann", "1234567812345678", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Too long Telefonnummer should have violations");
        assertEquals("Illegal Telefonnummer", violations.iterator().next().getMessage());
    }


    @Test
    public void testZeroAtBeginTelefonnummer() {
        mieter = new Mieter("Max", "Mustermann", "0711178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Telefonnummer starting with 0 should have no violations");
    }

    @Test
    public void testValidEinkommen() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Einkommen should have no violations");
    }

    @Test
    public void testNegativeEinkommen() {
        mieter = new Mieter("Max", "Mustermann", "071178532", -3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with negative Einkommen should have violations");
        assertEquals("Einkommen must be positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidMietbeginn() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Mietbeginn should have no violations");
    }

    @Test
    public void testNullMietbeginn() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, null, LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Mieter with null Mietbeginn should have no violations");
    }

    @Test
    public void testValidMietende() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Mietende should have no violations");
    }

    @Test
    public void testNullMietende() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), null, 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Mieter with null Mietende should have no violations");
    }

    @Test
    public void testMietbeginnAfterMietende() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2026, 1, 1), LocalDate.of(2024, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with Mietbeginn after Mietende should have violations");
        assertEquals("Mietbeginn must be before Mietende", violations.iterator().next().getMessage());
    }

    @Test
    public void testMietbeginnAndMietendeSameDate() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertEquals("Mietbeginn must be before Mietende", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidKaution() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid Kaution should have no violations");
    }

    @Test
    public void testNegativeKaution() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), -2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with negative Kaution should have violations");
        assertEquals("Kaution must be positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullKaution() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 0, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with null Kaution should have violations");
        assertEquals("Kaution must be positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidAnzahlBewohner() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertTrue(violations.isEmpty(), "Valid AnzahlBewohner should have no violations");
    }

    @Test
    public void testNegativeAnzahlBewohner() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, -2);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with negative AnzahlBewohner should have violations");
        assertEquals("AnzahlBewohner must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    public void testZeroAnzahlBewohner() {
        mieter = new Mieter("Max", "Mustermann", "071178532", 3500, LocalDate.of(2024, 1, 1), LocalDate.of(2026, 1, 1), 2000, 0);
        Set<ConstraintViolation<Mieter>> violations = validator.validate(mieter);
        assertFalse(violations.isEmpty(), "Mieter with zero AnzahlBewohner should have violations");
        assertEquals("AnzahlBewohner must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    public void testLongSetName() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setName("a".repeat(101)),
                "Expected setName to throw, but it didn't"
        );
        assertEquals("Name cannot exceed 100 characters", thrown.getMessage());
    }

    @Test
    public void testNumberSetName() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setName("P3ter"),
                "Expected setName to throw, but it didn't"
        );
        assertEquals("Name must contain only letters", thrown.getMessage());
    }

    @Test
    public void testNullSetName() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setName(null),
                "Expected setName to throw, but it didn't"
        );
        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testEmptySetName() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setName(""),
                "Expected setName to throw, but it didn't"
        );
        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testLongSetVorname() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setVorname("a".repeat(101)),
                "Expected setVorname to throw, but it didn't"
        );
        assertEquals("Vorname cannot exceed 100 characters", thrown.getMessage());
    }

    @Test
    public void testNumberSetVorname() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setVorname("P3ter"),
                "Expected setVorname to throw, but it didn't"
        );
        assertEquals("Vorname must contain only letters", thrown.getMessage());
    }

    @Test
    public void testNullSetVorname() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setVorname(null),
                "Expected setVorname to throw, but it didn't"
        );
        assertEquals("Vorname cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testEmptySetVorname() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setVorname(""),
                "Expected setVorname to throw, but it didn't"
        );
        assertEquals("Vorname cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testEmptySetTelefonnummer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setTelefonnummer(""),
                "Expected setTelefonnummer to throw, but it didn't"
        );
        assertEquals("Invalid Telefonnummer format", thrown.getMessage());
    }

    @Test
    public void testNullSetTelefonnummer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setTelefonnummer(null),
                "Expected setTelefonnummer to throw, but it didn't"
        );
        assertEquals("Invalid Telefonnummer format", thrown.getMessage());
    }

    @Test
    public void testLetterInSetTelefonnummer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setTelefonnummer("0711A78532"),
                "Expected setTelefonnummer to throw, but it didn't"
        );
        assertEquals("Invalid Telefonnummer format", thrown.getMessage());
    }

    @Test
    public void testShortSetTelefonnummer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setTelefonnummer("123"),
                "Expected setTelefonnummer to throw, but it didn't"
        );
        assertEquals("Invalid Telefonnummer format", thrown.getMessage());
    }

    @Test
    public void testLongSetTelefonnummer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setTelefonnummer("1234567812345678"),
                "Expected setTelefonnummer to throw, but it didn't"
        );
        assertEquals("Invalid Telefonnummer format", thrown.getMessage());
    }

    @Test
    public void testNegativeSetEinkommen() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setEinkommen(-1),
                "Expected setEinkommen to throw, but it didn't"
        );
        assertEquals("Einkommen must be positive", thrown.getMessage());
    }

    @Test
    public void testMietbeginnAfterSetMietende() {
        mieterTest.setMietbeginn(LocalDate.of(2024, 1, 1));
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setMietende(LocalDate.of(2022, 1, 1)),
                "Expected setMietende to throw, but it didn't"
        );
        assertEquals("Mietbeginn must be before Mietende", thrown.getMessage());
    }

    @Test
    public void testMietbeginnAndMietendeSetSameDate() {
        mieterTest.setMietbeginn(null);
        mieterTest.setMietende(null);
        mieterTest.setMietbeginn(LocalDate.of(2024, 1, 1));
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setMietende(LocalDate.of(2024, 1, 1)),
                "Expected setMietende to throw, but it didn't"
        );
        assertEquals("Mietbeginn must be before Mietende", thrown.getMessage());
    }

    @Test
    public void testNegativeSetKaution() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setKaution(-1),
                "Expected setKaution to throw, but it didn't"
        );
        assertEquals("Kaution must be positive", thrown.getMessage());
    }

    @Test
    public void testZeroSetKaution() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setKaution(0),
                "Expected setKaution to throw, but it didn't"
        );
        assertEquals("Kaution must be positive", thrown.getMessage());
    }

    @Test
    public void testNegativeSetAnzahlBewohner() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setAnzahlBewohner(-1),
                "Expected setAnzahlBewohner to throw, but it didn't"
        );
        assertEquals("AnzahlBewohner must be at least 1", thrown.getMessage());
    }

    @Test
    public void testZeroSetAnzahlBewohner() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> mieterTest.setAnzahlBewohner(0),
                "Expected setAnzahlBewohner to throw, but it didn't"
        );
        assertEquals("AnzahlBewohner must be at least 1", thrown.getMessage());
    }

}

 */