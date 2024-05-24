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
public class DokumentTest {

    private Validator validator;

    @Mock
    private Wohnung wohnung;

    @Mock
    private Mieter mieter;

    @InjectMocks
    private Dokument dokument;

    private Dokument testDokument;


    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        testDokument = new Dokument(wohnung, mieter, "Rechnung", "/path.png");
    }

    @Test
    public void testValidDokument() {
        dokument = new Dokument(wohnung, mieter, "Rechnung", "/pfad/zu/datei.pdf");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertTrue(violations.isEmpty(), "Valid Dokument should have no violations");
    }

    @Test
    public void testNullDokumententyp() {
        dokument = new Dokument(wohnung, mieter, null, "/pfad/zu/datei.pdf");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument with null Dokumententyp should have violations");
        assertEquals("Dokumententyp cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyDokumententyp() {
        dokument = new Dokument(wohnung, mieter, "", "/pfad/zu/datei.pdf");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument with empty Dokumententyp should have violations");
        assertEquals("Dokumententyp cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullDateipfad() {
        dokument = new Dokument(wohnung, mieter, "Rechnung", null);
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument with null Dateipfad should have violations");
        assertEquals("Dateipfad cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyDateipfad() {
        dokument = new Dokument(wohnung, mieter, "Rechnung", "");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument with empty Dateipfad should have violations");
        assertEquals("Dateipfad cannot be null", violations.iterator().next().getMessage());
    }


    @Test
    public void testNullSetDokumententyp() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testDokument.setDokumententyp(null),
                "Expected new Dokument with null Dokumententyp to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Dokumententyp cannot be null"));
    }

    @Test
    public void testEmptySetDokumententyp() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testDokument.setDokumententyp(""),
                "Expected new Dokument with empty Dokumententyp to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Dokumententyp cannot be null"));
    }

    @Test
    public void testNullSetDateipfad() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testDokument.setDateipfad(null),
                "Expected new Dokument with null Dateipfad to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Dateipfad cannot be null"));
    }

    @Test
    public void testEmptySetDateipfad() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testDokument.setDateipfad(""),
                "Expected new Dokument with empty Dateipfad to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Dateipfad cannot be null"));
    }
}