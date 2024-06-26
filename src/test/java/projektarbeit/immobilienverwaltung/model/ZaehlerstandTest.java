package projektarbeit.immobilienverwaltung.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ZaehlerstandTest {

    private Zaehlerstand zaehlerstand;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidZaehlerstand() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 1, 1),
                1234,
                "Stromzähler"
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertTrue(violations.isEmpty(), "Gültiger Zählerstand sollte keine Verstöße haben");
    }

    @Test
    public void testNullAblesedatum() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                null,
                1234,
                "Stromzähler"
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zählerstand mit null Ablesedatum sollte Verstöße haben");
        assertEquals("Ablesedatum darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullAblesewert() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 1, 1),
                0,
                "Stromzähler"
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zählerstand mit null Ablesewert sollte Verstöße haben");
        assertEquals("Ablesewert muss positiv sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullWohnung() {
        zaehlerstand = new Zaehlerstand(
                null,
                LocalDate.of(2024, 1, 1),
                1234,
                "Stromzähler"
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zählerstand mit null Wohnung sollte Verstöße haben");
        assertEquals("Wohnung darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testNegativeAblesewert() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 1, 1),
                -1,
                "Stromzähler"
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zählerstand mit negativem Ablesewert sollte Verstöße haben");
        assertEquals("Ablesewert muss positiv sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyZaehlername() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 1, 1),
                1234,
                ""
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zählerstand mit leerem Zählername sollte Verstöße haben");
        assertEquals("Name darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullZaehlername() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 1, 1),
                1234,
                null
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zählerstand mit null Zählername sollte Verstöße haben");
        assertEquals("Name darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidName() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 1, 1),
                1234,
                "Stromzähler"
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertTrue(violations.isEmpty(), "Gültiger Name sollte keine Verstöße haben");
    }

    @Test
    public void testNullName() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 1, 1),
                1234,
                null
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zählerstand mit null Name sollte Verstöße haben");
        assertEquals("Name darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyName() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 1, 1),
                1234,
                ""
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertFalse(violations.isEmpty(), "Zählerstand mit leerem Name sollte Verstöße haben");
        assertEquals("Name darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testMaxAblesewert() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 1, 1),
                Integer.MAX_VALUE,
                "Stromzähler"
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertTrue(violations.isEmpty(), "Maximaler Ablesewert sollte keine Verstöße haben");
    }

    @Test
    public void testMinPositiveAblesewert() {
        zaehlerstand = new Zaehlerstand(
                Mockito.mock(Wohnung.class),
                LocalDate.of(2024, 1, 1),
                1,
                "Stromzähler"
        );
        Set<ConstraintViolation<Zaehlerstand>> violations = validator.validate(zaehlerstand);
        assertTrue(violations.isEmpty(), "Minimaler positiver Ablesewert sollte keine Verstöße haben");
    }
}