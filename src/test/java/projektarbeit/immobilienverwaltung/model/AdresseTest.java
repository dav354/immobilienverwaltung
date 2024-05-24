package projektarbeit.immobilienverwaltung.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AdresseTest {

    private Validator validator;

    @Mock
    private Postleitzahl postleitzahl;

    @InjectMocks
    private Adresse adresse;

    private Adresse testAdresse;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        testAdresse = new Adresse(postleitzahl, "Musterstraße", "41b");
    }

    @Test
    public void testValidAdresse() {
        adresse = new Adresse(postleitzahl, "Musterstraße", "41b");
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertTrue(violations.isEmpty(), "Valid Adresse should have no violations");
    }

    @Test
    public void testNullPostleitzahl() {
        adresse = new Adresse(null, "Musterstraße", "4b");
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertFalse(violations.isEmpty(), "Adresse with null Postleitzahl should have violations");
        assertEquals("Postleitzahl cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyStrasse() {
        adresse = new Adresse(postleitzahl, "", "4b");
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertFalse(violations.isEmpty(), "Adresse with empty Strasse should have violations");
        assertEquals("Illegal Strasse", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullStrasse() {
        adresse = new Adresse(postleitzahl, null, "4b");
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertFalse(violations.isEmpty(), "Adresse with empty Strasse should have violations");
        assertEquals("Illegal Strasse", violations.iterator().next().getMessage());
    }

    @Test
    public void testLongStrasse() {
        String longStrasse = "a".repeat(101);
        adresse = new Adresse(postleitzahl, longStrasse, "4b");
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertFalse(violations.isEmpty(), "Adresse with long Strasse should have violations");
        assertEquals("Illegal Strasse", violations.iterator().next().getMessage());
    }

    @Test
    public void testNumberStrasse() {
        adresse = new Adresse(postleitzahl, "Musterstraße1", "4b");
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertFalse(violations.isEmpty(), "Adresse with long Strasse should have violations");
        assertEquals("Illegal Strasse", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullHausnummer() {
        adresse = new Adresse(postleitzahl, "Musterstraße", null);
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertFalse(violations.isEmpty(), "Adresse with null Hausnummer should have violations");
        assertEquals("Illegal Hausnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testOnlyLetterHausnummer() {
        adresse = new Adresse(postleitzahl, "Musterstraße", "abc");
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertFalse(violations.isEmpty(), "Adresse with null Hausnummer should have violations");
        assertEquals("Illegal Hausnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testWrongLetterPatternHausnummer() {
        adresse = new Adresse(postleitzahl, "Musterstraße", "b11");
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertFalse(violations.isEmpty(), "Adresse with null Hausnummer should have violations");
        assertEquals("Illegal Hausnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testLongHausnummer() {
        String longHausnummer = "1234567";
        adresse = new Adresse(postleitzahl, "Musterstraße", longHausnummer);
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertFalse(violations.isEmpty(), "Adresse with long Hausnummer should have violations");
        assertEquals("Illegal Hausnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyHausnummer() {
        adresse = new Adresse(postleitzahl, "Musterstraße", "");
        Set<ConstraintViolation<Adresse>> violations = validator.validate(adresse);
        assertFalse(violations.isEmpty(), "Adresse with null Hausnummer should have violations");
        assertEquals("Illegal Hausnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullSetPostleitzahl() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testAdresse.setPostleitzahlObj(null),
                "Expected setPostleitzahlObj(null) to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Postleitzahl cannot be null"));
    }

    @Test
    public void testEmptySetStrasse() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testAdresse.setStrasse(""),
                "Expected setStrasse('') to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Strasse cannot be null"));
    }

    @Test
    public void testNullSetStrasse() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testAdresse.setStrasse(null),
                "Expected setStrasse(null) to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Strasse cannot be null"));
    }

    @Test
    public void testLongSetStrasse() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    String longStrasse = "a".repeat(101);
                    testAdresse.setStrasse(longStrasse);
                },
                "Expected setStrasse(longStrasse) to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Strasse must be less than or equal to 100 characters"));
    }

    @Test
    public void testNumberSetStrasse() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testAdresse.setStrasse("Stra55e"),
                "Expected setStrasse('Stra55e') to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Strasse must contain only letters"));
    }

    @Test
    public void testNullSetHausnummer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testAdresse.setHausnummer(null),
                "Expected setHausnummer(null) to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Hausnummer cannot be null"));
    }

    @Test
    public void testOnlyLetterSetHausnummer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testAdresse.setHausnummer("AA"),
                "Expected setHausnummer('AA') to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Hausnummer must be numeric with an optional letter at the end"));
    }

    @Test
    public void testWrongLetterPatternSetHausnummer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testAdresse.setHausnummer("b1"),
                "Expected setHausnummer('b1') to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Hausnummer must be numeric with an optional letter at the end"));
    }

    @Test
    public void testLongSetHausnummer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testAdresse.setHausnummer("1234567"),
                "Expected setHausnummer('1234567') to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Hausnummer must be less than or equal to 6 characters"));
    }

    @Test
    public void testEmptySetHausnummer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testAdresse.setHausnummer(""),
                "Expected setHausnummer('') to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Hausnummer cannot be null"));
    }
}