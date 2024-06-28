package projektarbeit.immobilienverwaltung.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
/*
class WohnungTest {

    private Wohnung wohnung;

    @Mock
    private Mieter mieter;

    private Validator validator;

    private Wohnung testWohnung;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        testWohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
    }

    @Test
    public void testValidAdresse() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Valid Wohnung should have no violations");
    }

    @Test
    public void testValidQuadratmeter() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Valid Quadratmeter should have no violations");
    }

    @Test
    public void testNullQuadratmeter() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 0, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung with null Quadratmeter should have violations");
        assertEquals("GesamtQuadratmeter must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    public void testNegativeQuadratmeter() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, -1, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung with negative Quadratmeter should have violations");
        assertEquals("GesamtQuadratmeter must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidBaujahr() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Valid Baujahr should have no violations");
    }

    @Test
    public void testNullBaujahr() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 0, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung with null Baujahr should have violations");
        assertEquals("Baujahr must be a four-digit year and not in the future", violations.iterator().next().getMessage());
    }

    @Test
    public void testThreeDigitBaujahr() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 111, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung with three-digit Baujahr should have violations");
        assertEquals("Baujahr must be a four-digit year and not in the future", violations.iterator().next().getMessage());
    }

    @Test
    public void testFutureBaujahr() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 4444, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung with future Baujahr should have violations");
        assertEquals("Baujahr must be a four-digit year and not in the future", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidAnzahlBaeder() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Valid AnzahlBaeder should have no violations");
    }

    @Test
    public void testNegativeAnzahlBaeder() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, -2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung with negative AnzahlBaeder should have violations");
        assertEquals("AnzahlBäder must be positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullAnzahlBaeder() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 0, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung with null AnzahlBaeder should have violations");
        assertEquals("AnzahlBäder must be positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidAnzahlSchlafzimmer() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Valid AnzahlSchlafzimmer should have no violations");
    }

    @Test
    public void testNegativeAnzahlSchlafzimmer() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, -2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung with negative AnzahlSchlafzimmer should have violations");
        assertEquals("AnzahlSchlafzimmer must be zero or positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullAnzahlSchlafzimmer() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 0, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Wohnung with zero AnzahlSchlafzimmer should have no violations");
    }

    @Test
    public void testValidHatBalkon() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Valid HatBalkon should have no violations");
    }

    @Test
    public void testValidHatTerasse() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Valid HatTerasse should have no violations");
    }

    @Test
    public void testValidHatGarten() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Valid HatGarten should have no violations");
    }

    @Test
    public void testValidHatKlimaanlage() {
        wohnung = new Wohnung("Musterstraße", "11", "Test", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Valid HatKlimaanlage should have no violations");
    }

    @Test
    public void testNullSetQuadratmeter() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testWohnung.setGesamtQuadratmeter(0),
                "Expected setGesamtQuadratmeter to throw, but it didn't"
        );
        assertEquals("GesamtQuadratmeter must be at least 1", thrown.getMessage());
    }

    @Test
    public void testNegativeSetQuadratmeter() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testWohnung.setGesamtQuadratmeter(-200),
                "Expected setGesamtQuadratmeter to throw, but it didn't"
        );
        assertEquals("GesamtQuadratmeter must be at least 1", thrown.getMessage());
    }

    @Test
    public void testNullSetBaujahr() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testWohnung.setBaujahr(0),
                "Expected setBaujahr to throw, but it didn't"
        );
        assertEquals("Baujahr must be a four-digit year and not in the future", thrown.getMessage());
    }

    @Test
    public void testThreeDigitSetBaujahr() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testWohnung.setBaujahr(999),
                "Expected setBaujahr to throw, but it didn't"
        );
        assertEquals("Baujahr must be a four-digit year and not in the future", thrown.getMessage());
    }

    @Test
    public void testFutureSetBaujahr() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testWohnung.setBaujahr(3000),
                "Expected setBaujahr to throw, but it didn't"
        );
        assertEquals("Baujahr must be a four-digit year and not in the future", thrown.getMessage());
    }

    @Test
    public void testNegativeSetAnzahlBaeder() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testWohnung.setAnzahlBaeder(-2),
                "Expected setAnzahlBaeder to throw, but it didn't"
        );
        assertEquals("AnzahlBaeder must be positive", thrown.getMessage());
    }

    @Test
    public void testNullSetAnzahlBaeder() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testWohnung.setAnzahlBaeder(0),
                "Expected setAnzahlBaeder to throw, but it didn't"
        );
        assertEquals("AnzahlBaeder must be positive", thrown.getMessage());
    }

    @Test
    public void testNegativeSetAnzahlSchlafzimmer() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testWohnung.setAnzahlSchlafzimmer(-2),
                "Expected setAnzahlSchlafzimmer to throw, but it didn't"
        );
        assertEquals("AnzahlSchlafzimmer must be zero or positive", thrown.getMessage());
    }
}

 */