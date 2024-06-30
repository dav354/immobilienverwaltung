package projektarbeit.immobilienverwaltung.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;

public class MietvertragTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidMietvertrag() {
        Mietvertrag mietvertrag = new Mietvertrag(
                Mockito.mock(Mieter.class),
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2025, 6, 30),
                1500.0,
                800.0,
                2
        );

        var violations = validator.validate(mietvertrag);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testMietbeginnNotNull() {
        Mietvertrag mietvertrag = new Mietvertrag(
                Mockito.mock(Mieter.class),
                Mockito.mock(Wohnung.class),
                null,
                LocalDate.of(2025, 6, 30),
                1500.0,
                800.0,
                2
        );

        var violations = validator.validate(mietvertrag);
        assertEquals(1, violations.size());

        ConstraintViolation<Mietvertrag> violation = violations.iterator().next();
        assertEquals("mietbeginn", violation.getPropertyPath().toString());
        assertEquals("Mietbeginn darf nicht null sein", violation.getMessage());
    }

    @Test
    public void testKautionPositive() {
        Mietvertrag mietvertrag = new Mietvertrag(
                Mockito.mock(Mieter.class),
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2025, 6, 30),
                -500.0, // Negative Kaution
                800.0,
                2
        );

        var violations = validator.validate(mietvertrag);
        assertEquals(1, violations.size());

        ConstraintViolation<Mietvertrag> violation = violations.iterator().next();
        assertEquals("kaution", violation.getPropertyPath().toString());
        assertEquals("Kaution muss positiv sein", violation.getMessage());
    }

    @Test
    public void testMietePositive() {
        Mietvertrag mietvertrag = new Mietvertrag(
                Mockito.mock(Mieter.class),
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2025, 6, 30),
                1500.0,
                -800.0, // Negative Miete
                2
        );

        var violations = validator.validate(mietvertrag);
        assertEquals(1, violations.size());

        ConstraintViolation<Mietvertrag> violation = violations.iterator().next();
        assertEquals("miete", violation.getPropertyPath().toString());
        assertEquals("Miete muss positiv sein", violation.getMessage());
    }

    @Test
    public void testAnzahlBewohnerMinimum() {
        Mietvertrag mietvertrag = new Mietvertrag(
                Mockito.mock(Mieter.class),
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2025, 6, 30),
                1500.0,
                800.0,
                0 // Ungültige Anzahl der Bewohner
        );

        var violations = validator.validate(mietvertrag);
        assertEquals(1, violations.size());

        ConstraintViolation<Mietvertrag> violation = violations.iterator().next();
        assertEquals("anzahlBewohner", violation.getPropertyPath().toString());
        assertEquals("AnzahlBewohner muss mindestens 1 sein", violation.getMessage());
    }

    @Test
    public void testMaxKaution() {
        Mietvertrag mietvertrag = new Mietvertrag(
                Mockito.mock(Mieter.class),
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2025, 6, 30),
                Double.MAX_VALUE, // Sehr hohe Kaution
                800.0,
                2
        );

        var violations = validator.validate(mietvertrag);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testMaxMiete() {
        Mietvertrag mietvertrag = new Mietvertrag(
                Mockito.mock(Mieter.class),
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2025, 6, 30),
                1500.0,
                Double.MAX_VALUE, // Sehr hohe Miete
                2
        );

        var violations = validator.validate(mietvertrag);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testMinAnzahlBewohner() {
        Mietvertrag mietvertrag = new Mietvertrag(
                Mockito.mock(Mieter.class),
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 7, 1),
                LocalDate.of(2025, 6, 30),
                1500.0,
                800.0,
                1 // Minimale gültige Anzahl der Bewohner
        );

        var violations = validator.validate(mietvertrag);
        assertTrue(violations.isEmpty());
    }
}