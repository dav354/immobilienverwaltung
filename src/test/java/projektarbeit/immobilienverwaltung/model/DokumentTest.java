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
        assertTrue(violations.isEmpty(), "Gültiges Dokument sollte keine Verstöße haben");
    }

    @Test
    public void testNullDokumententyp() {
        dokument = new Dokument(wohnung, mieter, null, "/pfad/zu/datei.pdf");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument mit null Dokumententyp sollte Verstöße haben");
        assertEquals("Dokumententyp darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyDokumententyp() {
        dokument = new Dokument(wohnung, mieter, "", "/pfad/zu/datei.pdf");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument mit leerem Dokumententyp sollte Verstöße haben");
        assertEquals("Dokumententyp darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullDateipfad() {
        dokument = new Dokument(wohnung, mieter, "Rechnung", null);
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument mit null Dateipfad sollte Verstöße haben");
        assertEquals("Dateipfad darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyDateipfad() {
        dokument = new Dokument(wohnung, mieter, "Rechnung", "");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument mit leerem Dateipfad sollte Verstöße haben");
        assertEquals("Dateipfad darf nicht null sein", violations.iterator().next().getMessage());
    }

    @Test
    public void testNullSetDokumententyp() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testDokument.setDokumententyp(null),
                "Erwartete Ausnahme bei setDokumententyp mit null, aber es wurde keine geworfen"
        );
        assertTrue(thrown.getMessage().contains("Dokumententyp darf nicht null sein"));
    }

    @Test
    public void testEmptySetDokumententyp() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testDokument.setDokumententyp(""),
                "Erwartete Ausnahme bei setDokumententyp mit leerem Wert, aber es wurde keine geworfen"
        );
        assertTrue(thrown.getMessage().contains("Dokumententyp darf nicht null sein"));
    }

    @Test
    public void testNullSetDateipfad() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testDokument.setDateipfad(null),
                "Erwartete Ausnahme bei setDateipfad mit null, aber es wurde keine geworfen"
        );
        assertTrue(thrown.getMessage().contains("Dateipfad darf nicht null sein"));
    }

    @Test
    public void testEmptySetDateipfad() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> testDokument.setDateipfad(""),
                "Erwartete Ausnahme bei setDateipfad mit leerem Wert, aber es wurde keine geworfen"
        );
        assertTrue(thrown.getMessage().contains("Dateipfad darf nicht null sein"));
    }

    @Test
    public void testSetWohnung() {
        Wohnung neueWohnung = new Wohnung();
        testDokument.setWohnung(neueWohnung);
        assertEquals(neueWohnung, testDokument.getWohnung(), "Die Wohnung sollte korrekt gesetzt werden");
    }

    @Test
    public void testSetMieter() {
        Mieter neuerMieter = new Mieter();
        testDokument.setMieter(neuerMieter);
        assertEquals(neuerMieter, testDokument.getMieter(), "Der Mieter sollte korrekt gesetzt werden");
    }

    @Test
    public void testToString() {
        String toString = testDokument.toString();
        assertTrue(toString.contains("dokumentId='null'"), "Die toString-Methode sollte die dokumentId enthalten");
        assertTrue(toString.contains("dokumententyp='Rechnung'"), "Die toString-Methode sollte den dokumententyp enthalten");
        assertTrue(toString.contains("dateipfad='/path.png'"), "Die toString-Methode sollte den dateipfad enthalten");
    }
}