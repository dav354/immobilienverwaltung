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

        String violationMessage = violations.iterator().next().getMessage();
        boolean isValidMessage = "Dokumententyp darf nicht leer sein".equals(violationMessage) || "Dokumententyp darf nicht null sein".equals(violationMessage);
        assertTrue(isValidMessage, "Erwartete Nachricht 'Dokumententyp darf nicht leer sein' oder 'Dokumententyp darf nicht null sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testEmptyDokumententyp() {
        dokument = new Dokument(wohnung, mieter, "", "/pfad/zu/datei.pdf");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument mit leerem Dokumententyp sollte Verstöße haben");

        String violationMessage = violations.iterator().next().getMessage();
        boolean isValidMessage = "Dokumententyp darf nicht leer sein".equals(violationMessage) || "Dokumententyp darf nicht null sein".equals(violationMessage);
        assertTrue(isValidMessage, "Erwartete Nachricht 'Dokumententyp darf nicht leer sein' oder 'Dokumententyp darf nicht null sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testNullDateipfad() {
        dokument = new Dokument(wohnung, mieter, "Rechnung", null);
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument mit null Dateipfad sollte Verstöße haben");

        String violationMessage = violations.iterator().next().getMessage();
        boolean isValidMessage = "Dateipfad darf nicht leer sein".equals(violationMessage) || "Dateipfad darf nicht null sein".equals(violationMessage);
        assertTrue(isValidMessage, "Erwartete Nachricht 'Dateipfad darf nicht leer sein' oder 'Dateipfad darf nicht null sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testEmptyDateipfad() {
        dokument = new Dokument(wohnung, mieter, "Rechnung", "");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertFalse(violations.isEmpty(), "Dokument mit leerem Dateipfad sollte Verstöße haben");

        String violationMessage = violations.iterator().next().getMessage();
        boolean isValidMessage = "Dateipfad darf nicht leer sein".equals(violationMessage) || "Dateipfad darf nicht null sein".equals(violationMessage);
        assertTrue(isValidMessage, "Erwartete Nachricht 'Dateipfad darf nicht leer sein' oder 'Dateipfad darf nicht null sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testSetDokumententypValidation() {
        testDokument.setDokumententyp("");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(testDokument);
        assertFalse(violations.isEmpty(), "Dokument mit leerem Dokumententyp sollte Verstöße haben");

        String violationMessage = violations.iterator().next().getMessage();
        boolean isValidMessage = "Dokumententyp darf nicht leer sein".equals(violationMessage) || "Dokumententyp darf nicht null sein".equals(violationMessage);
        assertTrue(isValidMessage, "Erwartete Nachricht 'Dokumententyp darf nicht leer sein' oder 'Dokumententyp darf nicht null sein', aber gefunden: " + violationMessage);

        testDokument.setDokumententyp(null);
        violations = validator.validate(testDokument);
        assertFalse(violations.isEmpty(), "Dokument mit null Dokumententyp sollte Verstöße haben");

        violationMessage = violations.iterator().next().getMessage();
        isValidMessage = "Dokumententyp darf nicht leer sein".equals(violationMessage) || "Dokumententyp darf nicht null sein".equals(violationMessage);
        assertTrue(isValidMessage, "Erwartete Nachricht 'Dokumententyp darf nicht leer sein' oder 'Dokumententyp darf nicht null sein', aber gefunden: " + violationMessage);
    }

    @Test
    public void testSetDateipfadValidation() {
        testDokument.setDateipfad("");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(testDokument);
        assertFalse(violations.isEmpty(), "Dokument mit leerem Dateipfad sollte Verstöße haben");

        String violationMessage = violations.iterator().next().getMessage();
        boolean isValidMessage = "Dateipfad darf nicht leer sein".equals(violationMessage) || "Dateipfad darf nicht null sein".equals(violationMessage);
        assertTrue(isValidMessage, "Erwartete Nachricht 'Dateipfad darf nicht leer sein' oder 'Dateipfad darf nicht null sein', aber gefunden: " + violationMessage);

        testDokument.setDateipfad(null);
        violations = validator.validate(testDokument);
        assertFalse(violations.isEmpty(), "Dokument mit null Dateipfad sollte Verstöße haben");

        violationMessage = violations.iterator().next().getMessage();
        isValidMessage = "Dateipfad darf nicht leer sein".equals(violationMessage) || "Dateipfad darf nicht null sein".equals(violationMessage);
        assertTrue(isValidMessage, "Erwartete Nachricht 'Dateipfad darf nicht leer sein' oder 'Dateipfad darf nicht null sein', aber gefunden: " + violationMessage);
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

    @Test
    public void testSetWohnungNull() {
        testDokument.setWohnung(null);
        assertNull(testDokument.getWohnung(), "Wohnung sollte null sein dürfen");
    }

    @Test
    public void testSetMieterNull() {
        testDokument.setMieter(null);
        assertNull(testDokument.getMieter(), "Mieter sollte null sein dürfen");
    }

    @Test
    public void testDokumentOhneWohnungUndMieter() {
        dokument = new Dokument(null, null, "Rechnung", "/pfad/zu/datei.pdf");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertTrue(violations.isEmpty(), "Dokument ohne Wohnung und Mieter sollte keine Verstöße haben");
    }

    @Test
    public void testMaxDokumententyp() {
        String longDokumententyp = "A".repeat(255);
        dokument = new Dokument(wohnung, mieter, longDokumententyp, "/pfad/zu/datei.pdf");
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertTrue(violations.isEmpty(), "Maximaler Dokumententyp sollte keine Verstöße haben");
    }

    @Test
    public void testMaxDateipfad() {
        String longDateipfad = "/".repeat(200) + "datei.pdf";
        dokument = new Dokument(wohnung, mieter, "Rechnung", longDateipfad);
        Set<ConstraintViolation<Dokument>> violations = validator.validate(dokument);
        assertTrue(violations.isEmpty(), "Maximaler Dateipfad sollte keine Verstöße haben");
    }
}