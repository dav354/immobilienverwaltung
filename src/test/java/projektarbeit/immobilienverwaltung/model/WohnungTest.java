package projektarbeit.immobilienverwaltung.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WohnungTest {

    private Wohnung wohnung;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidAdresse() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültige Wohnung sollte keine Verstöße haben");
    }

    @Test
    public void testValidQuadratmeter() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültige Quadratmeter sollten keine Verstöße haben");
    }

    @Test
    public void testNullQuadratmeter() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 0, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit null Quadratmetern sollte Verstöße haben");
        assertEquals("GesamtQuadratmeter muss mindestens 1 sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testNegativeQuadratmeter() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, -1, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit negativen Quadratmetern sollte Verstöße haben");
        assertEquals("GesamtQuadratmeter muss mindestens 1 sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidBaujahr() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültiges Baujahr sollte keine Verstöße haben");
    }

    @Test
    public void testThreeDigitBaujahr() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 999, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit dreistelligem Baujahr sollte Verstöße haben");
        assertEquals("Baujahr muss eine vierstellige Zahl sein und darf nicht in der Zukunft liegen", violations.iterator().next().getMessage());
    }

    @Test
    public void testFutureBaujahr() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 3000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit zukünftigem Baujahr sollte Verstöße haben");
        assertEquals("Baujahr muss eine vierstellige Zahl sein und darf nicht in der Zukunft liegen", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidAnzahlBaeder() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültige Anzahl Bäder sollte keine Verstöße haben");
    }

    @Test
    public void testNegativeAnzahlBaeder() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, -2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit negativer Anzahl Bäder sollte Verstöße haben");
        assertEquals("AnzahlBäder muss positiv sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidAnzahlSchlafzimmer() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültige Anzahl Schlafzimmer sollte keine Verstöße haben");
    }

    @Test
    public void testNegativeAnzahlSchlafzimmer() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, -2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit negativer Anzahl Schlafzimmer sollte Verstöße haben");
        assertEquals("AnzahlSchlafzimmer muss null oder positiv sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullAnzahlSchlafzimmer() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 0, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Wohnung mit null Anzahl Schlafzimmer sollte keine Verstöße haben");
    }

    @Test
    public void testValidHatBalkon() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, true, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültige Balkon-Angabe sollte keine Verstöße haben");
    }

    @Test
    public void testValidHatTerasse() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, true, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültige Terassen-Angabe sollte keine Verstöße haben");
    }

    @Test
    public void testValidHatGarten() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, true, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültige Garten-Angabe sollte keine Verstöße haben");
    }

    @Test
    public void testValidHatKlimaanlage() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, true, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültige Klimaanlagen-Angabe sollte keine Verstöße haben");
    }

    @Test
    public void testValidStockwerk() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, "1", null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültige Stockwerk-Angabe sollte keine Verstöße haben");
    }

    @Test
    public void testInvalidStockwerk() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, "1a", null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit ungültiger Stockwerk-Angabe sollte Verstöße haben");
        assertEquals("Illegal Stockwerk", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidWohnungsnummer() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, "A1");
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Gültige Wohnungsnummer sollte keine Verstöße haben");
    }

    @Test
    public void testInvalidWohnungsnummer() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, "A1!");
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit ungültiger Wohnungsnummer sollte Verstöße haben");
        assertEquals("Illegal Wohnungsnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullStrasse() {
        wohnung = new Wohnung(null, "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit null Straße sollte Verstöße haben");
        assertEquals("Illegal Strasse", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullHausnummer() {
        wohnung = new Wohnung("Musterstraße", null, "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit null Hausnummer sollte Verstöße haben");
        assertEquals("Illegal Hausnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullPostleitzahl() {
        wohnung = new Wohnung("Musterstraße", "11", null, "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit null Postleitzahl sollte Verstöße haben");
        assertEquals("Illegal Postleitzahl", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullStadt() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", null, Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit null Stadt sollte Verstöße haben");
        assertEquals("Illegal Stadt", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullLand() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", null, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit null Land sollte Verstöße haben");
        assertEquals("Land darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyStrasse() {
        wohnung = new Wohnung("", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit leerer Straße sollte Verstöße haben");
        assertEquals("Illegal Strasse", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyHausnummer() {
        wohnung = new Wohnung("Musterstraße", "", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit leerer Hausnummer sollte Verstöße haben");
        assertEquals("Illegal Hausnummer", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyPostleitzahl() {
        wohnung = new Wohnung("Musterstraße", "11", "", "Musterstadt", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit leerer Postleitzahl sollte Verstöße haben");
        assertEquals("Illegal Postleitzahl", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyStadt() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "", Land.DE, 200, 2000, 2, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertFalse(violations.isEmpty(), "Wohnung mit leerer Stadt sollte Verstöße haben");
        assertEquals("Illegal Stadt", violations.iterator().next().getMessage());
    }

    @Test
    public void testMinAnzahlBaeder() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 1, 2, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Wohnung mit minimaler Anzahl Bäder sollte keine Verstöße haben");
    }

    @Test
    public void testMinAnzahlSchlafzimmer() {
        wohnung = new Wohnung("Musterstraße", "11", "12345", "Musterstadt", Land.DE, 200, 2000, 2, 0, false, false, false, false, null, null);
        Set<ConstraintViolation<Wohnung>> violations = validator.validate(wohnung);
        assertTrue(violations.isEmpty(), "Wohnung mit minimaler Anzahl Schlafzimmer sollte keine Verstöße haben");
    }
}