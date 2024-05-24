package projektarbeit.immobilienverwaltung.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PostleitzahlTest {

    private Postleitzahl postleitzahl;
    private Postleitzahl testPostleitzahl;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        testPostleitzahl = new Postleitzahl("12345", "Berlin", Land.DE);
    }

    @Test
    public void testValidPostleitzahl() {
        postleitzahl = new Postleitzahl("12345", "Berlin", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertTrue(violations.isEmpty(), "Valid Postleitzahl should not have violations");
    }

    @Test
    public void testShortPostleitzahl() {
        postleitzahl = new Postleitzahl("123", "Berlin", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertFalse(violations.isEmpty(), "Postleitzahl with fewer than 4 characters should have violations");
        assertEquals("Postleitzahl must contain only numbers", violations.iterator().next().getMessage());
    }

    @Test
    public void testLongPostleitzahl() {
        postleitzahl = new Postleitzahl("12345678901", "Berlin", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertFalse(violations.isEmpty(), "Postleitzahl with more than 10 characters should have violations");
        assertEquals("Postleitzahl must contain only numbers", violations.iterator().next().getMessage());
    }

    @Test
    public void testLetterInPostleitzahl() {
        postleitzahl = new Postleitzahl("1234A", "Berlin", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertFalse(violations.isEmpty(), "Postleitzahl with letters should have violations");
        assertEquals("Postleitzahl must contain only numbers", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullPostleitzahl() {
        postleitzahl = new Postleitzahl(null, "Berlin", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertFalse(violations.isEmpty(), "Postleitzahl with null value should have violations");
        assertEquals("Postleitzahl cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyPostleitzahl() {
        postleitzahl = new Postleitzahl("", "Berlin", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertFalse(violations.isEmpty(), "Postleitzahl with empty value should have violations");
        assertEquals("Postleitzahl must contain only numbers", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidStadt() {
        postleitzahl = new Postleitzahl("12345", "Berlin", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertTrue(violations.isEmpty(), "Valid Stadt should not have violations");
    }

    @Test
    public void testEmptyStadt() {
        postleitzahl = new Postleitzahl("12345", "", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertFalse(violations.isEmpty(), "Stadt with empty value should have violations");
        assertEquals("Illegal Stadt", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullStadt() {
        postleitzahl = new Postleitzahl("12345", null, Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertFalse(violations.isEmpty(), "Stadt with null value should have violations");
        assertEquals("Illegal Stadt", violations.iterator().next().getMessage());
    }

    @Test
    public void testLongStadt() {
        String longStadt = "A".repeat(101);
        postleitzahl = new Postleitzahl("12345", longStadt, Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertFalse(violations.isEmpty(), "Stadt with more than 100 characters should have violations");
        assertEquals("Illegal Stadt", violations.iterator().next().getMessage());
    }

    @Test
    public void testNumberInStadt() {
        postleitzahl = new Postleitzahl("12345", "Berlin123", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertFalse(violations.isEmpty(), "Stadt with numbers should have violations");
        assertEquals("Illegal Stadt", violations.iterator().next().getMessage());
    }

    @Test
    public void testGermanSpecialLetterInStadt() {
        postleitzahl = new Postleitzahl("12345", "München", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertTrue(violations.isEmpty(), "Stadt with German special letters should not have violations");
    }

    @Test
    public void testValidLand() {
        postleitzahl = new Postleitzahl("12345", "Berlin", Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertTrue(violations.isEmpty(), "Valid Land should not have violations");
    }

    @Test
    public void testNullLand() {
        postleitzahl = new Postleitzahl("12345", "Berlin", null);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(postleitzahl);
        assertFalse(violations.isEmpty(), "Land with null value should have violations");
        assertEquals("Land cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testValidSetPostleitzahl() {
        testPostleitzahl.setPostleitzahl("12345");
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(testPostleitzahl);
        assertTrue(violations.isEmpty(), "Valid Postleitzahl should not have violations");
    }

    @Test
    public void testShortSetPostleitzahl() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testPostleitzahl.setPostleitzahl("123"),
                "Expected setPostleitzahl to throw, but it didn't"
        );
        assertEquals("Postleitzahl must be between 4 and 10 characters", thrown.getMessage());
    }

    @Test
    public void testLongSetPostleitzahl() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testPostleitzahl.setPostleitzahl("01234567891"),
                "Expected setPostleitzahl to throw, but it didn't"
        );
        assertEquals("Postleitzahl must be between 4 and 10 characters", thrown.getMessage());
    }

    @Test
    public void testLetterInSetPostleitzahl() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testPostleitzahl.setPostleitzahl("1234A"),
                "Expected setPostleitzahl to throw, but it didn't"
        );
        assertEquals("Postleitzahl must contain only numbers", thrown.getMessage());
    }

    @Test
    public void testNullSetPostleitzahl() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testPostleitzahl.setPostleitzahl(null),
                "Expected setPostleitzahl to throw, but it didn't"
        );
        assertEquals("Postleitzahl cannot be blank", thrown.getMessage());
    }

    @Test
    public void testEmptySetPostleitzahl() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testPostleitzahl.setPostleitzahl(""),
                "Expected setPostleitzahl to throw, but it didn't"
        );
        assertEquals("Postleitzahl cannot be blank", thrown.getMessage());
    }

    @Test
    public void testEmptySetStadt() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testPostleitzahl.setStadt(""),
                "Expected setStadt to throw, but it didn't"
        );
        assertEquals("Stadt cannot be blank", thrown.getMessage());
    }

    @Test
    public void testNullSetStadt() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testPostleitzahl.setStadt(null),
                "Expected setStadt to throw, but it didn't"
        );
        assertEquals("Stadt cannot be blank", thrown.getMessage());
    }

    @Test
    public void testLongSetStadt() {
        String longStadt = "A".repeat(101);
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testPostleitzahl.setStadt(longStadt),
                "Expected setStadt to throw, but it didn't"
        );
        assertEquals("Stadt cannot exceed 100 characters", thrown.getMessage());
    }

    @Test
    public void testNumberInSetStadt() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testPostleitzahl.setStadt("Berl1n"),
                "Expected setStadt to throw, but it didn't"
        );
        assertEquals("Stadt must contain only letters", thrown.getMessage());
    }

    @Test
    public void testGermanSpecialLetterInSetStadt() {
        testPostleitzahl.setStadt("München");
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(testPostleitzahl);
        assertTrue(violations.isEmpty(), "Stadt with German special letters should not have violations");
    }

    @Test
    public void testValidSetLand() {
        testPostleitzahl.setLand(Land.DE);
        Set<ConstraintViolation<Postleitzahl>> violations = validator.validate(testPostleitzahl);
        assertTrue(violations.isEmpty(), "Valid Land should not have violations");
    }

    @Test
    public void testNullSetLand() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testPostleitzahl.setLand(null),
                "Expected setLand to throw, but it didn't"
        );
        assertEquals("Land cannot be null", thrown.getMessage());
    }
}