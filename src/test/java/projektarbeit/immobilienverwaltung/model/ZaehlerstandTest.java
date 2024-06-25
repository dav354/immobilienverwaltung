package projektarbeit.immobilienverwaltung.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
/*
class ZaehlerstandTest {

    private Zaehlerstand zaehlerstand;
    private Validator validator;

    private Wohnung wohnung;

    private Zaehlerstand testZaehlerstand;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        wohnung = new Wohnung("Teststraße", "11", "83248", "Teststadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        testZaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2024, 1, 1), 1234);

    }

    @Test
    public void testValidAblesedatum() {
        zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2024, 1, 1), 1234);
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertTrue(violations.isEmpty(), "Valid Ablesedatum should have no violations");
    }

    @Test
    public void testNullAblesedatum() {
        zaehlerstand = new Zaehlerstand(wohnung, null, 1234);
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zaehlerstand with null Ablesedatum should have violations");
        assertEquals("Ablesedatum cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidAblesewert() {
        zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2024, 1, 1), 1234);
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertTrue(violations.isEmpty(), "Valid Ablesewert should have no violations");
    }

    @Test
    public void testNullAblesewert() {
        zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2024, 1, 1), 0);
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zaehlerstand with null Ablesewert should have violations");
        assertEquals("Ablesewert cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidWohnung() {
        zaehlerstand = new Zaehlerstand(wohnung, LocalDate.of(2024, 1, 1), 1234);
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertTrue(violations.isEmpty(), "Valid Wohnung should have no violations");
    }

    @Test
    public void testNullWohnung() {
        zaehlerstand = new Zaehlerstand(null, LocalDate.of(2024, 1, 1), 1234);
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zaehlerstand with null Wohnung should have violations");
        assertEquals("Wohnung cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullSetAblesedatum() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testZaehlerstand.setAblesedatum(null),
                "Expected setAblesedatum to throw, but it didn't"
        );
        assertEquals("Ablesedatum cannot be null", thrown.getMessage());
    }

    @Test
    public void testNullSetAblesewert() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testZaehlerstand.setAblesewert(0),
                "Expected setAblesewert to throw, but it didn't"
        );
        assertEquals("Ablesewert must be positive", thrown.getMessage());
    }

    @Test
    public void testNegativeSetAblesewert() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testZaehlerstand.setAblesewert(-1),
                "Expected setAblesewert to throw, but it didn't"
        );
        assertEquals("Ablesewert must be positive", thrown.getMessage());
    }

    @Test
    public void testNullSetWohnung() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testZaehlerstand.setWohnung(null),
                "Expected setWohnung to throw, but it didn't"
        );
        assertEquals("Wohnung cannot be null", thrown.getMessage());
    }
}

 */